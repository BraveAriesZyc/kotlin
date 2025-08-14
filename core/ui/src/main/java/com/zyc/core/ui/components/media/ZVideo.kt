package com.zyc.core.ui.components.media

import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

private const val TAG = "ZVideo"

@OptIn(UnstableApi::class)
@Composable
fun ZVideo(
    videoUrl: String,
    autoPlay: Boolean = true,
    loopPlay: Boolean = true
) {
    val context = LocalContext.current
    var player by remember { mutableStateOf<ExoPlayer?>(null) }

    // 初始化播放器
    LaunchedEffect(videoUrl) {
        player?.release()

        try {
            val uri = Uri.parse(videoUrl)
            if (uri.scheme.isNullOrEmpty()) {
                Log.e(TAG, "无效的视频URL: $videoUrl")
                return@LaunchedEffect
            }

            player = ExoPlayer.Builder(context).build().apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            Player.STATE_READY -> {
                                Log.d(TAG, "视频准备就绪")
                                if (autoPlay) play()
                            }
                            Player.STATE_ENDED -> {
                                Log.d(TAG, "视频播放结束")
                                if (loopPlay) seekTo(0) // 循环播放
                            }
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        super.onPlayerError(error)
                        Log.e(TAG, "播放错误: ${error.message}", error)
                    }
                })

                setMediaItem(MediaItem.fromUri(uri))
                prepare()
            }
        } catch (e: Exception) {
            Log.e(TAG, "初始化播放器失败", e)
        }
    }

    // 释放资源
    DisposableEffect(Unit) {
        onDispose {
            player?.release()
            player = null
            Log.d(TAG, "播放器已释放")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        player?.let { exoPlayer ->
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        this.player = exoPlayer
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        controllerShowTimeoutMs = 2000 // 2秒后自动隐藏
                        useController = true
                        setShowNextButton(false)
                        setShowPreviousButton(false)
                        setShowSubtitleButton(false)
                        setShowVrButton(false)
                    }
                }
            )
        }
    }
}
