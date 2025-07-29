package com.zyc.feature.ui_showcase.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.feature.ui_showcase.components.ComponentSection
import com.zyc.feature.ui_showcase.components.ComponentDemo
import com.zyc.core.ui.components.feedback.loading.*
import com.zyc.core.ui.components.feedback.progress.CustomProgressBar
import kotlinx.coroutines.delay

@Composable
fun FeedbackComponentsScreen(
    onBack: () -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }

    // 模拟进度更新
    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            progress = (progress + 0.01f) % 1f
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ZAppBar(
            title = "反馈组件",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 加载组件部分
            ComponentSection(
                title = "加载组件",
                description = "各种类型的加载动画组件展示"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Loading 示例
                    ComponentDemo(
                        title = "Loading",
                        description = "通用加载组件"
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { isLoading = !isLoading }
                            ) {
                                Text(if (isLoading) "停止加载" else "开始加载")
                            }
                            if (isLoading) {
                                Loading.AnimatedBallLoader()
                            }
                        }
                    }

                    // AnimatedBallLoaderImp 示例
                    ComponentDemo(
                        title = "AnimatedBallLoaderImp",
                        description = "球形动画加载器"
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedBallLoaderImp()
                        }
                    }

                    // DouyinBounceLoaderImp 示例
                    ComponentDemo(
                        title = "DouyinBounceLoaderImp",
                        description = "抖音风格弹跳加载器"
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Loading.DouyinBounceLoader()
                        }
                    }

                    // HorizontalBounceLoaderImp 示例
                    ComponentDemo(
                        title = "HorizontalBounceLoaderImp",
                        description = "水平弹跳加载器"
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Loading.HorizontalBounceLoader()
                        }
                    }

                    // TextLoaderImp 示例
                    ComponentDemo(
                        title = "TextLoaderImp",
                        description = "文字加载器"
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TextLoaderImp()
                        }
                    }
                }
            }

            // 进度组件部分
            ComponentSection(
                title = "进度组件",
                description = "进度条和进度指示器组件展示"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // CustomProgressBar 示例
                    ComponentDemo(
                        title = "CustomProgressBar",
                        description = "自定义进度条"
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "进度: ${(progress * 100).toInt()}%",
                                fontSize = 14.sp
                            )
                            CustomProgressBar(
                                progress = progress,
                                modifier = Modifier.fillMaxWidth(),
                                onProgressChange = { }
                            )
                        }
                    }

                    // 系统进度条对比
                    ComponentDemo(
                        title = "LinearProgressIndicator",
                        description = "系统线性进度条（对比）"
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "进度: ${(progress * 100).toInt()}%",
                                fontSize = 14.sp
                            )
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // 使用说明
            ComponentSection(
                title = "使用说明",
                description = "组件的基本使用方法"
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "导入方式：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "import com.zyc.core.ui.components.feedback.loading.*\nimport com.zyc.core.ui.components.feedback.progress.*",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "使用建议：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• 加载组件适用于数据请求、页面切换等场景\n• 进度组件适用于文件上传、下载等有明确进度的操作\n• 根据设计风格选择合适的加载动画",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
