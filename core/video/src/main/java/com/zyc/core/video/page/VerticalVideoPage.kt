package com.zyc.core.video.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.zyc.core.video.VideoPlayerCompose

/**
 * 视频数据类
 */
data class VideoData(
    val url: String,
    val title: String = "",
    val description: String = "",
    val thumbnail: String = ""
)

/**
 * 垂直视频页面数据
 */
data class VerticalVideoPageData(
    val videos: List<VideoData>,
    val onPageChange: (Int) -> Unit = {},
    val pagerState: PagerState,
    val autoPlay: Boolean = true,
    val showSystemControls: Boolean = false
)

/**
 * 垂直滚动视频页面组件
 * 类似抖音、快手等短视频应用的上下滑动切换视频效果
 */
@Composable
fun VerticalVideoPage(
    data: VerticalVideoPageData,
    modifier: Modifier = Modifier
) {
    // 页面变化回调
    LaunchedEffect(data.pagerState.currentPage) {
        data.onPageChange(data.pagerState.currentPage)
    }

    VerticalPager(
        state = data.pagerState,
        modifier = modifier.fillMaxSize()
    ) { page ->
        if (page < data.videos.size) {
            val video = data.videos[page]
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                VideoPlayerCompose(
                    videoUrl = video.url,
                    modifier = Modifier.fillMaxSize(),
                    autoPlay = data.autoPlay && page == data.pagerState.currentPage,
                    showSystemControls = data.showSystemControls,
                    showCustomControls = !data.showSystemControls,
                    aspectRatio = null,
                )
            }
        }
    }
}

/**
 * 简化的垂直视频页面组件
 */
@Composable
fun SimpleVerticalVideoPage(
    videoUrls: List<String>,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    showSystemControls: Boolean = false,
    onPageChange: (Int) -> Unit = {}
) {
    val videos = videoUrls.map { VideoData(url = it) }
    val pagerState = rememberPagerState(pageCount = { videos.size })
    
    VerticalVideoPage(
        data = VerticalVideoPageData(
            videos = videos,
            onPageChange = onPageChange,
            pagerState = pagerState,
            autoPlay = autoPlay,
            showSystemControls = showSystemControls
        ),
        modifier = modifier
    )
}

/**
 * 记住垂直视频页面状态
 */
@Composable
fun rememberVerticalVideoPageState(
    videos: List<VideoData>,
    initialPage: Int = 0
): VerticalVideoPageData {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { videos.size }
    )
    
    return VerticalVideoPageData(
        videos = videos,
        pagerState = pagerState
    )
}