package com.zyc.feature.ui_showcase.hardware.bluetooth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.IconBackground

/**
 * 蓝牙组件演示
 * 展示蓝牙设备连接和通信功能的组件
 */
@Composable
fun BluetoothComponentDemo() {
    var isBluetoothEnabled by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }
    var connectedDevices by remember { mutableStateOf(0) }

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
                    icon = "\uEA9B",
                    color = Color(0xffff67c9)
                )
                Text(
                    text = "蓝牙控制",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "蓝牙状态: ${if (isBluetoothEnabled) "已开启" else "已关闭"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "扫描状态: ${if (isScanning) "扫描中" else "未扫描"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "已连接设备: $connectedDevices 个",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isBluetoothEnabled = !isBluetoothEnabled }
                ) {
                    Text(if (isBluetoothEnabled) "关闭蓝牙" else "开启蓝牙")
                }

                OutlinedButton(
                    onClick = {
                        isScanning = !isScanning
                        if (isScanning) {
                            // 模拟发现设备
                            connectedDevices = (1..3).random()
                        }
                    },
                    enabled = isBluetoothEnabled
                ) {
                    Text(if (isScanning) "停止扫描" else "扫描设备")
                }

                OutlinedButton(
                    onClick = {
                        connectedDevices = 0
                        isScanning = false
                    },
                    enabled = isBluetoothEnabled
                ) {
                    Text("断开所有")
                }
            }
        }
    }
}
