package com.zyc.core.ui.components.video

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
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.common.util.Util
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import com.zyc.core.ui.components.progress.CustomProgressBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
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

    // 播放器状态
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var isBuffering by remember { mutableStateOf(false) }
    var isVideoReady by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
    var retryCount by remember { mutableIntStateOf(0) }
    
    // UI 控制状态
    var showControls by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }
    var volume by remember { mutableFloatStateOf(1f) }
    var isMuted by remember { mutableStateOf(false) }

    // 计算进度和格式化时间
    val progress by remember {
        derivedStateOf {
            if (duration > 0) currentPosition.toFloat() / duration else 0f
        }
    }

    val currentTimeFormatted by remember {
        derivedStateOf { formatTime(currentPosition) }
    }

    val totalTimeFormatted by remember {
        derivedStateOf { formatTime(duration) }
    }

    // 播放器实例管理 - 重构：优先使用外部播放器，统一管理
    val player = remember(videoUrl) {
        externalPlayer ?: createInternalPlayer(context)
    }

    // 播放器初始化 - 重构：分离外部和内部播放器的初始化逻辑
    LaunchedEffect(videoUrl, externalPlayer) {
        android.util.Log.d("VideoPlayer", "初始化播放器 - videoUrl: $videoUrl, hasExternalPlayer: ${externalPlayer != null}")
        
        if (externalPlayer != null) {
            // 外部播放器：直接同步状态，无需重新初始化
            syncExternalPlayerState(externalPlayer) { playing, position, dur, buffering, ready, error ->
                isPlaying = playing
                currentPosition = position
                duration = dur
                isBuffering = buffering
                isVideoReady = ready
                hasError = error
                retryCount = 0
            }
            android.util.Log.d("VideoPlayer", "外部播放器状态同步完成")
        } else {
            // 内部播放器：重置状态并初始化
            resetPlayerState { playing, position, dur, buffering, ready, error, retry ->
                isPlaying = playing
                currentPosition = position
                duration = dur
                isBuffering = buffering
                isVideoReady = ready
                hasError = error
                retryCount = retry
            }
            
            // 配置并准备内部播放器
            initializeInternalPlayer(player, videoUrl, repeatMode, volume)
            android.util.Log.d("VideoPlayer", "内部播放器初始化完成")
        }
    }

    // 设置重复播放模式
    LaunchedEffect(repeatMode) {
        if (externalPlayer == null) {
            player.repeatMode = if (repeatMode) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        }
    }

    // 监控播放器状态，自动重试加载失败的视频
    LaunchedEffect(player, videoUrl) {
        if (externalPlayer == null && videoUrl.isNotEmpty()) {
            while (true) {
                delay(3000) // 每3秒检查一次，给更多时间加载
                
                // 如果播放器处于空闲状态且没有准备好，尝试重新加载
                if (player.playbackState == Player.STATE_IDLE && !isVideoReady && !hasError && retryCount < 3) {
                    android.util.Log.d("VideoPlayer", "检测到播放器空闲状态，尝试重新加载 (${retryCount + 1}/3)")
                    retryCount++
                    break // 触发重新加载
                }
                
                // 如果视频已经准备好或者有错误，停止监控
                if (isVideoReady || hasError || retryCount >= 3) {
                    break
                }
            }
        }
    }

    // 资源释放
    if (externalPlayer == null) {
        DisposableEffect(Unit) {
            onDispose {
                try {
                    player.release()
                } catch (e: Exception) {
                    android.util.Log.e("VideoPlayer", "释放播放器失败", e)
                }
            }
        }
    }

    // 播放器状态监听器 - 重构：仅用于内部播放器
    val playerListener = remember {
        createPlayerListener(
            onStateUpdate = { playing, position, dur, buffering, ready, error, retry ->
                if (playing != isPlaying) isPlaying = playing
                if (position >= 0) currentPosition = position
                if (dur >= 0) duration = dur
                isBuffering = buffering
                isVideoReady = ready
                hasError = error
                retryCount = retry
            },
            player = player,
            repeatMode = repeatMode,
            videoUrl = videoUrl
        )
    }

    // 播放器监听器管理 - 重构：仅对内部播放器添加监听器
    LaunchedEffect(player, externalPlayer) {
        if (externalPlayer == null) {
            player.addListener(playerListener)
            android.util.Log.d("VideoPlayer", "添加内部播放器监听器")
        }
    }
    
    // 清理监听器
    DisposableEffect(player, externalPlayer) {
        onDispose {
            if (externalPlayer == null) {
                player.removeListener(playerListener)
                android.util.Log.d("VideoPlayer", "移除内部播放器监听器")
            }
        }
    }

    // 进度更新 - 重构：统一处理内部和外部播放器
    LaunchedEffect(player, externalPlayer, isVideoReady) {
        if (isVideoReady) {
            while (isActive) {
                if (!isDragging) {
                    val activePlayer = externalPlayer ?: player
                    currentPosition = activePlayer.currentPosition
                    if (activePlayer.duration > 0) {
                        duration = activePlayer.duration
                    }
                    // 同步播放状态
                    isPlaying = activePlayer.isPlaying
                }
                delay(100)
            }
        }
    }

    // 控制器自动隐藏
    LaunchedEffect(showControls, isDragging, isPlaying) {
        if (showControls && !isDragging && isPlaying) {
            delay(3000)
            showControls = false
        }
    }

    Box(modifier = modifier) {
        // ExoPlayer 视频播放器
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    this.player = player
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                }
            },
            update = { playerView ->
                if (playerView.player != player) {
                    playerView.player = player
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    showControls = true
                    handlePlayPauseClick(
                        externalPlayer = externalPlayer,
                        internalPlayer = player,
                        isVideoReady = isVideoReady,
                        hasError = hasError,
                        isPlaying = isPlaying
                    )
                }
        )

        // 缓冲指示器
        if (isBuffering) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        // 错误状态显示
        if (hasError && retryCount >= 3) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "⚠",
                        color = Color.Red,
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "视频加载失败",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            retryCount = 0
                            hasError = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.8f)
                        )
                    ) {
                        Text("重试", color = Color.White)
                    }
                }
            }
        }

        // 控制器
        if (showControls && !hasError) {
            CustomVideoControls(
                isPlaying = isPlaying,
                progress = progress,
                currentTimeFormatted = currentTimeFormatted,
                totalTimeFormatted = totalTimeFormatted,
                volume = volume,
                isMuted = isMuted,
                isDragging = isDragging,
                onPlayPause = {
                    handlePlayPauseClick(
                        externalPlayer = externalPlayer,
                        internalPlayer = player,
                        isVideoReady = isVideoReady,
                        hasError = hasError,
                        isPlaying = isPlaying
                    )
                },
                onSeek = { position ->
                    player.seekTo(position)
                    currentPosition = position
                },
                onProgressChange = { newProgress ->
                    val newPosition = (newProgress * duration).toLong()
                    player.seekTo(newPosition)
                    currentPosition = newPosition
                },
                onDragStart = { isDragging = true },
                onDragEnd = { isDragging = false },
                onVolumeChange = { newVolume ->
                    volume = newVolume
                    player.volume = newVolume
                },
                onMuteToggle = {
                    isMuted = !isMuted
                    player.volume = if (isMuted) 0f else volume
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// 辅助函数：创建播放器监听器
private fun createPlayerListener(
    onStateUpdate: (Boolean, Long, Long, Boolean, Boolean, Boolean, Int) -> Unit,
    player: ExoPlayer,
    repeatMode: Boolean,
    videoUrl: String
): Player.Listener {
    var currentRetryCount = 0
    
    return object : Player.Listener {
        override fun onIsPlayingChanged(playing: Boolean) {
            android.util.Log.d("VideoPlayer", "播放状态变化: $playing")
            // 保持当前的缓冲状态，只更新播放状态
            val isBuffering = player.playbackState == Player.STATE_BUFFERING
            val isReady = player.playbackState == Player.STATE_READY
            onStateUpdate(playing, -1L, -1L, isBuffering, isReady, player.playerError != null, currentRetryCount)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            android.util.Log.d("VideoPlayer", "播放器状态变化: $playbackState")
            when (playbackState) {
                Player.STATE_READY -> {
                    currentRetryCount = 0
                    val duration = if (player.duration > 0) player.duration else -1L
                    android.util.Log.d("VideoPlayer", "视频准备完成，时长: $duration")
                    onStateUpdate(player.isPlaying, player.currentPosition, duration, false, true, false, currentRetryCount)
                }
                Player.STATE_BUFFERING -> {
                    android.util.Log.d("VideoPlayer", "缓冲中...")
                    onStateUpdate(player.isPlaying, -1L, -1L, true, false, false, currentRetryCount)
                }
                Player.STATE_ENDED -> {
                    android.util.Log.d("VideoPlayer", "播放结束")
                    if (!repeatMode) {
                        onStateUpdate(false, -1L, -1L, false, true, false, currentRetryCount)
                    }
                }
                Player.STATE_IDLE -> {
                    android.util.Log.w("VideoPlayer", "播放器进入空闲状态")
                    onStateUpdate(false, -1L, -1L, false, false, false, currentRetryCount)
                }
            }
        }

        override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
            android.util.Log.e("VideoPlayer", "播放器错误: ${error.message}")
            
            when (error.errorCode) {
                androidx.media3.common.PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                androidx.media3.common.PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
                    if (currentRetryCount < 3) {
                        currentRetryCount++
                        android.util.Log.w("VideoPlayer", "网络错误，重试 ($currentRetryCount/3)")
                        onStateUpdate(false, -1L, -1L, false, false, true, currentRetryCount)
                    } else {
                        android.util.Log.e("VideoPlayer", "网络错误重试次数已达上限")
                        onStateUpdate(false, -1L, -1L, false, false, true, currentRetryCount)
                    }
                }
                else -> {
                    android.util.Log.e("VideoPlayer", "播放器错误，停止重试")
                    onStateUpdate(false, -1L, -1L, false, false, true, currentRetryCount)
                }
            }
        }
    }
}

// 辅助函数：同步外部播放器状态
private suspend fun syncExternalPlayerState(
    externalPlayer: ExoPlayer,
    onStateUpdate: (Boolean, Long, Long, Boolean, Boolean, Boolean) -> Unit
) {
    val isPlaying = externalPlayer.isPlaying
    val currentPosition = externalPlayer.currentPosition
    val duration = externalPlayer.duration
    val isBuffering = externalPlayer.playbackState == Player.STATE_BUFFERING
    val isReady = externalPlayer.playbackState == Player.STATE_READY
    val hasError = false // 外部播放器错误由外部管理
    
    onStateUpdate(isPlaying, currentPosition, duration, isBuffering, isReady, hasError)
}

// 辅助函数：重置播放器状态
private fun resetPlayerState(
    onStateUpdate: (Boolean, Long, Long, Boolean, Boolean, Boolean, Int) -> Unit
) {
    onStateUpdate(false, 0L, 0L, false, false, false, 0)
}

// 辅助函数：创建内部播放器
private fun createInternalPlayer(context: android.content.Context): ExoPlayer {
    return ExoPlayer.Builder(context)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                .build(),
            true
        )
        .build()
}

// 辅助函数：初始化内部播放器
private fun initializeInternalPlayer(
    player: ExoPlayer,
    videoUrl: String,
    repeatMode: Boolean,
    volume: Float
) {
    try {
        val mediaItem = MediaItem.fromUri(videoUrl)
        player.setMediaItem(mediaItem)
        player.repeatMode = if (repeatMode) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        player.volume = volume
        player.playWhenReady = false // 默认不自动播放
        player.prepare()
        android.util.Log.d("VideoPlayer", "内部播放器配置完成")
    } catch (e: Exception) {
        android.util.Log.e("VideoPlayer", "初始化内部播放器失败", e)
    }
}

// 辅助函数：统一的播放暂停控制
private fun handlePlayPauseClick(
    externalPlayer: ExoPlayer?,
    internalPlayer: ExoPlayer,
    isVideoReady: Boolean,
    hasError: Boolean,
    isPlaying: Boolean
) {
    val activePlayer = externalPlayer ?: internalPlayer
    val playerType = if (externalPlayer != null) "外部" else "内部"
    
    android.util.Log.d("VideoPlayer", "${playerType}播放器控制 - isVideoReady: $isVideoReady, hasError: $hasError, isPlaying: $isPlaying")
    
    when {
        hasError -> {
            android.util.Log.w("VideoPlayer", "播放器处于错误状态，忽略操作")
        }
        isVideoReady -> {
            if (isPlaying) {
                activePlayer.pause()
                android.util.Log.d("VideoPlayer", "${playerType}播放器：暂停播放")
            } else {
                activePlayer.play()
                activePlayer.playWhenReady = true
                android.util.Log.d("VideoPlayer", "${playerType}播放器：开始播放")
            }
        }
        else -> {
            // 视频还没准备好，设置为准备播放状态
            activePlayer.playWhenReady = true
            android.util.Log.d("VideoPlayer", "${playerType}播放器：视频未准备好，设置playWhenReady=true")
        }
    }
}

@Composable
fun CustomVideoControls(
    isPlaying: Boolean,
    progress: Float,
    currentTimeFormatted: String,
    totalTimeFormatted: String,
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
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // 中央播放区域
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable(
                    onClick = onPlayPause,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 播放/暂停按钮
            if (!isPlaying) {
                Text(
                    text = "▶", // 播放图标
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 80.sp
                )
            }
        }

        // 底部控制栏
        if (!isPlaying) {
            CustomProgressBar(
                progress = progress,
                onProgressChange = onProgressChange,
                onDragStart = onDragStart,
                onDragEnd = onDragEnd,
                currentTime = currentTimeFormatted,
                totalTime = totalTimeFormatted,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
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
                Color.Black.copy(alpha = 0.6f),
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
            val center = Offset(size.width / 2, size.height / 2)

            drawCircle(
                color = Color.White.copy(alpha = 0.3f),
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

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
                    center.x - radius,
                    center.y - radius
                )
            )
        }
    }
}