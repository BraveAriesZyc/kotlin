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
import com.zyc.core.ui.components.medium.VideoPlayer
import com.zyc.core.ui.utils.manager.VideoPreloadManager
import com.zyc.data.models.WorkModel
import kotlinx.coroutines.delay

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

    // 优化预加载策略 - 减少预加载数量，降低内存压力
    LaunchedEffect(pagerState.currentPage, topicList) {
        val currentPage = pagerState.currentPage
        val currentVideoUrl = topicList.getOrNull(currentPage)?.workList?.firstOrNull()

        preloadManager.setCurrentPlaying(currentVideoUrl)

        // 只预加载下一页视频，减少内存使用
        val nextPage = currentPage + 1
        topicList.getOrNull(nextPage)?.workList?.firstOrNull()?.let { videoUrl ->
            if (videoUrl.isNotEmpty()) {
                preloadManager.preloadVideo(videoUrl)
            }
        }
        
        // 清理过远的视频缓存
        val cleanupRange = currentPage - 2
        if (cleanupRange >= 0) {
            topicList.getOrNull(cleanupRange)?.workList?.firstOrNull()?.let { videoUrl ->
                if (videoUrl.isNotEmpty()) {
                    preloadManager.forceReleasePlayer(videoUrl)
                }
            }
        }
    }

    // 内存监控 - 更积极的清理策略
    LaunchedEffect(Unit) {
        while (true) {
            delay(15000) // 每15秒检查一次
            val runtime = Runtime.getRuntime()
            val memoryUsagePercent = ((runtime.totalMemory() - runtime.freeMemory()) * 100 / runtime.maxMemory()).toInt()

            // 降低内存压力阈值，更积极地清理
            if (memoryUsagePercent > 60) {
                android.util.Log.w("HomeScreen", "内存使用率: ${memoryUsagePercent}%, 触发清理")
                preloadManager.onMemoryPressure()
                // 强制垃圾回收
                System.gc()
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
            state = pagerState
        ) { page ->
            TopicItem(
                item = topicList[page],
                preloadManager = preloadManager,
                isCurrentPage = page == pagerState.currentPage
            )
        }
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

    // 获取预加载的播放器实例 - 添加错误处理
    val exoPlayer = remember(videoUrl) {
        if (videoUrl.isNotEmpty()) {
            try {
                preloadManager.getOrCreatePlayer(videoUrl)
            } catch (e: Exception) {
                android.util.Log.e("TopicItem", "创建播放器失败: $videoUrl", e)
                null
            }
        } else null
    }

    // 控制播放状态 - 添加错误处理
    LaunchedEffect(isCurrentPage, videoUrl) {
        if (videoUrl.isNotEmpty()) {
            try {
                if (isCurrentPage) {
                    delay(300) // 增加延迟以确保播放器准备就绪
                    preloadManager.playVideo(videoUrl)
                } else {
                    preloadManager.pauseVideo(videoUrl)
                }
            } catch (e: Exception) {
                android.util.Log.e("TopicItem", "控制播放状态失败: $videoUrl", e)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (videoUrl.isNotEmpty() && exoPlayer != null) {
            VideoPlayer(
                videoUrl = videoUrl,
                externalPlayer = exoPlayer,
                modifier = Modifier.fillMaxSize()
            )
        } else {
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
    }
}