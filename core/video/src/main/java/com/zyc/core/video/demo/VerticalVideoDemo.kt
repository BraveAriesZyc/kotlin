package com.zyc.core.video.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.video.page.VideoData
import com.zyc.core.video.page.VerticalVideoPage
import com.zyc.core.video.page.VerticalVideoPageData

/**
 * 垂直视频页面演示
 * 展示类似抖音、快手的上下滑动切换视频效果
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalVideoDemo(
    onBack: () -> Unit = {}
) {
    var currentPage by remember { mutableIntStateOf(0) }
    var showAppBar by remember { mutableStateOf(true) }
    
    // 演示视频数据
    val demoVideos = listOf(
        VideoData(
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            title = "Big Buck Bunny",
            description = "一只大兔子的冒险故事，这是一个开源的3D动画短片，展示了精美的动画制作技术。"
        ),
        VideoData(
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            title = "Elephant's Dream",
            description = "大象之梦，世界上第一部完全使用开源软件制作的3D动画电影。"
        ),
        VideoData(
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            title = "For Bigger Blazes",
            description = "壮观的火焰场景，展示了高质量的视频编码和播放效果。"
        ),
        VideoData(
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            title = "Sintel",
            description = "一个关于勇敢女孩和她的龙朋友的感人故事，由Blender Foundation制作。"
        ),
        VideoData(
            url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            title = "Tears of Steel",
            description = "钢铁之泪，一部科幻短片，展示了开源电影制作的可能性。"
        )
    )
    
    val pagerState = rememberPagerState(pageCount = { demoVideos.size })
    
    Box(modifier = Modifier.fillMaxSize()) {
        // 垂直视频页面
        VerticalVideoPage(
            data = VerticalVideoPageData(
                videos = demoVideos,
                onPageChange = { page -> 
                    currentPage = page
                    // 滑动时隐藏顶部栏
                    showAppBar = false
                },
                pagerState = pagerState,
                autoPlay = true,
                showSystemControls = false
            )
        )
        
        // 顶部应用栏（可隐藏）
        if (showAppBar) {
            TopAppBar(
                title = {
                    Text(
                        text = "短视频演示",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.5f)
                )
            )
        }
        
        // 右侧页面指示器
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(demoVideos.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == currentPage) 12.dp else 8.dp)
                        .background(
                            color = if (index == currentPage) Color.White else Color.White.copy(alpha = 0.5f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
            }
        }
        
        // 左下角视频信息
        if (currentPage < demoVideos.size) {
            val currentVideo = demoVideos[currentPage]
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (currentVideo.description.isNotEmpty()) {
                    Text(
                        text = currentVideo.description,
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        // 右下角操作按钮
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 点赞按钮
            FloatingActionButton(
                onClick = { /* 点赞操作 */ },
                modifier = Modifier.size(48.dp),
                containerColor = Color.White.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "❤️",
                    fontSize = 20.sp
                )
            }
            
            // 评论按钮
            FloatingActionButton(
                onClick = { /* 评论操作 */ },
                modifier = Modifier.size(48.dp),
                containerColor = Color.White.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "💬",
                    fontSize = 20.sp
                )
            }
            
            // 分享按钮
            FloatingActionButton(
                onClick = { /* 分享操作 */ },
                modifier = Modifier.size(48.dp),
                containerColor = Color.White.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "📤",
                    fontSize = 20.sp
                )
            }
        }
        
        // 点击屏幕显示/隐藏顶部栏
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            // 这里可以添加手势检测来控制UI显示/隐藏
        }
    }
    
    // 监听页面变化，自动隐藏顶部栏
    LaunchedEffect(currentPage) {
        kotlinx.coroutines.delay(3000) // 3秒后自动隐藏
        showAppBar = false
    }
}