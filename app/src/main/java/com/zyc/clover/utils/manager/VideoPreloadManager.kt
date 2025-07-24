package com.zyc.clover.utils.manager

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write


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
class VideoPreloadManager(private val context: Context) {
    /** 播放器实例池，使用线程安全的ConcurrentHashMap */
    private val playerPool = ConcurrentHashMap<String, ExoPlayer>()

    /** 记录视频访问顺序，用于实现LRU缓存策略 */
    private val accessOrder = mutableListOf<String>()

    /** 读写锁，保护accessOrder的线程安全 */
    private val accessOrderLock = ReentrantReadWriteLock()

    /** 播放器池的最大容量 */
    private val maxPoolSize = 3

    /** 当前正在播放的视频URL */
    private var currentPlayingUrl: String? = null

    /** 协程作用域，用于异步操作 */
    private val managerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    /** 播放器状态回调接口 */
    interface PlayerStateCallback {
        fun onPlayerReady(videoUrl: String)
        fun onPlayerBuffering(videoUrl: String, receivedBufferPercent: Int)
        fun onPlayerError(videoUrl: String, error: String)
        fun onPlayerEnded(videoUrl: String)
    }

    /** 播放器状态回调 */
    private var stateCallback: PlayerStateCallback? = null

    /**
     * 获取或创建指定视频URL的播放器实例
     *
     * @param videoUrl 视频的URL地址
     * @return 对应的ExoPlayer实例
     */
    fun getOrCreatePlayer(videoUrl: String): ExoPlayer {
        // 更新访问顺序，将当前URL移至最近使用位置
        updateAccessOrder(videoUrl)
        // 从池中获取，若不存在则创建新实例
        return playerPool[videoUrl] ?: createNewPlayer(videoUrl)
    }

    /**
     * 创建新的ExoPlayer实例
     *
     * 当播放器池达到最大容量时，会先释放最久未使用的实例(当前播放的除外)
     *
     * @param videoUrl 视频的URL地址
     * @return 新创建的ExoPlayer实例
     */
    @OptIn(UnstableApi::class)
    private fun createNewPlayer(videoUrl: String): ExoPlayer {
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

        // 构建新的ExoPlayer实例并配置
        val player = ExoPlayer.Builder(context)
            // 配置缓冲策略，优化播放流畅度
            .setLoadControl(
                androidx.media3.exoplayer.DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                        10000,  // 最小缓冲时长(毫秒)：10秒
                        30000,  // 最大缓冲时长(毫秒)：30秒
                        1000,   // 播放启动所需的缓冲时长(毫秒)：1秒
                        3000    // 播放后继续缓冲的时长(毫秒)：3秒
                    )
                    .build()
            )
            // 配置音频属性，减少杂音
            .setAudioAttributes(
                androidx.media3.common.AudioAttributes.Builder()
                    .setUsage(androidx.media3.common.C.USAGE_MEDIA)
                    .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MOVIE)
                    .build(),
                true // 允许音频焦点自动处理
            )
            .build().apply {
                // 设置媒体资源
                val mediaItem = androidx.media3.common.MediaItem.fromUri(videoUrl)
                setMediaItem(mediaItem)
                // 初始状态为不自动播放
                playWhenReady = false
                // 设置初始音量
                volume = 1.0f
                // 准备播放器
                prepare()

                // 添加播放器状态监听器
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            // 播放结束时暂停并通知回调
                            Player.STATE_ENDED -> {
                                pause()
                                stateCallback?.onPlayerEnded(videoUrl)
                                Log.d("视频预加载", "播放结束: $videoUrl")
                            }
                            // 准备就绪时输出日志并通知回调
                            Player.STATE_READY -> {
                                Log.d("视频预加载", "播放器已就绪: $videoUrl")
                                stateCallback?.onPlayerReady(videoUrl)
                            }
                            // 缓冲状态，可显示加载指示器
                            Player.STATE_BUFFERING -> {
                                // 获取当前缓冲进度
                                val bufferedPosition = bufferedPosition
                                val duration = duration
                                val bufferPercent = if (duration > 0) (bufferedPosition * 100 / duration).toInt() else 0
                                Log.d("视频预加载", "缓冲中: $videoUrl，已缓冲: $bufferPercent%")
                                stateCallback?.onPlayerBuffering(videoUrl, bufferPercent)
                            }
                            // 空闲状态，可能需要重新准备
                            Player.STATE_IDLE -> {
                                Log.d("视频预加载", "播放器空闲状态: $videoUrl")
                                // 如果是当前播放的视频，尝试重新准备
                                if (videoUrl == currentPlayingUrl) {
                                    managerScope.launch {
                                        delay(500) // 延迟500ms后重试
                                        if (playbackState == Player.STATE_IDLE) {
                                            try {
                                                prepare()
                                                Log.d("视频预加载", "重新准备播放器: $videoUrl")
                                            } catch (e: Exception) {
                                                Log.e("视频预加载", "重新准备播放器失败: $videoUrl", e)
                                                stateCallback?.onPlayerError(videoUrl, "重新准备失败: ${e.message}")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 播放错误时输出日志并通知回调
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        val errorMsg = "播放器错误 ($videoUrl): ${error.message}"
                         Log.e("视频预加载", errorMsg)
                        stateCallback?.onPlayerError(videoUrl, error.message ?: "未知错误")

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
     * 预加载指定视频
     *
     * @param videoUrl 视频的URL地址
     */
    fun preloadVideo(videoUrl: String) {
        // 如果池中没有该视频的播放器且URL不为空，则预加载
        if (!playerPool.containsKey(videoUrl) && videoUrl.isNotEmpty()) {
            try {
                createNewPlayer(videoUrl)
                 Log.d("视频预加载", "预加载成功: $videoUrl")
            } catch (e: Exception) {
                Log.e("视频预加载", "预加载失败: $videoUrl", e)
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
        Log.d("视频预加载", "暂停所有播放器")
    }

    /**
     * 暂停除指定视频外的所有播放器
     *
     * @param excludeUrl 不暂停的视频URL
     */
    fun pauseAllExcept(excludeUrl: String) {
        playerPool.forEach { (url, player) ->
            if (url != excludeUrl) {
                player.pause()
                player.playWhenReady = false
            }
        }
        Log.d("视频预加载", "暂停除 $excludeUrl 外的所有播放器")
    }

    /**
     * 播放指定视频
     *
     * @param videoUrl 视频的URL地址
     */
    fun playVideo(videoUrl: String) {
        playerPool[videoUrl]?.let { player ->
            // 暂停其他所有播放器
            pauseAllExcept(videoUrl)
            // 设置当前播放
            setCurrentPlaying(videoUrl)

            // 根据当前状态决定如何播放
            if (player.playbackState == androidx.media3.common.Player.STATE_READY ||
                player.playbackState == androidx.media3.common.Player.STATE_BUFFERING) {
                player.playWhenReady = true
                player.play()
            } else {
                // 尚未准备好时，设置为准备好后自动播放
                player.playWhenReady = true
            }
            Log.d("视频预加载", "开始播放视频: $videoUrl")
        } ?: Log.w("视频预加载", "播放失败，未找到播放器: $videoUrl")
    }

    /**
     * 暂停指定视频
     *
     * @param videoUrl 视频的URL地址
     */
    fun pauseVideo(videoUrl: String) {
        playerPool[videoUrl]?.let { player ->
            player.pause()
            player.playWhenReady = false
            // 如果暂停的是当前播放的视频，清除当前播放状态
            if (currentPlayingUrl == videoUrl) {
                currentPlayingUrl = null
            }
            Log.d("视频预加载", "暂停视频: $videoUrl")
        } ?: Log.w("视频预加载", "暂停失败，未找到播放器: $videoUrl")
    }

    /**
     * 设置指定视频的音量
     *
     * @param videoUrl 视频URL
     * @param volume 音量值(0.0-1.0)
     */
    fun setVolume(videoUrl: String, volume: Float) {
        val clampedVolume = volume.coerceIn(0.0f, 1.0f)
        playerPool[videoUrl]?.let { player ->
            player.volume = clampedVolume
            Log.d("视频预加载", "设置音量: $videoUrl = $clampedVolume")
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
     * 设置播放器状态回调
     *
     * @param callback 状态回调接口
     */
    fun setStateCallback(callback: PlayerStateCallback?) {
        this.stateCallback = callback
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
     *
     * @param videoUrl 视频URL
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
        Log.d("视频预加载", "强制释放播放器: $videoUrl")
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
     * 销毁管理器，释放所有资源
     */
    fun destroy() {
        managerScope.cancel()
        releaseAll()
        stateCallback = null
        Log.d("视频预加载", "VideoPreloadManager已销毁")
    }
}
