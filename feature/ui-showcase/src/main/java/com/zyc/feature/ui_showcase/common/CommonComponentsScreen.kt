package com.zyc.feature.ui_showcase.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.feature.ui_showcase.components.ComponentSection
import com.zyc.feature.ui_showcase.components.ComponentDemo

@Composable
fun CommonComponentsScreen(
    onBack: () -> Unit = {}
) {
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ZAppBar(
            title = "通用组件",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ZAppBar 组件部分
            ComponentSection(
                title = "ZAppBar 组件",
                description = "统一的应用顶部导航栏组件"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 基础 ZAppBar 示例
                    ComponentDemo(
                        title = "基础 ZAppBar",
                        description = "只有标题的基础导航栏"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            ZAppBar(
                                title = "基础标题"
                            )
                        }
                    }

                    // 带返回按钮的 ZAppBar
                    ComponentDemo(
                        title = "带返回按钮的 ZAppBar",
                        description = "包含返回按钮的导航栏"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            ZAppBar(
                                title = "带返回按钮",
                                onBack = {
                                    snackbarMessage = "点击了返回按钮"
                                    showSnackbar = true
                                }
                            )
                        }
                    }

                    // 带操作按钮的 ZAppBar
                    ComponentDemo(
                        title = "带操作按钮的 ZAppBar",
                        description = "包含右侧操作按钮的导航栏"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            ZAppBar(
                                title = "带操作按钮",
                                onBack = {
                                    snackbarMessage = "点击了返回按钮"
                                    showSnackbar = true
                                },
                                actions = {
                                    IconButton(
                                        onClick = {
                                            snackbarMessage = "点击了搜索按钮"
                                            showSnackbar = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "搜索"
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            snackbarMessage = "点击了设置按钮"
                                            showSnackbar = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = "设置"
                                        )
                                    }
                                }
                            )
                        }
                    }

                    // 自定义内容的 ZAppBar
                    ComponentDemo(
                        title = "自定义内容的 ZAppBar",
                        description = "使用自定义内容布局的导航栏"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            ZAppBar {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "自定义布局",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row {
                                        AssistChip(
                                            onClick = {
                                                snackbarMessage = "点击了标签1"
                                                showSnackbar = true
                                            },
                                            label = { Text("标签1") }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        AssistChip(
                                            onClick = {
                                                snackbarMessage = "点击了标签2"
                                                showSnackbar = true
                                            },
                                            label = { Text("标签2") }
                                        )
                                    }
                                }
                            }
                        }
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
                            text = "import com.zyc.core.ui.components.common.ZAppBar",
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "主要参数：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• title: String - 导航栏标题\n• showBackButton: Boolean - 是否显示返回按钮\n• onBackClick: () -> Unit - 返回按钮点击回调\n• actions: @Composable RowScope.() -> Unit - 右侧操作按钮\n• content: @Composable () -> Unit - 自定义内容（与其他参数互斥）",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "使用建议：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• 统一使用 ZAppBar 保持应用导航栏风格一致\n• 根据页面功能选择合适的导航栏类型\n• 自定义内容模式适用于特殊布局需求",
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
            kotlinx.coroutines.delay(2000)
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
