package com.zyc.feature.ui_showcase.screen.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
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
import com.zyc.core.ui.components.layout.page.PageScreen
import com.zyc.core.ui.components.layout.page.PageScreenData
import com.zyc.core.ui.components.layout.refreshview.BounceListView
import com.zyc.core.ui.components.layout.refreshview.ZRefreshView
import com.zyc.feature.ui_showcase.screen.components.ComponentDemo
import com.zyc.feature.ui_showcase.screen.components.ComponentSection
import kotlinx.coroutines.delay


@Composable
fun LayoutComponentsScreen(
    onBack: () -> Unit = {}
) {
    var refreshing by remember { mutableStateOf(false) }
    var itemCount by remember { mutableStateOf(10) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val pagerState = rememberPagerState(pageCount = { 1 })

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ZAppBar(
            title = "布局组件",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // PageScreen 组件部分
            ComponentSection(
                title = "PageScreen 组件",
                description = "统一的页面布局组件，支持加载、错误、空状态等"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 加载状态示例
                    ComponentDemo(
                        title = "加载状态",
                        description = "显示加载中的页面状态"
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            PageScreen(
                                data = PageScreenData(
                                    pagerState = pagerState,
                                    pageContents = listOf(
                                        {
                                            Text(
                                                text = "加载中..."
                                            )
                                        }
                                    )
                                )
                            )

                        }
                    }

                    // 错误状态示例
                    ComponentDemo(
                        title = "错误状态",
                        description = "显示错误信息的页面状态"
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            PageScreen(
                                data = PageScreenData(
                                    pagerState = pagerState,
                                    pageContents = listOf(
                                        {
                                            Text(
                                                text = "加载中..."
                                            )
                                        }
                                    )
                                )
                            )
                        }
                    }

                    // 空状态示例
                    ComponentDemo(
                        title = "空状态",
                        description = "显示空数据的页面状态"
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            PageScreen(
                                data = PageScreenData(
                                    pagerState = pagerState,
                                    pageContents = listOf(
                                        {
                                            Text(
                                                text = "加载中..."
                                            )
                                        }
                                    )
                                )
                            )
                        }
                    }

                    // 成功状态示例
                    ComponentDemo(
                        title = "成功状态",
                        description = "显示正常内容的页面状态"
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            PageScreen(
                                data = PageScreenData(
                                    pagerState = pagerState,
                                    pageContents = listOf(
                                        {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(16.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "页面内容区域",
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "这里显示实际的页面内容",
                                                    fontSize = 14.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    )
                                )
                            )
                        }
                    }
                }
            }

            // ZRefreshView 组件部分
            ComponentSection(
                title = "ZRefreshView 组件",
                description = "支持下拉刷新的容器组件"
            ) {
                ComponentDemo(
                    title = "下拉刷新示例",
                    description = "支持下拉刷新功能的列表容器"
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        ZRefreshView(
                            onRefresh = {
                                refreshing = true
                                // 模拟刷新操作
                                snackbarMessage = "开始刷新数据"
                                showSnackbar = true
                            },
                            content = {
                                items(itemCount) { index ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "列表项 ${index + 1}",
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }

            // BounceListView 组件部分
            ComponentSection(
                title = "BounceListView 组件",
                description = "带弹性效果的列表视图组件"
            ) {
                ComponentDemo(
                    title = "弹性列表示例",
                    description = "具有弹性滚动效果的列表组件"
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        BounceListView(
                            content = {

                                (1..15).map { "弹性列表项 $it" }.forEach { item ->
                                    item {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 4.dp),
                                            content = {
                                                Text(
                                                    text = item,
                                                    modifier = Modifier.padding(16.dp)
                                                )
                                            }
                                        )
                                    }
                                }
                            }


                        )
                    }
                }
            }

            // 控制按钮
            ComponentSection(
                title = "交互控制",
                description = "测试组件的交互功能"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            refreshing = false
                            itemCount += 5
                            snackbarMessage = "刷新完成，新增了5个项目"
                            showSnackbar = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("完成刷新")
                    }

                    Button(
                        onClick = {
                            itemCount = 10
                            snackbarMessage = "重置列表项目"
                            showSnackbar = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("重置列表")
                    }
                }
            }

            // 使用说明
            ComponentSection(
                title = "使用说明",
                description = "组件的基本使用方法和参数说明"
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
                            text = "import com.zyc.core.ui.components.layout.page.*\nimport com.zyc.core.ui.components.layout.refreshview.*",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "主要组件：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• PageScreen - 统一页面状态管理\n• ZRefreshView - 下拉刷新容器\n• BounceListView - 弹性列表视图",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "使用建议：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• 使用 PageScreen 统一管理页面状态\n• 结合 ZRefreshView 实现下拉刷新功能\n• BounceListView 适用于需要弹性效果的列表",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    // Snackbar 显示
    if (showSnackbar) {
        LaunchedEffect(snackbarMessage) {
            delay(2000)
            showSnackbar = false
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(
                        onClick = { showSnackbar = false }
                    ) {
                        Text("关闭")
                    }
                }
            ) {
                Text(snackbarMessage)
            }
        }
    }
}
