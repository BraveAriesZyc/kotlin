package com.zyc.clover.components.medium

import androidx.annotation.OptIn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.AspectRatioFrameLayout
import com.zyc.clover.utils.manager.VideoPreloadManager

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    externalPlayer: ExoPlayer? = null, // 外部传入的播放器实例
    preloadManager: VideoPreloadManager? = null, // 视频预加载管理器
    onPlayerReady: ((String) -> Unit)? = null, // 播放器就绪回调
    onPlayerBuffering: ((String, Int) -> Unit)? = null, // 缓冲进度回调
    onPlayerError: ((String, String) -> Unit)? = null, // 播放错误回调
    onPlayerEnded: ((String) -> Unit)? = null // 播放结束回调
) {
    val context = LocalContext.current
    
    // 状态变量
    var isPlayerReady by remember { mutableStateOf(false) }
    var bufferPercent by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

            override fun onPlayerBuffering(videoUrl: String, bufferPercent: Int) {
                bufferPercent = bufferPercent
                onPlayerBuffering?.invoke(videoUrl, bufferPercent)
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

    // 5. 将Player与PlayerView绑定
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                this.player = player
                useController = true // 显示控制栏
                // 设置视频缩放模式，避免画面变形
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                // 启用硬件加速和优化设置
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                // 设置控制器超时时间
                controllerShowTimeoutMs = 3000
                // 隐藏某些控制按钮以简化界面
                setShowPreviousButton(false)
                setShowNextButton(false)
                setShowFastForwardButton(false)
                setShowRewindButton(false)
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
        modifier = modifier
    )
}
