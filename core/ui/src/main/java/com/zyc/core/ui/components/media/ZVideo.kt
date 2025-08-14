package com.zyc.core.ui.components.media

import android.view.SurfaceView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ZVideo() {
    val context = androidx.compose.ui.platform.LocalContext.current
    var player by remember { mutableStateOf<IjkMediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(autoPlay) }

    // 初始化播放器
    LaunchedEffect(Unit) {
        Ijk.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")

        player = IjkMediaPlayer().apply {
            // 配置播放器参数
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", if (autoPlay) 1 else 0)
            setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "mediacodec", 1) // 开启硬件解码
            setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "mediacodec-auto-rotate", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 10000000) // 10秒超时
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 视频渲染视图
        AndroidView(
            factory = { ctx ->
                SurfaceView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    holder.addCallback(object : SurfaceHolder.Callback {
                        override fun surfaceCreated(holder: SurfaceHolder) {
                            player?.setDisplay(holder)
                            setupPlayer(ctx, player, videoUrl)
                        }

                        override fun surfaceChanged(
                            holder: SurfaceHolder,
                            format: Int,
                            width: Int,
                            height: Int
                        ) {}

                        override fun surfaceDestroyed(holder: SurfaceHolder) {
                            player?.setDisplay(null)
                        }
                    })
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 播放/暂停按钮
//        PlayerControlButton(
//            isPlaying = isPlaying,
//            modifier = Modifier.align(Alignment.Center)
//        ) {
//            player?.let {
//                if (it.isPlaying) {
//                    it.pause()
//                    isPlaying = false
//                } else {
//                    it.start()
//                    isPlaying = true
//                }
//            }
//        }
    }

}