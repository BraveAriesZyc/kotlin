package com.zyc.feature.ui_showcase.hardware.biometric

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.IconBackground
import kotlinx.coroutines.launch

/**
 * 生物识别组件演示
 * 展示指纹识别和生物认证功能的组件
 */
@Composable
fun BiometricComponentDemo() {
    var isBiometricAvailable by remember { mutableStateOf(true) }
    var isAuthenticating by remember { mutableStateOf(false) }
    var authResult by remember { mutableStateOf("未认证") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconBackground(
                    icon = "\uEC09",
                    color = Color(0xffff00b6)
                )
                Text(
                    text = "生物识别",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "设备支持: ${if (isBiometricAvailable) "支持" else "不支持"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "认证状态: ${if (isAuthenticating) "认证中" else "待认证"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "认证结果: $authResult",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        isAuthenticating = true
                        // 模拟认证过程
                        authResult = "认证中..."
                        // 这里应该调用实际的生物识别API
                        // 模拟认证结果
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                            kotlinx.coroutines.delay(2000)
                            isAuthenticating = false
                            authResult = if ((0..1).random() == 1) "认证成功" else "认证失败"
                        }
                    },
                    enabled = isBiometricAvailable && !isAuthenticating
                ) {
                    Text("指纹认证")
                }

                OutlinedButton(
                    onClick = {
                        isAuthenticating = true
                        authResult = "面部识别中..."
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                            kotlinx.coroutines.delay(1500)
                            isAuthenticating = false
                            authResult = if ((0..1).random() == 1) "面部识别成功" else "面部识别失败"
                        }
                    },
                    enabled = isBiometricAvailable && !isAuthenticating
                ) {
                    Text("面部识别")
                }

                OutlinedButton(
                    onClick = {
                        authResult = "未认证"
                        isAuthenticating = false
                    }
                ) {
                    Text("重置")
                }
            }
        }
    }
}
