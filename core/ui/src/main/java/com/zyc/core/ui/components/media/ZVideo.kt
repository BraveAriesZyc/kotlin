package com.zyc.core.ui.components.media

import Media3PlayerManager
import VideoInfoOverlay
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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

// 视频数据模型
data class VideoItem(
    val id: Int,
    val uri: Uri, // 视频地址（本地或网络）
    val title: String,
    val description: String
)

@OptIn(UnstableApi::class)
@Composable
fun ZVideo(
    videos: List<VideoItem>
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var currentPlayingIndex by remember { mutableStateOf(0) }
    val playerManagers = remember { mutableMapOf<Int, Media3PlayerManager>() }

    // 监听列表滚动，暂停/播放视频
    LaunchedEffect(remember { derivedStateOf { listState.firstVisibleItemIndex } }) {
        val newIndex = listState.firstVisibleItemIndex
        if (newIndex != currentPlayingIndex) {
            // 暂停上一个视频
            playerManagers[currentPlayingIndex]?.pause()
            // 播放新视频
            currentPlayingIndex = newIndex
            playerManagers[newIndex]?.play()
        }
    }

    // 释放所有播放器资源
    DisposableEffect(Unit) {
        onDispose {
            playerManagers.values.forEach { it.release() }
            playerManagers.clear()
        }
    }

    // 返回键处理
    BackHandler {
        playerManagers[currentPlayingIndex]?.pause()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.scrollable(listState, Orientation.Vertical)
        ) {
            itemsIndexed(videos) { index, video ->
                // 每个视频项占满一屏
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(Modifier)
                ) {
                    // 初始化播放器
                    if (!playerManagers.containsKey(index)) {
                        playerManagers[index] = Media3PlayerManager(context).apply {
                            setMediaSource(video.uri.toString())
                            // 预加载但不自动播放
                            pause()
                        }
                    }

                    val playerManager = playerManagers[index]!!

                    // 视频播放器视图
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                player = playerManager.getPlayer()
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                                useController = false // 隐藏默认控制器
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // 视频信息叠加层
                    VideoInfoOverlay(
                        title = video.title,
                        description = video.description,
                        isPlaying = playerManager.getPlayer().playWhenReady,
                        onPlayPauseClick = {
                            if (playerManager.getPlayer().playWhenReady) {
                                playerManager.pause()
                            } else {
                                playerManager.play()
                            }
                        }
                    )
                }
            }
        }
    }
}
