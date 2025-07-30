package com.zyc.feature.ui_showcase.hardware.sensor

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.IconBackground

/**
 * 传感器组件演示
 * 展示设备传感器功能的组件
 */
@Composable
fun SensorComponentDemo() {
    var accelerometerData by remember { mutableStateOf("未获取") }
    var gyroscopeData by remember { mutableStateOf("未获取") }
    var magnetometerData by remember { mutableStateOf("未获取") }

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
                    icon = "\uEE07",
                    color = Color(0xff4720ff)
                )
                Text(
                    text = "传感器数据",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "加速度计: $accelerometerData",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "陀螺仪: $gyroscopeData",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "磁力计: $magnetometerData",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        // 模拟获取传感器数据
                        accelerometerData = "X: 0.1, Y: 0.2, Z: 9.8"
                        gyroscopeData = "X: 0.01, Y: 0.02, Z: 0.03"
                        magnetometerData = "X: 25.5, Y: -12.3, Z: 45.2"
                    }
                ) {
                    Text("获取数据")
                }

                OutlinedButton(
                    onClick = {
                        accelerometerData = "未获取"
                        gyroscopeData = "未获取"
                        magnetometerData = "未获取"
                    }
                ) {
                    Text("清除数据")
                }
            }
        }
    }
}
