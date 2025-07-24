package com.zyc.clover.components.medium

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.AspectRatioFrameLayout
import com.zyc.clover.utils.manager.VideoPreloadManager
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    externalPlayer: ExoPlayer? = null, // 外部传入的播放器实例
    preloadManager: VideoPreloadManager? = null, // 视频预加载管理器
    showCustomControls: Boolean = true, // 是否显示自定义控制器
    onPlayerReady: ((String) -> Unit)? = null, // 播放器就绪回调
    onPlayerBuffering: ((String, Int) -> Unit)? = null, // 缓冲进度回调
    onPlayerError: ((String, String) -> Unit)? = null, // 播放错误回调
    onPlayerEnded: ((String) -> Unit)? = null // 播放结束回调
) {
    val context = LocalContext.current

    // 状态变量
    var isPlayerReady by remember { mutableStateOf(false) }
    var bufferPercent by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 自定义控制器状态
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var showControls by remember { mutableStateOf(true) }
    var volume by remember { mutableFloatStateOf(1f) }
    var isMuted by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) } // 是否正在拖拽进度条
    var bufferPercentage by remember { mutableIntStateOf(0) }

    // 1. 优先使用外部传入的播放器，否则创建新的
    val player = externalPlayer ?: remember {
        ExoPlayer.Builder(context).build()
    }

    // 2. 设置状态回调监听器
    LaunchedEffect(preloadManager, videoUrl) {
        preloadManager?.setStateCallback(object : VideoPreloadManager.PlayerStateCallback {
            override fun onPlayerReady(videoUrl: String) {
                isPlayerReady = true
                onPlayerReady?.invoke(videoUrl)
            }

            override fun onPlayerBuffering(videoUrl: String, receivedBufferPercent: Int) {
                bufferPercent = receivedBufferPercent
                onPlayerBuffering?.invoke(videoUrl, receivedBufferPercent)
            }

            override fun onPlayerError(videoUrl: String, error: String) {
                errorMessage = error
                isPlayerReady = false
                onPlayerError?.invoke(videoUrl, error)
            }

            override fun onPlayerEnded(videoUrl: String) {
                isPlayerReady = false
                onPlayerEnded?.invoke(videoUrl)
            }
        })
    }

    // 3. 只有在没有外部播放器时才处理视频地址变化
    if (externalPlayer == null) {
        LaunchedEffect(videoUrl) {
            if (videoUrl.isNotEmpty()) {
                try {
                    val mediaItem = MediaItem.fromUri(videoUrl)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    player.playWhenReady = false // 默认不自动播放，由管理器控制
                } catch (e: Exception) {
                    errorMessage = "加载视频失败: ${e.message}"
                    android.util.Log.e("视频播放器", "加载视频失败: $videoUrl", e)
                }
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
    LaunchedEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        isPlayerReady = true
                        duration = player.duration
                    }

                    Player.STATE_ENDED -> {
                        isPlaying = false
                        onPlayerEnded?.invoke(videoUrl)
                    }
                }
            }
        }
        player.addListener(listener)
    }

    // 定期更新缓冲进度
    LaunchedEffect(isPlayerReady) {
        while (isPlayerReady) {
            bufferPercentage = player.bufferedPercentage
            delay(500) // 每500ms更新一次缓冲进度
        }
    }

    // 定期更新播放进度
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = player.currentPosition
            delay(100)
        }
    }

    // 自动隐藏控制器 - 拖拽时不隐藏
    LaunchedEffect(showControls, isDragging) {
        if (showControls && showCustomControls && !isDragging) {
            delay(3000)
            showControls = false
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = false // 禁用默认控制栏
                    // 设置视频缩放模式，避免画面变形
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    // 启用硬件加速和优化设置
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                    // 优化性能设置
                    setShutterBackgroundColor(android.graphics.Color.BLACK)
                    setKeepContentOnPlayerReset(true)
                }
            },
            update = { playerView ->
                // 确保播放器绑定正确
                if (playerView.player != player) {
                    playerView.player = player
                }

                // 根据状态更新UI
                playerView.setShowBuffering(
                    if (isPlayerReady) PlayerView.SHOW_BUFFERING_WHEN_PLAYING
                    else PlayerView.SHOW_BUFFERING_ALWAYS
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable {
                    if (showCustomControls) {
                        showControls = !showControls
                    }
                }
        )

        // 控制器覆盖层 - 包含播放/暂停按钮和进度条
        if (showCustomControls && showControls) {
            CustomVideoControls(
                isPlaying = isPlaying,
                currentPosition = currentPosition,
                duration = duration,
                volume = volume,
                isMuted = isMuted,
                bufferPercentage = bufferPercentage,
                isDragging = isDragging,
                onPlayPause = {
                    if (isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                },
                onSeek = { position ->
                    player.seekTo(position)
                    currentPosition = position
                },
                onProgressChange = { progress ->
                    val newPosition = (progress * duration).toLong()
                    player.seekTo(newPosition)
                    currentPosition = newPosition
                },
                onDragStart = {
                    isDragging = true
                },
                onDragEnd = {
                    isDragging = false
                },
                onVolumeChange = { newVolume ->
                    volume = newVolume
                    player.volume = newVolume
                },
                onMuteToggle = {
                    isMuted = !isMuted
                    player.volume = if (isMuted) 0f else volume
                },
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
    bufferPercentage: Int,
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
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.2f)),
        content = {
            // 中央播放/暂停按钮
            IconButton(
                onClick = onPlayPause,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.7f)),
                content = {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.ArrowDropDown else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "暂停" else "播放",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                content = {
                    CustomProgressBar(
                        progress = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f,
                        bufferProgress = bufferPercentage / 100f,
                        onProgressChange = onProgressChange,
                        onDragStart = onDragStart,
                        onDragEnd = onDragEnd,
                        currentTime = formatTime(currentPosition),
                        totalTime = formatTime(duration),
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    )

                }
            )

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
