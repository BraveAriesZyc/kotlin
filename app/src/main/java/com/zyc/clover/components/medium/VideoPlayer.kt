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
import com.zyc.clover.R
import com.zyc.clover.components.drawer.ICON_SIZE
import com.zyc.clover.utils.event.GlobalAntiShake.debounceClick
import com.zyc.clover.utils.manager.VideoPreloadManager
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


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
    var showControls by remember { mutableStateOf(true) }
    var volume by remember { mutableFloatStateOf(1f) }
    var isMuted by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) } // 是否正在拖拽进度条

    // 1. 优先使用外部传入的播放器，否则创建新的
    val player = externalPlayer ?: remember {
        ExoPlayer.Builder(context).build()
    }

    // 设置重复播放模式
    LaunchedEffect(repeatMode) {
        player.repeatMode = if (repeatMode) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
    }



    // 处理视频地址变化
    if (externalPlayer == null) {
        LaunchedEffect(videoUrl) {
            if (videoUrl.isNotEmpty()) {
                val mediaItem = MediaItem.fromUri(videoUrl)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.playWhenReady = false
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
                        duration = player.duration
                    }
                    Player.STATE_ENDED -> {
                        if (!repeatMode) {
                            isPlaying = false
                        }
                    }
                }
            }
        }
        player.addListener(listener)
    }

    // 定期更新播放进度
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = player.currentPosition
            delay(100)
        }
    }

    // 自动隐藏控制器
    LaunchedEffect(showControls, isDragging) {
        if (showControls && !isDragging) {
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
                    setRepeatToggleModes(Player.REPEAT_MODE_ONE)
                    setKeepContentOnPlayerReset(true)
                }
            },
            update = { playerView ->
                if (playerView.player != player) {
                    playerView.player = player
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable {
                    if (isPlaying) {
                        player.pause()
                        showControls = true
                    } else {
                        showControls = !showControls
                    }
                }
        )

        // 控制器覆盖层
        if (showControls) {
            CustomVideoControls(
                isPlaying = isPlaying,
                currentPosition = currentPosition,
                duration = duration,
                volume = volume,
                isMuted = isMuted,
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
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.2f)),
        content = {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            onPlayPause()
                        }
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    // 中央播放按钮 - 只在暂停时显示
                    if (!isPlaying) {
                        Text(
                            text = "\uEDCF", // 播放图标
                            color = Color.White,
                            fontSize = 80.sp,
                            fontFamily = FontFamily(Font(R.font.icons))
                        )
                    }
                }
            )

            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                content = {
                    CustomProgressBar(
                        progress = if (duration > 0) currentPosition.toFloat() / duration else 0f,
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
