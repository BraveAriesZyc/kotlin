package com.zyc.clover.components.medium

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource

import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.database.StandaloneDatabaseProvider

import java.io.File
import com.zyc.clover.R
import com.zyc.clover.di.databaseModel
import kotlinx.coroutines.delay
import kotlin.math.abs


@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    externalPlayer: ExoPlayer? = null,
    repeatMode: Boolean = true
) {
    val context = LocalContext.current

    // 状态变量
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var hasUserInteracted by remember { mutableStateOf(false) } // 用户是否已经点击过页面
    var showControls by remember { mutableStateOf(false) } // 初始不显示控制器
    var volume by remember { mutableFloatStateOf(1f) }
    var isMuted by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) } // 是否正在拖拽进度条
    // 移除封面图片功能
    var isBuffering by remember { mutableStateOf(false) }
    var playerState by remember { mutableIntStateOf(Player.STATE_IDLE) } // 缓冲状态
    var isVideoReady by remember { mutableStateOf(false) } // 视频是否已准备好显示

    // 1. 优先使用外部传入的播放器，否则创建新的
    val player = externalPlayer ?: remember(context) {
        try {
            // 创建缓存目录
            val cacheDir = File(context.cacheDir, "video_cache")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            // 简化缓存配置：降低缓存大小，避免内存问题
            val cacheEvictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024L) // 100MB缓存
            val databaseProvider = StandaloneDatabaseProvider(context)
            val cache = SimpleCache(
                cacheDir,
                cacheEvictor,
                databaseProvider
            )

            // 创建缓存数据源工厂
            val dataSourceFactory = DefaultDataSource.Factory(context)
            val cacheDataSourceFactory = CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(dataSourceFactory)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

            // 关键修复14: 优化缓冲配置，减少黑屏和卡顿
            val loadControl = DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    2000,   // 最小缓冲时长：2秒（减少初始等待时间）
                    10000,  // 最大缓冲时长：10秒（减少内存使用）
                    500,    // 播放启动缓冲：0.5秒（更快开始播放）
                    1000    // 播放后继续缓冲：1秒（减少重新缓冲）
                )
                .setTargetBufferBytes(-1) // 使用默认目标缓冲字节数
                .setPrioritizeTimeOverSizeThresholds(true) // 优先考虑时间而非大小
                .build()

            // 音频属性配置
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                .build()

            // 创建媒体源工厂
            val mediaSourceFactory = DefaultMediaSourceFactory(cacheDataSourceFactory)

            // 构建ExoPlayer
            ExoPlayer.Builder(context)
                .setMediaSourceFactory(mediaSourceFactory)
                .setLoadControl(loadControl)
                .setAudioAttributes(audioAttributes, true)
                .build()
        } catch (e: Exception) {
            android.util.Log.e("VideoPlayer", "创建播放器失败，使用基础配置", e)
            // 如果缓存配置失败，使用基础配置
            ExoPlayer.Builder(context).build()
        }
    }

    // 设置重复播放模式
    LaunchedEffect(repeatMode) {
        player.repeatMode = if (repeatMode) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
    }

    // 移除封面图片加载功能


    // 处理视频地址变化
    if (externalPlayer == null) {
        LaunchedEffect(videoUrl) {
            if (videoUrl.isNotEmpty()) {
                try {
                    isVideoReady = false // 重置视频准备状态
                    val mediaItem = MediaItem.fromUri(videoUrl)
                    player.setMediaItem(mediaItem)

                    // 设置自动播放
                    player.playWhenReady = true
                    player.prepare()

                    android.util.Log.d("VideoPlayer", "设置视频源: $videoUrl，playWhenReady=true")
                } catch (e: Exception) {
                    android.util.Log.e("VideoPlayer", "设置视频源失败: ${e.message}")
                    isVideoReady = false
                }
            } else {
                player.clearMediaItems()
                player.playWhenReady = true
                isVideoReady = false // 清空时重置状态
                android.util.Log.d("VideoPlayer", "清空视频源")
            }
        }
    }


    // 4. 只有在没有外部播放器时才在组件销毁时释放资源
    if (externalPlayer == null) {
        DisposableEffect(Unit) {
            onDispose {
                try {
                    player.stop()
                    player.release() // 必须释放，否则会内存泄漏
                } catch (e: Exception) {
                    android.util.Log.e("视频播放器", "释放播放器时出错", e)
                }
            }
        }
    }

    // 监听播放器状态变化
    val playerListener = remember {
        object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
                android.util.Log.d("VideoPlayer", "播放状态变化: $playing")
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        duration = player.duration
                        isBuffering = false
                        playerState = Player.STATE_READY
                        isVideoReady = true // 视频准备好，可以显示
                        android.util.Log.d("VideoPlayer", "播放器准备完成，时长: $duration")
                        // 自动开始播放
                        player.playWhenReady = true
                        android.util.Log.d("VideoPlayer", "播放器准备完成，开始自动播放")
                    }

                    Player.STATE_ENDED -> {
                        if (!repeatMode) {
                            isPlaying = false
                        }
                        isBuffering = false
                        playerState = Player.STATE_ENDED
                        android.util.Log.d("VideoPlayer", "播放结束")
                    }

                    Player.STATE_BUFFERING -> {
                        android.util.Log.d("VideoPlayer", "缓冲中")
                        isBuffering = true
                        playerState = Player.STATE_BUFFERING
                    }

                    Player.STATE_IDLE -> {
                        android.util.Log.w("VideoPlayer", "播放器空闲状态 - 可能需要重新初始化")
                        isBuffering = false
                        playerState = Player.STATE_IDLE
                        isVideoReady = false // 重置视频准备状态
                        // 关键修复12: 空闲状态时尝试恢复
                        if (hasUserInteracted && videoUrl.isNotEmpty()) {
                            android.util.Log.d("VideoPlayer", "尝试从空闲状态恢复")
                            try {
                                val mediaItem = MediaItem.fromUri(videoUrl)
                                player.setMediaItem(mediaItem)
                                player.prepare()
                            } catch (e: Exception) {
                                android.util.Log.e("VideoPlayer", "恢复播放器失败", e)
                            }
                        }
                    }
                }
            }

            // 关键修复13: 添加播放器错误处理
            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                android.util.Log.e("VideoPlayer", "播放器错误: ${error.message}", error)

                // 错误时重置播放状态
                isPlaying = false
                playerState = Player.STATE_IDLE
                isVideoReady = false // 错误时重置视频准备状态

                // 根据错误类型进行处理
                when (error.errorCode) {
                    androidx.media3.common.PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                    androidx.media3.common.PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
                        android.util.Log.w("VideoPlayer", "网络连接错误，尝试重新加载")
                        // 网络错误，延迟后重试
                        if (videoUrl.isNotEmpty()) {
                            try {
                                val mediaItem = MediaItem.fromUri(videoUrl)
                                player.setMediaItem(mediaItem)
                                player.prepare()
                            } catch (e: Exception) {
                                android.util.Log.e("VideoPlayer", "重新加载失败", e)
                            }
                        }
                    }

                    androidx.media3.common.PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED,
                    androidx.media3.common.PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> {
                        android.util.Log.e("VideoPlayer", "视频格式错误，无法播放")
                        // 格式错误，停止播放
                        isPlaying = false
                    }

                    else -> {
                        android.util.Log.e("VideoPlayer", "未知播放器错误")
                        // 其他错误，尝试重新初始化
                        if (videoUrl.isNotEmpty()) {
                            try {
                                player.stop()
                                val mediaItem = MediaItem.fromUri(videoUrl)
                                player.setMediaItem(mediaItem)
                                player.prepare()
                            } catch (e: Exception) {
                                android.util.Log.e("VideoPlayer", "重新初始化失败", e)
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(player, playerListener) {
        player.addListener(playerListener)
    }

    // 优化播放进度更新 - 使用更智能的更新策略
    LaunchedEffect(isPlaying, isDragging) {
        while (isPlaying && !isDragging) {
            val newPosition = player.currentPosition
            // 只有位置变化超过1秒时才更新，进一步减少重绘
            if (kotlin.math.abs(newPosition - currentPosition) > 1000) {
                currentPosition = newPosition
            }
            delay(500) // 降低更新频率到500ms
        }
    }

    // 自动隐藏控制器 - 只有在播放状态且用户已经交互过后才自动隐藏
    LaunchedEffect(showControls, isDragging, hasUserInteracted, isPlaying) {
        if (showControls && !isDragging && hasUserInteracted && isPlaying) {
            delay(3000)
            showControls = false
        }
    }

    Box(modifier = modifier) {
        // ExoPlayer视频播放器 - 只有在视频准备好时才显示
        if (isVideoReady) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = false
                        this.player = player
                        // 关键修复: 使用FIT模式防止视频加载时的跳跃拉伸
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

                        // 关键修复1: 设置黑屏背景为透明，避免黑屏问题
                        setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)

                        // 关键修复2: 显示缓冲状态，让用户知道视频正在加载
                        setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)

                        // 关键修复3: 保持内容显示，避免重置时的黑屏
                        setKeepContentOnPlayerReset(true)
                        // 允许播放器自动开始播放
                        player.playWhenReady = true

                        // 防止视频加载时的视觉跳跃
                        defaultArtwork = null
                        // 禁用所有UI控制元素
                        controllerAutoShow = false
                        setControllerHideOnTouch(false)
                        setControllerShowTimeoutMs(0)
                        setShowNextButton(false)
                        setShowPreviousButton(false)
                        setShowFastForwardButton(false)
                        setShowRewindButton(false)
                        setShowShuffleButton(false)
                        setShowSubtitleButton(false)
                        setShowVrButton(false)

                        // 关键修复5: PlayerView会自动处理视频表面配置
                        // 无需手动设置videoSurfaceView属性

                        android.util.Log.d("VideoPlayer", "PlayerView初始化完成")
                    }
                },
                update = { playerView ->
                    if (playerView.player != player) {
                        playerView.player = player
                        // 关键修复6: 更新播放器时重新设置关键属性
                        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        playerView.setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                        playerView.setKeepContentOnPlayerReset(true)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                    .clickable {
                        hasUserInteracted = true
                        if (isPlaying) {
                            player.pause()
                            showControls = true
                        } else {
                            player.play()
                            showControls = !showControls
                            android.util.Log.d("VideoPlayer", "用户点击播放，开始播放视频")
                        }
                    }
            )
        } else {
            // 视频未准备好时显示占位符
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                if (isBuffering || playerState == Player.STATE_BUFFERING) {
                    LoadingIndicator()
                }
            }
        }

        // 移除封面图片显示功能

        // 缓冲加载指示器 - 只在视频准备好且正在缓冲时显示
        if (isVideoReady && isBuffering && hasUserInteracted) {
            LoadingIndicator()
        }

        // 控制器覆盖层
        if (showControls) {
            // 记忆化回调函数以避免重复创建
            val onPlayPause = remember {
                {
                    if (isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                }
            }

            val onSeek = remember {
                { position: Long ->
                    player.seekTo(position)
                    currentPosition = position
                }
            }

            val onProgressChange = remember {
                { progress: Float ->
                    val newPosition = (progress * duration).toLong()
                    player.seekTo(newPosition)
                    currentPosition = newPosition
                }
            }

            val onDragStart = remember {
                {
                    isDragging = true
                }
            }

            val onDragEnd = remember {
                {
                    isDragging = false
                }
            }

            val onVolumeChange = remember {
                { newVolume: Float ->
                    volume = newVolume
                    player.volume = newVolume
                }
            }

            val onMuteToggle = remember {
                {
                    isMuted = !isMuted
                    player.volume = if (isMuted) 0f else volume
                }
            }

            CustomVideoControls(
                isPlaying = isPlaying,
                currentPosition = currentPosition,
                duration = duration,
                volume = volume,
                isMuted = isMuted,
                isDragging = isDragging,
                onPlayPause = onPlayPause,
                onSeek = onSeek,
                onProgressChange = onProgressChange,
                onDragStart = onDragStart,
                onDragEnd = onDragEnd,
                onVolumeChange = onVolumeChange,
                onMuteToggle = onMuteToggle,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun CustomVideoControls(
    isPlaying: Boolean,
    currentPosition: Long,
    duration: Long,
    volume: Float,
    isMuted: Boolean,
    isDragging: Boolean,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onProgressChange: (Float) -> Unit,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    onMuteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 使用derivedStateOf优化进度计算
    val progress by remember {
        derivedStateOf {
            if (duration > 0) currentPosition.toFloat() / duration else 0f
        }
    }

    // 记忆化格式化时间以避免重复计算
    val currentTimeFormatted by remember {
        derivedStateOf { formatTime(currentPosition) }
    }

    val totalTimeFormatted by remember {
        derivedStateOf { formatTime(duration) }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        content = {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable(
                        onClick = onPlayPause, // 禁用默认水波纹效果
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    // 中央播放按钮 - 只在暂停时显示
                    if (!isPlaying) {
                        Text(
                            text = "\uEDCF", // 播放图标
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 80.sp,
                            fontFamily = FontFamily(Font(R.font.icons))
                        )
                    }
                }

            )
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    content = {
                        CustomProgressBar(
                            progress = progress,
                            onProgressChange = onProgressChange,
                            onDragStart = onDragStart,
                            onDragEnd = onDragEnd,
                            currentTime = currentTimeFormatted,
                            totalTime = totalTimeFormatted,
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                        )
                    }
                )
            }
        }
    )
}

// 格式化时间显示
@SuppressLint("DefaultLocale")
fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
fun LoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                Color.Black.copy(alpha = 0.7f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(40.dp)
                .rotate(rotation)
        ) {
            val strokeWidth = 4.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2

            // 绘制背景圆环
            drawCircle(
                color = Color.White.copy(alpha = 0.3f),
                radius = radius,
                style = Stroke(width = strokeWidth)
            )

            // 绘制加载弧线
            drawArc(
                color = Color.White,
                startAngle = 0f,
                sweepAngle = 120f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                ),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(
                    (size.width - radius * 2) / 2,
                    (size.height - radius * 2) / 2
                )
            )
        }
    }
}
