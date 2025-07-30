package com.zyc.feature.ui_showcase.hardware.location

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.IconBackground

/**
 * GPS定位组件演示
 * 展示位置获取和地图显示功能的组件
 */
@Composable
fun LocationComponentDemo() {
    var isLocationEnabled by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf("未获取") }
    var accuracy by remember { mutableStateOf("未知") }

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
                    icon = "\uED71",
                    color = Color(0xffffe423)
                )
                Text(
                    text = "GPS定位",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "定位状态: ${if (isLocationEnabled) "已开启" else "已关闭"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "当前位置: $currentLocation",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "精度: $accuracy",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        isLocationEnabled = !isLocationEnabled
                        if (isLocationEnabled) {
                            // 模拟获取位置
                            currentLocation = "北京市朝阳区"
                            accuracy = "±5米"
                        } else {
                            currentLocation = "未获取"
                            accuracy = "未知"
                        }
                    }
                ) {
                    Text(if (isLocationEnabled) "停止定位" else "开始定位")
                }

                OutlinedButton(
                    onClick = {
                        // 模拟刷新位置
                        if (isLocationEnabled) {
                            val locations = listOf(
                                "北京市海淀区",
                                "上海市浦东新区",
                                "广州市天河区",
                                "深圳市南山区"
                            )
                            currentLocation = locations.random()
                            accuracy = "±${(3..10).random()}米"
                        }
                    },
                    enabled = isLocationEnabled
                ) {
                    Text("刷新位置")
                }

                OutlinedButton(
                    onClick = {
                        // 模拟打开地图
                    },
                    enabled = isLocationEnabled
                ) {
                    Text("打开地图")
                }
            }
        }
    }
}
