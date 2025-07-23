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
    
    // 状态管理
    var currentPlayingUrl by remember { mutableStateOf<String?>(null) }
    var isBuffering by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 预加载相邻视频
    LaunchedEffect(pagerState.currentPage, topicList) {
        val currentPage = pagerState.currentPage
        val currentVideoUrl = topicList.getOrNull(currentPage)?.workList?.firstOrNull()
        
        // 设置当前播放的视频
        currentPlayingUrl = currentVideoUrl
        preloadManager.setCurrentPlaying(currentVideoUrl)

        // 预加载当前页面的前后视频（当前页面 + 前后各1页）
        for (offset in -1..1) {
            val targetPage = currentPage + offset
            if (targetPage in 0 until topicList.size) {
                val videoUrl = topicList[targetPage].workList.firstOrNull()
                if (!videoUrl.isNullOrEmpty()) {
                    try {
                        preloadManager.preloadVideo(videoUrl)
                    } catch (e: Exception) {
                        // 预加载失败时记录日志，但不影响用户体验
                        android.util.Log.w("视频预加载", "预加载视频失败: $videoUrl", e)
                    }
                }
            }
        }

        // 输出缓存状态用于调试
        android.util.Log.d("视频预加载", preloadManager.getCacheInfo())
    }
    
    // 内存压力监听
    LaunchedEffect(Unit) {
        // 模拟内存监控，实际项目中可以使用系统内存监听
        // 这里简化为定期检查
        while (true) {
            delay(30000) // 每30秒检查一次
            val runtime = Runtime.getRuntime()
            val usedMemory = runtime.totalMemory() - runtime.freeMemory()
            val maxMemory = runtime.maxMemory()
            val memoryUsagePercent = (usedMemory * 100 / maxMemory).toInt()
            
            if (memoryUsagePercent > 80) {
                android.util.Log.w("视频预加载", "内存使用率过高: $memoryUsagePercent%")
                preloadManager.onMemoryPressure()
            }
        }
    }

    // 组件销毁时释放所有播放器
    DisposableEffect(Unit) {
        onDispose {
            preloadManager.destroy()
        }
    }

    Scaffold(
        content = { pd ->
            VerticalPager(
                modifier = Modifier
                    .padding(top = pd.calculateTopPadding())
                    .fillMaxSize(),
                state = pagerState,

                ) { page ->
                TopicItem(
                    item = topicList[page],
                    preloadManager = preloadManager,
                    isCurrentPage = page == pagerState.currentPage
                )
            }
        }
    )

}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun TopicItem(
    item: WorkModel,
    preloadManager: VideoPreloadManager,
    isCurrentPage: Boolean
) {
    val videoUrl = item.workList.firstOrNull() ?: ""
    
    // 状态管理
    var isPlayerReady by remember { mutableStateOf(false) }
    var bufferPercent by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    // 获取预加载的播放器实例
    val exoPlayer = if (videoUrl.isNotEmpty()) {
        preloadManager.getOrCreatePlayer(videoUrl)
    } else null

    // 根据是否为当前页面控制播放状态
    LaunchedEffect(isCurrentPage, videoUrl) {
        if (videoUrl.isNotEmpty()) {
            if (isCurrentPage) {
                // 延迟一小段时间再播放，确保播放器准备就绪
                delay(100)
                preloadManager.playVideo(videoUrl)
                isPlaying = true
            } else {
                // 非当前页面立即暂停播放
                preloadManager.pauseVideo(videoUrl)
                isPlaying = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    if (videoUrl.isNotEmpty()) {
                        // 显示视频播放器，传入预加载的播放器实例和状态回调
                        VideoPlayer(
                            videoUrl = videoUrl,
                            externalPlayer = exoPlayer,
                            preloadManager = preloadManager,
                            onPlayerReady = { url ->
                                isPlayerReady = true
                                errorMessage = null
                                android.util.Log.d("视频播放器", "播放器就绪: $url")
                            },
                            onPlayerBuffering = { url, percent ->
                                bufferPercent = percent
                                android.util.Log.d("视频播放器", "缓冲中: $url - $percent%")
                            },
                            onPlayerError = { url, error ->
                                errorMessage = error
                                isPlayerReady = false
                                android.util.Log.e("视频播放器", "播放器错误: $url - $error")
                            },
                            onPlayerEnded = { url ->
                                isPlaying = false
                                android.util.Log.d("视频播放器", "播放结束: $url")
                            }
                        )
                        
                        // 显示错误信息
                        errorMessage?.let { error ->
                            Card(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Text(
                                    text = "播放错误: $error",
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                        
                        // 显示缓冲进度（可选）
                        if (!isPlayerReady && bufferPercent > 0) {
                            Card(
                                modifier = Modifier
                                    .padding(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(
                                    text = "缓冲中: $bufferPercent%",
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    } else {
                        // 无视频内容时的占位符
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = "暂无视频内容",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            )
        }
    )
}
