package com.zyc.clover.utils.manager

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import java.io.File
import android.app.ActivityManager
import android.os.Debug


/**
 * 视频预加载管理器
 *
 * 该管理器负责维护一个ExoPlayer实例池，实现视频的预加载和缓存功能，
 * 通过合理管理播放器实例和缓冲策略，减少视频播放时的卡顿和杂音问题。
 * 采用LRU(最近最少使用)算法管理播放器实例，当池满时自动回收最久未使用的实例。
 *
 * 特性：
 * - 线程安全的播放器池管理
 * - 智能内存监控和自动清理
 * - 播放器状态回调机制
 * - 协程支持的异步操作
 */
@UnstableApi
class VideoPreloadManager(private val context: Context) {
    /** 播放器实例池，使用线程安全的ConcurrentHashMap */
    private val playerPool = ConcurrentHashMap<String, ExoPlayer>()

    /** 记录视频访问顺序，用于实现LRU缓存策略 */
    private val accessOrder = mutableListOf<String>()

    /** 读写锁，保护accessOrder的线程安全 */
    private val accessOrderLock = ReentrantReadWriteLock()

    /** 播放器池的最大容量 - 进一步优化：降低到2个以减少内存使用 */
    private val maxPoolSize = 2

    /** 当前正在播放的视频URL */
    private var currentPlayingUrl: String? = null

    /** 协程作用域，用于异步操作 */
    private val managerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    /** 内存监控相关 */
    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    private var lastMemoryCheckTime = 0L
    private val memoryCheckInterval = 10000L // 10秒检查一次内存

    /** 数据库提供者 */
    private val databaseProvider = StandaloneDatabaseProvider(context)

    /** 缓存配置 - 简化：降低缓存大小避免内存问题 */
    private val cache: SimpleCache by lazy {
        val cacheDir = File(context.cacheDir, "video_preload_cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        val cacheEvictor = LeastRecentlyUsedCacheEvictor(30 * 1024 * 1024L) // 30MB缓存，进一步减少内存使用
        SimpleCache(cacheDir, cacheEvictor,databaseProvider)
    }

    /** 缓存数据源工厂 - 简化配置 */
    private val cacheDataSourceFactory: CacheDataSource.Factory by lazy {
        val dataSourceFactory = DefaultDataSource.Factory(context)
        CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(dataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }



    /**
     * 获取或创建指定视频URL的播放器实例
     *
     * @param videoUrl 视频的URL地址
     * @param repeatMode 是否启用重复播放模式
     * @return 对应的ExoPlayer实例
     */
    fun getOrCreatePlayer(videoUrl: String, repeatMode: Boolean = false): ExoPlayer {
        // 更新访问顺序，将当前URL移至最近使用位置
        updateAccessOrder(videoUrl)
        // 从池中获取，若不存在则创建新实例
        return playerPool[videoUrl] ?: createNewPlayer(videoUrl, repeatMode)
    }

    /**
     * 创建新的ExoPlayer实例
     *
     * 当播放器池达到最大容量时，会先释放最久未使用的实例(当前播放的除外)
     *
     * @param videoUrl 视频的URL地址
     * @param repeatMode 是否启用重复播放模式
     * @return 新创建的ExoPlayer实例
     */
    @OptIn(UnstableApi::class)
    private fun createNewPlayer(videoUrl: String, repeatMode: Boolean = false): ExoPlayer {
        return try {
            createPlayerWithCache(videoUrl, repeatMode)
        } catch (e: Exception) {
            Log.e("视频预加载", "创建缓存播放器失败，使用基础配置: $videoUrl", e)
            createBasicPlayer(videoUrl, repeatMode)
        }
    }

    /**
     * 创建带缓存的播放器实例
     */
    @OptIn(UnstableApi::class)
    private fun createPlayerWithCache(videoUrl: String, repeatMode: Boolean): ExoPlayer {
        // 当池已满时，移除最久未使用的播放器(当前播放的除外)
        if (playerPool.size >= maxPoolSize) {
            // 使用读锁查找最久未使用且不是当前播放的URL
            val lruKey = accessOrderLock.read {
                accessOrder.firstOrNull { it != currentPlayingUrl }
                    ?: accessOrder.firstOrNull() // 如果所有都是当前播放的(理论上不可能)，则移除第一个
            }

            lruKey?.let { key ->
                // 释放并移除该播放器
                playerPool[key]?.let { player ->
                    player.stop()
                    player.release()
                }
                playerPool.remove(key)
                accessOrderLock.write {
                    accessOrder.remove(key)
                }
                Log.d("视频预加载", "LRU清理播放器: $key")
            }
        }

        // 更保守的加载控制器配置 - 减少缓冲以降低内存使用
         val loadControl = DefaultLoadControl.Builder()
             .setBufferDurationsMs(
                 1500,   // 最小缓冲时长：1.5秒
                 5000,   // 最大缓冲时长：5秒
                 500,    // 播放启动缓冲：0.5秒
                 800     // 播放后继续缓冲：0.8秒
             )
             .build()

         // 音频属性配置
         val audioAttributes = AudioAttributes.Builder()
             .setUsage(C.USAGE_MEDIA)
             .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
             .build()

         // 创建媒体源工厂
         val mediaSourceFactory = DefaultMediaSourceFactory(cacheDataSourceFactory)

         // 构建ExoPlayer实例
         val player = ExoPlayer.Builder(context)
             .setMediaSourceFactory(mediaSourceFactory)
             .setLoadControl(loadControl)
             .setAudioAttributes(audioAttributes, true)
             .build().apply {
                // 设置媒体资源
                val mediaItem = androidx.media3.common.MediaItem.fromUri(videoUrl)
                setMediaItem(mediaItem)
                // 初始状态为不自动播放
                playWhenReady = false
                // 设置初始音量
                volume = 1.0f
                // 设置重复模式
                this.repeatMode = if (repeatMode) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                // 准备播放器
                prepare()

                // 添加播放器状态监听器
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_ENDED -> {
                                if (!repeatMode) {
                                    pause()
                                }
                            }
                            Player.STATE_READY -> {
                                // 关键修复7: 播放器就绪时确保视频帧可见
                                Log.d("视频预加载", "播放器就绪: $videoUrl")
                                // 确保视频表面已准备好
                                if (videoUrl == currentPlayingUrl && !playWhenReady) {
                                    // 预加载第一帧但不播放
                                    seekTo(0)
                                }
                            }
                            Player.STATE_BUFFERING -> {
                                Log.d("视频预加载", "缓冲中: $videoUrl")
                            }
                            Player.STATE_IDLE -> {
                                Log.w("视频预加载", "播放器空闲状态: $videoUrl")
                                // 关键修复8: 改进空闲状态处理，避免无限重试
                                if (videoUrl == currentPlayingUrl) {
                                    managerScope.launch {
                                        delay(1000) // 增加延迟时间
                                        if (playbackState == Player.STATE_IDLE) {
                                            try {
                                                Log.d("视频预加载", "尝试重新准备播放器: $videoUrl")
                                                val mediaItem = androidx.media3.common.MediaItem.fromUri(videoUrl)
                                                setMediaItem(mediaItem)
                                                prepare()
                                            } catch (e: Exception) {
                                                Log.e("视频预加载", "重新准备播放器失败: $videoUrl", e)
                                                // 如果重试失败，从池中移除该播放器
                                                playerPool.remove(videoUrl)
                                                accessOrderLock.write {
                                                    accessOrder.remove(videoUrl)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 关键修复9: 增强播放错误处理和恢复机制
                    override fun onPlayerError(error: PlaybackException) {
                        Log.e("视频预加载", "播放器错误 ($videoUrl): ${error.message}", error)

                        // 根据错误类型进行不同处理
                        when (error.errorCode) {
                            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
                                Log.w("视频预加载", "网络错误，尝试重新加载: $videoUrl")
                                // 网络错误，延迟后重试
                                managerScope.launch {
                                    delay(2000)
                                    try {
                                        val mediaItem = androidx.media3.common.MediaItem.fromUri(videoUrl)
                                        setMediaItem(mediaItem)
                                        prepare()
                                    } catch (e: Exception) {
                                        Log.e("视频预加载", "重试失败: $videoUrl", e)
                                        // 重试失败，从池中移除
                                        playerPool.remove(videoUrl)
                                        accessOrderLock.write {
                                            accessOrder.remove(videoUrl)
                                        }
                                    }
                                }
                            }
                            PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED,
                            PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> {
                                Log.e("视频预加载", "视频格式错误，移除播放器: $videoUrl")
                                // 格式错误，直接移除
                                playerPool.remove(videoUrl)
                                accessOrderLock.write {
                                    accessOrder.remove(videoUrl)
                                }
                            }
                            else -> {
                                Log.e("视频预加载", "未知错误，移除播放器: $videoUrl")
                                // 其他错误，移除播放器
                                playerPool.remove(videoUrl)
                                accessOrderLock.write {
                                    accessOrder.remove(videoUrl)
                                }
                            }
                        }

                        // 如果是当前播放的视频出错，清除当前播放状态
                        if (videoUrl == currentPlayingUrl) {
                            currentPlayingUrl = null
                        }
                    }
                })
            }

        // 将新创建的播放器添加到池中
        playerPool[videoUrl] = player
        // 更新访问顺序
        updateAccessOrder(videoUrl)
        return player
    }

    /**
     * 创建基础播放器实例（无缓存）
     */
    private fun createBasicPlayer(videoUrl: String, repeatMode: Boolean): ExoPlayer {
        // 当池已满时，移除最久未使用的播放器(当前播放的除外)
        if (playerPool.size >= maxPoolSize) {
            val lruKey = accessOrderLock.read {
                accessOrder.firstOrNull { it != currentPlayingUrl }
                    ?: accessOrder.firstOrNull()
            }

            lruKey?.let { key ->
                playerPool[key]?.let { player ->
                    player.stop()
                    player.release()
                }
                playerPool.remove(key)
                accessOrderLock.write {
                    accessOrder.remove(key)
                }
                Log.d("视频预加载", "LRU清理播放器: $key")
            }
        }

        // 基础播放器配置
        val player = ExoPlayer.Builder(context).build().apply {
            val mediaItem = androidx.media3.common.MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)
            playWhenReady = false
            volume = 1.0f
            this.repeatMode = if (repeatMode) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
            prepare()

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_ENDED -> {
                            if (!repeatMode) {
                                pause()
                            }
                        }
                        Player.STATE_IDLE -> {
                            if (videoUrl == currentPlayingUrl) {
                                managerScope.launch {
                                    delay(500)
                                    if (playbackState == Player.STATE_IDLE) {
                                        try {
                                            prepare()
                                        } catch (e: Exception) {
                                            Log.e("视频预加载", "重新准备播放器失败: $videoUrl", e)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    Log.e("视频预加载", "播放器错误 ($videoUrl): ${error.message}")
                    if (videoUrl == currentPlayingUrl) {
                        currentPlayingUrl = null
                    }
                }
            })
        }

        playerPool[videoUrl] = player
        updateAccessOrder(videoUrl)
        return player
    }

    /**
     * 更新视频访问顺序，将指定URL移至列表末尾(表示最近使用)
     * 使用读写锁保证线程安全
     *
     * @param videoUrl 视频的URL地址
     */
    private fun updateAccessOrder(videoUrl: String) {
        accessOrderLock.write {
            // 先移除再添加，确保在列表末尾
            accessOrder.remove(videoUrl)
            accessOrder.add(videoUrl)
        }
    }

    /**
     * 预加载指定视频 - 添加内存检查
     */
    fun preloadVideo(videoUrl: String, repeatMode: Boolean = false) {
        if (!playerPool.containsKey(videoUrl) && videoUrl.isNotEmpty()) {
            // 预加载前检查内存状态
            if (checkMemoryAndCleanup()) {
                managerScope.launch {
                    try {
                        createNewPlayer(videoUrl, repeatMode)
                        Log.d("视频预加载", "预加载成功: $videoUrl")
                    } catch (e: Exception) {
                        Log.e("视频预加载", "预加载失败: $videoUrl", e)
                    }
                }
            } else {
                Log.w("视频预加载", "内存不足，跳过预加载: $videoUrl")
            }
        }
    }

    /**
     * 设置当前正在播放的视频URL
     *
     * @param videoUrl 视频的URL地址，可为null表示无播放中的视频
     */
    fun setCurrentPlaying(videoUrl: String?) {
        currentPlayingUrl = videoUrl
    }

    /**
     * 获取当前正在播放的视频URL
     *
     * @return 当前播放的视频URL，可为null
     */
    fun getCurrentPlaying(): String? = currentPlayingUrl

    /**
     * 释放所有播放器资源
     */
    fun releaseAll() {
        playerPool.values.forEach { player ->
            player.stop()
            player.release()
        }
        playerPool.clear()
        accessOrder.clear()
        currentPlayingUrl = null
    }

    /**
     * 暂停所有播放器
     */
    fun pauseAll() {
        playerPool.values.forEach { player ->
            player.pause()
            player.playWhenReady = false
        }
    }

    /**
     * 暂停除指定视频外的所有播放器
     */
    fun pauseAllExcept(excludeUrl: String) {
        playerPool.forEach { (url, player) ->
            if (url != excludeUrl) {
                player.pause()
                player.playWhenReady = false
            }
        }
    }

    /**
     * 播放指定视频
     */
    fun playVideo(videoUrl: String) {
        playerPool[videoUrl]?.let { player ->
            pauseAllExcept(videoUrl)
            setCurrentPlaying(videoUrl)

            player.playWhenReady = true
            if (player.playbackState == Player.STATE_IDLE) {
                player.prepare()
            }
            player.play()
        }
    }

    /**
     * 暂停指定视频
     */
    fun pauseVideo(videoUrl: String) {
        playerPool[videoUrl]?.let { player ->
            player.pause()
            player.playWhenReady = false
            if (currentPlayingUrl == videoUrl) {
                currentPlayingUrl = null
            }
        }
    }

    /**
     * 设置指定视频的音量
     */
    fun setVolume(videoUrl: String, volume: Float) {
        val clampedVolume = volume.coerceIn(0.0f, 1.0f)
        playerPool[videoUrl]?.let { player ->
            player.volume = clampedVolume
        }
    }

    /**
     * 设置指定视频的重复播放模式
     */
    fun setRepeatMode(videoUrl: String, repeatMode: Boolean) {
        playerPool[videoUrl]?.let { player ->
            player.repeatMode = if (repeatMode) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        }
    }

    /**
     * 静音/取消静音指定视频
     *
     * @param videoUrl 视频URL
     * @param mute 是否静音
     */
    fun setMute(videoUrl: String, mute: Boolean) {
        setVolume(videoUrl, if (mute) 0.0f else 1.0f)
    }

    /**
     * 安全获取播放器实例（只读）
     *
     * @param videoUrl 视频URL
     * @return 播放器实例，可能为null
     */
    fun getPlayerSafely(videoUrl: String): ExoPlayer? {
        return playerPool[videoUrl]
    }



    /**
     * 检查播放器是否存在
     *
     * @param videoUrl 视频URL
     * @return 是否存在对应的播放器
     */
    fun hasPlayer(videoUrl: String): Boolean {
        return playerPool.containsKey(videoUrl)
    }

    /**
     * 获取播放器状态
     *
     * @param videoUrl 视频URL
     * @return 播放器状态，如果不存在返回null
     */
    fun getPlayerState(videoUrl: String): Int? {
        return playerPool[videoUrl]?.playbackState
    }

    /**
     * 强制清理指定视频的播放器
     */
    fun forceReleasePlayer(videoUrl: String) {
        playerPool[videoUrl]?.let { player ->
            player.stop()
            player.release()
        }
        playerPool.remove(videoUrl)
        accessOrderLock.write {
            accessOrder.remove(videoUrl)
        }
        if (currentPlayingUrl == videoUrl) {
            currentPlayingUrl = null
        }
    }

    /**
     * 内存压力时的清理策略
     * 保留当前播放的视频，清理其他所有视频
     */
    fun onMemoryPressure() {
        Log.w("视频预加载", "内存压力，开始清理播放器池")
        val currentPlaying = currentPlayingUrl

        playerPool.entries.removeAll { (url, player) ->
            if (url != currentPlaying) {
                player.stop()
                player.release()
                accessOrderLock.write {
                    accessOrder.remove(url)
                }
                Log.d("视频预加载", "内存清理释放播放器: $url")
                true
            } else {
                false
            }
        }
    }

    /**
     * 获取详细的缓存信息
     *
     * @return 包含缓存数量、状态和URL的详细信息
     */
    fun getCacheInfo(): String {
        val stateInfo = playerPool.map { (url, player) ->
            val state = when (player.playbackState) {
                Player.STATE_IDLE -> "空闲"
                Player.STATE_BUFFERING -> "缓冲中"
                Player.STATE_READY -> "就绪"
                Player.STATE_ENDED -> "结束"
                else -> "未知"
            }
            "$url($state)"
        }.joinToString(", ")

        return "缓存视频: ${playerPool.size}/$maxPoolSize\n" +
                "当前播放: ${currentPlayingUrl ?: "无"}\n" +
                "详细状态: $stateInfo"
    }

    /**
     * 智能内存检查和清理
     * @return 是否有足够内存继续操作
     */
    private fun checkMemoryAndCleanup(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastMemoryCheckTime < memoryCheckInterval) {
            return true // 间隔时间内不重复检查
        }
        lastMemoryCheckTime = currentTime

        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        // 计算内存使用率
        val usedMemoryPercent = (memoryInfo.totalMem - memoryInfo.availMem).toFloat() / memoryInfo.totalMem

        Log.d("内存监控", "内存使用率: ${(usedMemoryPercent * 100).toInt()}%")

        // 如果内存使用率超过80%，进行清理
        if (usedMemoryPercent > 0.8f) {
            Log.w("内存监控", "内存使用率过高，开始清理")
            onMemoryPressure()
            return false
        }

        // 如果内存使用率超过70%且播放器池不为空，清理非当前播放的视频
        if (usedMemoryPercent > 0.7f && playerPool.size > 1) {
            Log.w("内存监控", "内存使用率较高，清理部分缓存")
            cleanupNonCurrentPlayers()
        }

        return true
    }

    /**
     * 清理非当前播放的播放器
     */
    private fun cleanupNonCurrentPlayers() {
        val currentPlaying = currentPlayingUrl
        val toRemove = mutableListOf<String>()

        playerPool.forEach { (url, player) ->
            if (url != currentPlaying) {
                player.stop()
                player.release()
                toRemove.add(url)
            }
        }

        toRemove.forEach { url ->
            playerPool.remove(url)
            accessOrderLock.write {
                accessOrder.remove(url)
            }
            Log.d("内存清理", "清理播放器: $url")
        }
    }

    /**
     * 销毁管理器，释放所有资源
     */
    fun destroy() {
        managerScope.cancel()
        releaseAll()
        // 释放缓存资源
        try {
            cache.release()
        } catch (e: Exception) {
            Log.e("视频预加载", "释放缓存失败", e)
        }
    }
}
