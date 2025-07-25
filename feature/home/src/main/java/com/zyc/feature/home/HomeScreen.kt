package com.zyc.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import com.zyc.core.ui.components.medium.VideoPlayer
import com.zyc.core.ui.utils.manager.VideoPreloadManager
import com.zyc.core.model.entity.WorkModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.awaitCancellation

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val homeViewModel = viewModel<HomeViewModel>()
    val topicList by homeViewModel.topicList.collectAsState()
    val pagerState = rememberPagerState(pageCount = { topicList.size })
    val context = LocalContext.current

    // 创建视频预加载管理器
    val preloadManager = remember { VideoPreloadManager(context) }

    // 优化预加载策略 - 更保守的预加载和清理策略
    LaunchedEffect(pagerState.currentPage, topicList) {
        val currentPage = pagerState.currentPage
        val currentVideoUrl = topicList.getOrNull(currentPage)?.workList?.firstOrNull()

        // 设置当前播放视频
        if (!currentVideoUrl.isNullOrEmpty()) {
            preloadManager.setCurrentPlaying(currentVideoUrl)
        }

        // 延迟预加载下一页视频，避免频繁切换时的资源浪费
        delay(500)
        val nextPage = currentPage + 1
        if (nextPage < topicList.size) {
            topicList.getOrNull(nextPage)?.workList?.firstOrNull()?.let { videoUrl ->
                if (videoUrl.isNotEmpty() && !preloadManager.hasPlayer(videoUrl)) {
                    preloadManager.preloadVideo(videoUrl)
                }
            }
        }

        // 更积极的清理策略 - 清理前一页和更远的视频
        val cleanupRange = currentPage - 1
        if (cleanupRange >= 0) {
            topicList.getOrNull(cleanupRange)?.workList?.firstOrNull()?.let { videoUrl ->
                if (videoUrl.isNotEmpty() && videoUrl != currentVideoUrl) {
                    preloadManager.forceReleasePlayer(videoUrl)
                }
            }
        }

        // 清理更远的视频缓存
        for (i in 0 until currentPage - 1) {
            topicList.getOrNull(i)?.workList?.firstOrNull()?.let { videoUrl ->
                if (videoUrl.isNotEmpty()) {
                    preloadManager.forceReleasePlayer(videoUrl)
                }
            }
        }

        // 清理后面过远的视频缓存
        for (i in currentPage + 2 until topicList.size) {
            topicList.getOrNull(i)?.workList?.firstOrNull()?.let { videoUrl ->
                if (videoUrl.isNotEmpty()) {
                    preloadManager.forceReleasePlayer(videoUrl)
                }
            }
        }
    }

    // 内存监控 - 优化监控策略
    LaunchedEffect(Unit) {
        while (true) {
            delay(20000) // 每20秒检查一次，减少检查频率
            val runtime = Runtime.getRuntime()
            val usedMemory = runtime.totalMemory() - runtime.freeMemory()
            val maxMemory = runtime.maxMemory()
            val memoryUsagePercent = (usedMemory * 100 / maxMemory).toInt()

            android.util.Log.d(
                "HomeScreen",
                "内存使用情况: ${memoryUsagePercent}% (${usedMemory / 1024 / 1024}MB / ${maxMemory / 1024 / 1024}MB)"
            )

            when {
                memoryUsagePercent > 75 -> {
                    android.util.Log.w("HomeScreen", "内存使用率过高: ${memoryUsagePercent}%, 执行强制清理")
                    preloadManager.onMemoryPressure()
                    System.gc()
                }

                memoryUsagePercent > 65 -> {
                    android.util.Log.w("HomeScreen", "内存使用率较高: ${memoryUsagePercent}%, 执行部分清理")
                    // 只清理非当前播放的视频
                    val currentVideoUrl = topicList.getOrNull(pagerState.currentPage)?.workList?.firstOrNull()
                    topicList.forEachIndexed { index, workModel ->
                        if (index != pagerState.currentPage && index != pagerState.currentPage + 1) {
                            workModel.workList.firstOrNull()?.let { videoUrl ->
                                if (videoUrl.isNotEmpty() && videoUrl != currentVideoUrl) {
                                    preloadManager.forceReleasePlayer(videoUrl)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 组件销毁时释放资源
    DisposableEffect(Unit) {
        onDispose { preloadManager.destroy() }
    }

    Scaffold { paddingValues ->
        VerticalPager(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize(),
            state = pagerState,
            pageContent = { page ->
                TopicItem(
                    item = topicList[page],
                    preloadManager = preloadManager,
                    isCurrentPage = page == pagerState.currentPage
                )
            }
        )
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun TopicItem(
    item: WorkModel,
    preloadManager: VideoPreloadManager,
    isCurrentPage: Boolean
) {
    val videoUrl = item.workList.firstOrNull() ?: ""
    var playerError by remember { mutableStateOf<String?>(null) }
    var isPlayerReady by remember { mutableStateOf(false) }

    // 获取预加载的播放器实例 - 改进错误处理
    val exoPlayer = remember(videoUrl) {
        if (videoUrl.isNotEmpty()) {
            try {
                val player = preloadManager.getOrCreatePlayer(videoUrl)
                isPlayerReady = player.playbackState == Player.STATE_READY
                playerError = null
                player
            } catch (e: Exception) {
                android.util.Log.e("TopicItem", "创建播放器失败: $videoUrl", e)
                playerError = "播放器创建失败: ${e.message}"
                isPlayerReady = false
                null
            }
        } else {
            playerError = null
            isPlayerReady = false
            null
        }
    }

    // 监听播放器状态变化
    LaunchedEffect(exoPlayer) {
        exoPlayer?.let { player ->
            val listener = object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    // 更新播放器准备状态，考虑缓冲状态
                    isPlayerReady = playbackState == Player.STATE_READY && !player.isLoading
                    android.util.Log.d(
                        "TopicItem",
                        "播放器状态变化: $playbackState, isLoading: ${player.isLoading}, isPlayerReady: $isPlayerReady"
                    )

                    if (playbackState == Player.STATE_IDLE) {
                        android.util.Log.w("TopicItem", "播放器进入空闲状态: $videoUrl")
                        isPlayerReady = false
                    }
                }

                override fun onIsLoadingChanged(isLoading: Boolean) {
                    // 当加载状态变化时，重新评估播放器准备状态
                    isPlayerReady = player.playbackState == Player.STATE_READY && !isLoading
                    android.util.Log.d(
                        "TopicItem",
                        "加载状态变化: isLoading=$isLoading, playbackState=${player.playbackState}, isPlayerReady=$isPlayerReady"
                    )
                }

                override fun onPlayerError(error: PlaybackException) {
                    android.util.Log.e("TopicItem", "播放器错误: $videoUrl", error)
                    playerError = "播放错误: ${error.message}"
                    isPlayerReady = false
                }
            }
            player.addListener(listener)
            // 清理监听器
            kotlinx.coroutines.awaitCancellation()
        }
    }

    // 控制播放状态 - 改进状态管理
    LaunchedEffect(isCurrentPage, videoUrl, isPlayerReady) {
        if (videoUrl.isNotEmpty() && exoPlayer != null) {
            try {
                if (isCurrentPage) {
                    // 确保播放器准备就绪后再播放
                    if (isPlayerReady) {
                        preloadManager.playVideo(videoUrl)
                    } else {
                        // 等待播放器准备就绪
                        delay(500)
                        if (preloadManager.getPlayerState(videoUrl) == Player.STATE_READY) {
                            preloadManager.playVideo(videoUrl)
                        }
                    }
                } else {
                    preloadManager.pauseVideo(videoUrl)
                }
            } catch (e: Exception) {
                android.util.Log.e("TopicItem", "控制播放状态失败: $videoUrl", e)
                playerError = "播放控制失败: ${e.message}"
            }
        }
    }

    // 页面不可见时暂停播放
    DisposableEffect(isCurrentPage) {
        onDispose {
            if (videoUrl.isNotEmpty()) {
                preloadManager.pauseVideo(videoUrl)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            playerError != null -> {
                // 显示错误状态
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "视频加载失败\n${playerError}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            videoUrl.isNotEmpty() && exoPlayer != null -> {
                VideoPlayer(
                    videoUrl = videoUrl,
                    externalPlayer = exoPlayer,
                    modifier = Modifier.fillMaxSize()
                )

                // 显示加载状态 - 只在视频真正未准备好时显示
                if (!isPlayerReady && playerError == null && exoPlayer.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            videoUrl.isEmpty() -> {
                // 无视频内容时的占位符
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无视频内容",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            else -> {
                // 默认加载状态
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
