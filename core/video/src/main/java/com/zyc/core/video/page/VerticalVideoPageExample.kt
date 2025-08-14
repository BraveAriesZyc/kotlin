package com.zyc.core.video.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 垂直视频页面使用示例
 */
@Composable
fun VerticalVideoPageExample() {
    var currentPage by remember { mutableIntStateOf(0) }
    
    // 示例视频数据
    val sampleVideos = listOf(
        VideoData(
            url = "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover_oss/1383efe0e5924feb98f1d6334df919ec_share_dadcc43dee633c78cb597fa308c9a57d1755097038756.mp4",
            title = "示例视频 1",
            description = "这是第一个示例视频"
        ),
        VideoData(
            url = "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover_oss/f266a286d47c44119934a09d96994af5_share_dadcc43dee633c78cb597fa308c9a57d1755097038756.mp4",
            title = "示例视频 2",
            description = "这是第二个示例视频"
        ),
        VideoData(
            url = "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover_oss/2d0be6c8f3b640efa7f2bf154a53150f_share_3a868f72d29dd71f2bf0a6808d5c54841755086447681.mp4",
            title = "示例视频 3",
            description = "这是第三个示例视频"
        )
    )
    
    val pagerState = rememberPagerState(pageCount = { sampleVideos.size })
    
    Box(modifier = Modifier.fillMaxSize()) {
        // 垂直视频页面
        VerticalVideoPage(
            data = VerticalVideoPageData(
                videos = sampleVideos,
                onPageChange = { page -> currentPage = page },
                pagerState = pagerState,
                autoPlay = true,
                showSystemControls = false
            )
        )
        
        // 页面指示器
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(sampleVideos.size) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (index == currentPage) Color.White else Color.White.copy(alpha = 0.5f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
            }
        }
        
        // 视频信息覆盖层
        if (currentPage < sampleVideos.size) {
            val currentVideo = sampleVideos[currentPage]
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .fillMaxWidth(0.7f)
            ) {
                if (currentVideo.title.isNotEmpty()) {
                    Text(
                        text = currentVideo.title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                if (currentVideo.description.isNotEmpty()) {
                    Text(
                        text = currentVideo.description,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * 简化版垂直视频页面示例
 */
@Composable
fun SimpleVerticalVideoPageExample() {
    val videoUrls = listOf(
        "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
        "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
        "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_5mb.mp4"
    )
    
    SimpleVerticalVideoPage(
        videoUrls = videoUrls,
        autoPlay = true,
        showSystemControls = false,
        onPageChange = { page ->
            println("当前页面: $page")
        }
    )
}

/**
 * 自定义垂直视频页面示例
 */
@Composable
fun CustomVerticalVideoPageExample() {
    val videos = listOf(
        VideoData(
            url = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
            title = "风景视频",
            description = "美丽的自然风光"
        ),
        VideoData(
            url = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
            title = "城市夜景",
            description = "繁华的都市夜晚"
        )
    )
    
    val pageData = rememberVerticalVideoPageState(
        videos = videos,
        initialPage = 0
    )
    
    VerticalVideoPage(
        data = pageData.copy(
            autoPlay = true,
            showSystemControls = false,
            onPageChange = { page ->
                // 处理页面变化
                println("切换到视频: ${videos.getOrNull(page)?.title}")
            }
        )
    )
}