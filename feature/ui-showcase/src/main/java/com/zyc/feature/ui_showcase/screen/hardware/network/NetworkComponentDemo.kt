package com.zyc.feature.ui_showcase.screen.hardware.network

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.IconBackground

/**
 * 网络组件演示
 * 展示网络连接状态和功能的组件
 */
@Composable
fun NetworkComponentDemo() {
    var isConnected by remember { mutableStateOf(true) }
    var networkType by remember { mutableStateOf("WiFi") }
    var signalStrength by remember { mutableStateOf(85) }

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
                    icon = "\uEF33",
                    color = Color(0xff0080ff)
                )
                Text(
                    text = "网络状态",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "连接状态: ${if (isConnected) "已连接" else "未连接"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "网络类型: $networkType",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "信号强度: $signalStrength%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        // 模拟网络检测
                        isConnected = !isConnected
                        if (isConnected) {
                            networkType = listOf("WiFi", "4G", "5G").random()
                            signalStrength = (60..100).random()
                        }
                    }
                ) {
                    Text("检测网络")
                }

                OutlinedButton(
                    onClick = {
                        // 模拟网络测速
                        signalStrength = (30..100).random()
                    }
                ) {
                    Text("测试速度")
                }

                OutlinedButton(
                    onClick = {
                        // 模拟刷新网络
                        networkType = listOf("WiFi", "4G", "5G", "3G").random()
                    }
                ) {
                    Text("刷新")
                }
            }
        }
    }
}
