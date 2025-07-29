package com.zyc.feature.ui_showcase.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
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
import com.zyc.core.ui.components.navigation.menu.MenuAction

@Composable
fun NavigationComponentsScreen(
    onBack: () -> Unit = {}
) {
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ZAppBar(
            title = "导航组件",
           onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 菜单组件部分
            ComponentSection(
                title = "菜单组件",
                description = "上下文菜单和弹出菜单组件展示"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // WeChatPopupMenu 示例
                    ComponentDemo(
                        title = "WeChatPopupMenu",
                        description = "微信风格弹出菜单"
                    ) {
                        val menuActions = listOf(
                            MenuAction(
                                icon = "\uEE01",
                                title = "编辑",
                                onClickMenu = {
                                    snackbarMessage = "点击了编辑"
                                    showSnackbar = true
                                }
                            ),
                            MenuAction(
                                icon = "\uEE01",
                                title = "分享",
                                onClickMenu = {
                                    snackbarMessage = "点击了分享"
                                    showSnackbar = true
                                }
                            ),
                            MenuAction(
                                icon = "\uEE01",
                                title = "删除",
                                onClickMenu = {
                                    snackbarMessage = "点击了删除"
                                    showSnackbar = true
                                }
                            )
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "长按下方卡片显示菜单：",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

//                            WeChatPopupMenu(
//                                menuActions = menuActions
//                            ) {
//                                Card(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .height(80.dp),
//                                    colors = CardDefaults.cardColors(
//                                        containerColor = MaterialTheme.colorScheme.primaryContainer
//                                    )
//                                ) {
//                                    Box(
//                                        modifier = Modifier.fillMaxSize(),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text = "长按我显示菜单",
//                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
//                                            fontWeight = FontWeight.Medium
//                                        )
//                                    }
//                                }
//                            }
                        }
                    }

                    // MenuAction 数据类说明
                    ComponentDemo(
                        title = "MenuAction",
                        description = "菜单项数据类"
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
                                    text = "MenuAction 数据结构：",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "data class MenuAction(\n    val icon: ImageVector,\n    val text: String,\n    val onClick: () -> Unit\n)",
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    fontSize = 12.sp
                                )
                            }
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
                            text = "import com.zyc.core.ui.components.navigation.menu.*",
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "使用步骤：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "1. 创建 MenuAction 列表，定义菜单项\n2. 使用 WeChatPopupMenu 包装需要显示菜单的内容\n3. 长按触发菜单显示",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "注意事项：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• 菜单会根据触摸位置自动调整显示位置\n• 支持自定义菜单项图标和文字\n• 点击菜单项后会自动关闭菜单",
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
