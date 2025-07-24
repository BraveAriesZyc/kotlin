package com.zyc.clover.pages.layout.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import com.zyc.clover.components.medium.VideoPlayer
import com.zyc.clover.utils.manager.VideoPreloadManager
import com.zyc.data.models.WorkModel
import kotlinx.coroutines.delay


val PADDING = 8.dp



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

    // 预加载相邻视频
    LaunchedEffect(pagerState.currentPage, topicList) {
        val currentPage = pagerState.currentPage
        val currentVideoUrl = topicList.getOrNull(currentPage)?.workList?.firstOrNull()
        
        preloadManager.setCurrentPlaying(currentVideoUrl)

        // 预加载当前页面的前后视频（前后各1页）
        for (offset in -1..1) {
            val targetPage = currentPage + offset
            topicList.getOrNull(targetPage)?.workList?.firstOrNull()?.let { videoUrl ->
                if (videoUrl.isNotEmpty()) {
                    preloadManager.preloadVideo(videoUrl)
                }
            }
        }
    }
    
    // 内存监控
    LaunchedEffect(Unit) {
        while (true) {
            delay(30000) // 每30秒检查一次
            val runtime = Runtime.getRuntime()
            val memoryUsagePercent = ((runtime.totalMemory() - runtime.freeMemory()) * 100 / runtime.maxMemory()).toInt()
            
            if (memoryUsagePercent > 80) {
                preloadManager.onMemoryPressure()
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
    
    // 获取预加载的播放器实例
    val exoPlayer = remember(videoUrl) {
        if (videoUrl.isNotEmpty()) preloadManager.getOrCreatePlayer(videoUrl) else null
    }

    // 控制播放状态
    LaunchedEffect(isCurrentPage, videoUrl) {
        if (videoUrl.isNotEmpty()) {
            if (isCurrentPage) {
                delay(100) // 确保播放器准备就绪
                preloadManager.playVideo(videoUrl)
            } else {
                preloadManager.pauseVideo(videoUrl)
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
