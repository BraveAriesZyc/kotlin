package com.zyc.core.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
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
import com.zyc.core.video.controls.VideoControls
import com.zyc.core.video.player.VideoPlayerController
import com.zyc.core.video.player.rememberVideoPlayerController
import kotlinx.coroutines.delay

/**
 * 完整的视频播放器组件（带控制栏）
 * @param videoUrl 视频URL
 * @param modifier 修饰符
 * @param autoPlay 是否自动播放
 * @param showSystemControls 是否显示系统控制器
 * @param showCustomControls 是否显示自定义控制器
 * @param aspectRatio 宽高比，null表示自适应
 * @param cornerRadius 圆角半径
 * @param onPlayerReady 播放器准备就绪回调
 * @param onPlaybackStateChanged 播放状态变化回调
 */
@Composable
fun VideoPlayerCompose(
    videoUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    showSystemControls: Boolean = false,
    showCustomControls: Boolean = true,
    aspectRatio: Float? = 16f / 9f,
    cornerRadius: Float = 8f,
    onPlayerReady: ((ExoPlayer) -> Unit)? = null,
    onPlaybackStateChanged: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current
    val controller = rememberVideoPlayerController()
    
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
    
    // 将播放器绑定到控制器
    LaunchedEffect(exoPlayer) {
        controller.attachPlayer(exoPlayer)
        
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                onPlaybackStateChanged?.invoke(playbackState)
            }
        }
        exoPlayer.addListener(listener)
        
        // 定期更新播放位置
        while (true) {
            controller.updatePosition()
            delay(1000) // 每秒更新一次
        }
    }
    
    // 在组件销毁时释放资源
    DisposableEffect(exoPlayer) {
        onDispose {
            controller.detachPlayer()
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
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(Color.Black)
    ) {
        // 视频播放器视图
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = showSystemControls
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // 自定义控制栏
        if (showCustomControls) {
            VideoControls(
                controller = controller,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * 简化版视频播放器（仅播放功能）
 * @param videoUrl 视频URL
 * @param modifier 修饰符
 * @param autoPlay 是否自动播放
 */
@Composable
fun SimpleVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = false
) {
    VideoPlayerCompose(
        videoUrl = videoUrl,
        modifier = modifier,
        autoPlay = autoPlay,
        showSystemControls = true,
        showCustomControls = false
    )
}

/**
 * 全屏视频播放器
 * @param videoUrl 视频URL
 * @param modifier 修饰符
 * @param autoPlay 是否自动播放
 */
@Composable
fun FullscreenVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true
) {
    VideoPlayerCompose(
        videoUrl = videoUrl,
        modifier = modifier.fillMaxSize(),
        autoPlay = autoPlay,
        showSystemControls = false,
        showCustomControls = true,
        aspectRatio = null,
        cornerRadius = 0f
    )
}