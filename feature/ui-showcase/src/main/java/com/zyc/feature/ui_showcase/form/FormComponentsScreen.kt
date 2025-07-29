package com.zyc.feature.ui_showcase.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.feature.ui_showcase.components.ComponentSection
import com.zyc.feature.ui_showcase.components.ComponentDemo
import com.zyc.core.ui.components.form.button.FormButton
import com.zyc.core.ui.components.form.input.FormInput
import com.zyc.core.ui.components.form.input.NoBorderFormInput

@Composable
fun FormComponentsScreen(
    onBack: () -> Unit = {}
) {
    var inputText1 by remember { mutableStateOf("") }
    var inputText2 by remember { mutableStateOf("") }
    var inputText3 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ZAppBar(
            title = "表单组件",
         onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 按钮组件部分
            ComponentSection(
                title = "按钮组件",
                description = "各种类型的按钮组件展示"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ButtonComponent 示例
                    ComponentDemo(
                        title = "ButtonComponent",
                        description = "基础按钮组件"
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FormButton(
                                text = "主要按钮",
                                onClick = { /* 处理提交 */ }
                            )
                            FormButton(
                                text = "次要按钮",
                                onClick = { /* 处理提交 */ }
                            )
                        }
                    }

                    // FormButton 示例
                    ComponentDemo(
                        title = "FormButton",
                        description = "表单专用按钮"
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FormButton(
                                text = "提交表单",
                                onClick = { /* 处理提交 */ }
                            )
                            FormButton(
                                text = "重置表单",
                                onClick = { /* 处理重置 */ }
                            )
                        }
                    }
                }
            }

            // 输入框组件部分
            ComponentSection(
                title = "输入框组件",
                description = "各种类型的输入框组件展示"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // FormInput 示例
                    ComponentDemo(
                        title = "FormInput",
                        description = "标准表单输入框"
                    ) {
                        FormInput(
                            value = inputText1,
                            onValueChange = { inputText1 = it },
                            placeholder = "请输入内容",
                        )
                    }

                    // InputComponent 示例
                    ComponentDemo(
                        title = "InputComponent",
                        description = "基础输入组件"
                    ) {
                        FormInput(
                            value = inputText2,
                            onValueChange = { inputText2 = it },
                            placeholder = "基础输入组件"
                        )
                    }

                    // NoBorderFormInput 示例
                    ComponentDemo(
                        title = "NoBorderFormInput",
                        description = "无边框表单输入框"
                    ) {
                        NoBorderFormInput(
                            value = inputText3,
                            onValueChange = { inputText3 = it },
                            placeholder = "无边框输入框"
                        )
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
                            text = "import com.zyc.core.ui.components.form.button.*\nimport com.zyc.core.ui.components.form.input.*",
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
