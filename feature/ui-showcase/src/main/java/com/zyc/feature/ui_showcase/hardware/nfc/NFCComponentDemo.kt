package com.zyc.feature.ui_showcase.hardware.nfc

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.IconBackground

/**
 * NFC组件演示
 * 展示近场通信功能的组件
 */
@Composable
fun NFCComponentDemo() {
    var isNFCEnabled by remember { mutableStateOf(false) }
    var isReading by remember { mutableStateOf(false) }
    var lastReadData by remember { mutableStateOf("无数据") }

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
                    icon = "\uEA11",
                    color = Color(0xff08ff2a)
                )
                Text(
                    text = "NFC控制",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "NFC状态: ${if (isNFCEnabled) "已开启" else "已关闭"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "读取状态: ${if (isReading) "监听中" else "未监听"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "最后读取: $lastReadData",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isNFCEnabled = !isNFCEnabled }
                ) {
                    Text(if (isNFCEnabled) "关闭NFC" else "开启NFC")
                }

                OutlinedButton(
                    onClick = {
                        isReading = !isReading
                        if (isReading) {
                            // 模拟读取NFC数据
                            lastReadData = "卡片ID: ${(1000..9999).random()}"
                        }
                    },
                    enabled = isNFCEnabled
                ) {
                    Text(if (isReading) "停止读取" else "开始读取")
                }

                OutlinedButton(
                    onClick = {
                        // 模拟写入NFC数据
                        lastReadData = "写入成功: ${System.currentTimeMillis()}"
                    },
                    enabled = isNFCEnabled
                ) {
                    Text("写入数据")
                }
            }
        }
    }
}
