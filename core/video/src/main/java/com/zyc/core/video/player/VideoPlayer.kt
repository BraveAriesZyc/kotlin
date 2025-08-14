package com.zyc.core.video.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/**
 * 视频播放器组件
 * @param videoUrl 视频URL
 * @param modifier 修饰符
 * @param autoPlay 是否自动播放
 * @param showControls 是否显示控制器
 * @param aspectRatio 宽高比，null表示自适应
 * @param onPlayerReady 播放器准备就绪回调
 * @param onPlaybackStateChanged 播放状态变化回调
 */
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    showControls: Boolean = true,
    aspectRatio: Float? = 16f / 9f,
    onPlayerReady: ((ExoPlayer) -> Unit)? = null,
    onPlaybackStateChanged: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current
    
    // 创建ExoPlayer实例
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(videoUrl))
                playWhenReady = autoPlay
                prepare()
                onPlayerReady?.invoke(this)
            }
    }
    
    // 监听播放状态变化
    LaunchedEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                onPlaybackStateChanged?.invoke(playbackState)
            }
        }
        exoPlayer.addListener(listener)
    }
    
    // 在组件销毁时释放播放器
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .let { mod ->
                if (aspectRatio != null) {
                    mod.aspectRatio(aspectRatio)
                } else {
                    mod.wrapContentHeight()
                }
            }
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = showControls
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * 简化版视频播放器
 * @param videoUrl 视频URL
 * @param modifier 修饰符
 */
@Composable
fun SimpleVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    VideoPlayer(
        videoUrl = videoUrl,
        modifier = modifier,
        autoPlay = false,
        showControls = true,
        aspectRatio = 16f / 9f
    )
}