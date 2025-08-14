package com.zyc.feature.ui_showcase.screen.network.media

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView
import com.zyc.core.video.SimpleVideoPlayer
import com.zyc.core.video.VideoPlayerCompose
import com.zyc.core.video.player.VideoPlayer
import com.zyc.core.video.utils.VideoUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoComponentsScreen(
    onBack: () -> Unit = {}
) {
    var selectedVideoUrl by remember { mutableStateOf(sampleVideos.first().url) }
    var playbackState by remember { mutableStateOf(Player.STATE_IDLE) }

    Scaffold(
        topBar = {
            ZAppBar(
                title = "视频播放器示例",
                onBack = onBack
            )
        },


        content = { pd ->
            BounceListView(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = pd.calculateTopPadding(),
                        bottom = pd.calculateBottomPadding()
                    )
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp),
            ) {
                // 基础视频播放器
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "基础视频播放器",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            VideoPlayer(
                                videoUrl = selectedVideoUrl,
                                autoPlay = false,
                                showControls = true,
                                onPlaybackStateChanged = { state ->
                                    playbackState = state
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "播放状态: ${getPlaybackStateText(playbackState)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // 完整功能视频播放器
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "完整功能视频播放器",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            VideoPlayerCompose(
                                videoUrl = selectedVideoUrl,
                                autoPlay = false,
                                showSystemControls = false,
                                showCustomControls = true
                            )
                        }
                    }
                }

                // 简化版播放器
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "简化版播放器",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            SimpleVideoPlayer(
                                videoUrl = selectedVideoUrl,
                                autoPlay = false
                            )
                        }
                    }
                }


                // 垂直视频页面示例
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "垂直视频页面示例",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "类似抖音、快手的上下滑动切换视频效果",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { /* 这里可以导航到垂直视频页面 */ },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("完整示例")
                                }

                                OutlinedButton(
                                    onClick = { /* 这里可以导航到简化版页面 */ },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("简化版")
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "特性:\n• 垂直滑动切换视频\n• 自动播放当前视频\n• 页面指示器\n• 视频信息覆盖层",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // 视频选择
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "选择视频",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            sampleVideos.forEach { video ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedVideoUrl == video.url,
                                        onClick = { selectedVideoUrl = video.url }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = video.title,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = video.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 工具函数示例
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "工具函数示例",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text("时间格式化: ${VideoUtils.formatTime(125000)}")
                            Text("文件大小格式化: ${VideoUtils.formatFileSize(1024 * 1024 * 50)}")
                            Text("URL验证: ${VideoUtils.isValidVideoUrl(selectedVideoUrl)}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "播放速度选项:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            VideoUtils.getSpeedOptions().forEach { (speed, label) ->
                                Text(
                                    text = "$label ($speed)",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

/**
 * 获取播放状态文本
 */
private fun getPlaybackStateText(state: Int): String {
    return when (state) {
        Player.STATE_IDLE -> "空闲"
        Player.STATE_BUFFERING -> "缓冲中"
        Player.STATE_READY -> "准备就绪"
        Player.STATE_ENDED -> "播放结束"
        else -> "未知状态"
    }
}

/**
 * 示例视频数据
 */
data class SampleVideo(
    val title: String,
    val description: String,
    val url: String
)

private val sampleVideos = listOf(
    SampleVideo(
        title = "Big Buck Bunny",
        description = "开源测试视频 - MP4格式",
        url = "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover_oss/1383efe0e5924feb98f1d6334df919ec_share_dadcc43dee633c78cb597fa308c9a57d1755097038756.mp4"
    ),
    SampleVideo(
        title = "Elephant Dream",
        description = "开源测试视频 - MP4格式",
        url = "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover_oss/f266a286d47c44119934a09d96994af5_share_dadcc43dee633c78cb597fa308c9a57d1755097038756.mp4"
    ),
    SampleVideo(
        title = "For Bigger Blazes",
        description = "Google测试视频 - MP4格式",
        url = "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover_oss/2d0be6c8f3b640efa7f2bf154a53150f_share_3a868f72d29dd71f2bf0a6808d5c54841755086447681.mp4"
    ),
    SampleVideo(
        title = "Sintel",
        description = "开源测试视频 - MP4格式",
        url = "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover_oss/2d0be6c8f3b640efa7f2bf154a53150f_share_3a868f72d29dd71f2bf0a6808d5c54841755086447681.mp4"
    )
)