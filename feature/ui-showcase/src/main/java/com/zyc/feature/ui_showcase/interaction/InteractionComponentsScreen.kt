package com.zyc.feature.ui_showcase.interaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.zyc.core.ui.components.interaction.keyboard.InputArea

@Composable
fun InteractionComponentsScreen(
    onBack: () -> Unit = {}
) {
    var inputText by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ZAppBar(
            title = "交互组件",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // InputArea 组件部分
            ComponentSection(
                title = "InputArea 组件",
                description = "智能输入区域组件，支持多种输入模式和功能"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 基础输入区域示例
                    ComponentDemo(
                        title = "基础输入区域",
                        description = "简单的文本输入区域"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "当前输入内容：",
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = if (inputText.isNotEmpty()) inputText else "暂无输入",
                                    color = if (inputText.isNotEmpty())
                                        MaterialTheme.colorScheme.onSurface
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                InputArea(
                                    onSend = { text ->
                                        snackbarMessage = "发送了消息：$text"
                                        showSnackbar = true
                                        inputText = ""
                                    }
                                )
                            }
                        }
                    }

                    // 带表情功能的输入区域
                    ComponentDemo(
                        title = "带表情功能的输入区域",
                        description = "支持表情选择的输入区域"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "表情输入示例：",
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                InputArea(

                                    onSend = { text ->
                                        snackbarMessage = "发送了带表情的消息：$text"
                                        showSnackbar = true
                                        inputText = ""
                                    }
                                )
                            }
                        }
                    }

                    // 带附件功能的输入区域
                    ComponentDemo(
                        title = "带附件功能的输入区域",
                        description = "支持附件上传的输入区域"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "附件输入示例：",
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                InputArea(

                                    onSend = { text ->
                                        snackbarMessage = "发送了带附件的消息：$text"
                                        showSnackbar = true
                                        inputText = ""
                                    }
                                )
                            }
                        }
                    }

                    // 完整功能的输入区域
                    ComponentDemo(
                        title = "完整功能的输入区域",
                        description = "包含所有功能的输入区域"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "完整功能示例：",
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                InputArea(

                                    onSend = { text ->
                                        snackbarMessage = "发送了完整功能消息：$text"
                                        showSnackbar = true
                                        inputText = ""
                                    }
                                )
                            }
                        }
                    }

                    // 自定义样式的输入区域
                    ComponentDemo(
                        title = "自定义样式的输入区域",
                        description = "可以自定义外观的输入区域"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "自定义样式示例：",
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                InputArea(

                                    onSend = { text ->
                                        snackbarMessage = "发送了自定义样式消息：$text"
                                        showSnackbar = true
                                        inputText = ""
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // 功能测试区域
            ComponentSection(
                title = "功能测试",
                description = "测试输入组件的各种功能"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                inputText = "这是一个测试消息 😊"
                                snackbarMessage = "设置了测试文本"
                                showSnackbar = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("设置测试文本")
                        }

                        Button(
                            onClick = {
                                inputText = ""
                                snackbarMessage = "清空了输入内容"
                                showSnackbar = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("清空内容")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                inputText += "📷"
                                snackbarMessage = "添加了图片表情"
                                showSnackbar = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("添加表情")
                        }

                        Button(
                            onClick = {
                                inputText += "[附件]"
                                snackbarMessage = "添加了附件标记"
                                showSnackbar = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("添加附件")
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
                            text = "import com.zyc.core.ui.components.interaction.keyboard.InputArea",
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "主要参数：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• value: String - 输入框的值\n• onValueChange: (String) -> Unit - 值变化回调\n• placeholder: String - 占位符文本\n• onSend: (String) -> Unit - 发送按钮回调\n• showEmojiButton: Boolean - 是否显示表情按钮\n• showAttachmentButton: Boolean - 是否显示附件按钮\n• showVoiceButton: Boolean - 是否显示语音按钮\n• maxLines: Int - 最大行数\n• enabled: Boolean - 是否启用",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "使用建议：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• 根据场景选择合适的功能按钮\n• 合理设置最大行数避免界面过高\n• 提供清晰的占位符文本提示用户\n• 处理好发送回调中的业务逻辑",
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
