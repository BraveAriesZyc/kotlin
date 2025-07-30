package com.zyc.feature.ui_showcase.hardware.vibration

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.IconBackground
import com.zyc.core.ui.utils.sysHardwareUtil.VibrationUtils


/**
 * 振动组件演示
 * 展示设备振动功能的组件
 */
@Composable
fun VibrationComponentDemo() {
    val context = LocalContext.current
    var lastVibrationTime by remember { mutableStateOf(0L) }

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
                    icon = "\uED7E",
                    color = Color(0xffff5619)
                )
                Text(
                    text = "振动控制",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "上次振动: ${if (lastVibrationTime > 0) "${System.currentTimeMillis() - lastVibrationTime}ms 前" else "无"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        VibrationUtils.vibrateShort(context)
                        lastVibrationTime = System.currentTimeMillis()
                    }
                ) {
                    Text("短振动")
                }

                OutlinedButton(
                    onClick = {
                        VibrationUtils.vibrateLong(context)
                        lastVibrationTime = System.currentTimeMillis()
                    }
                ) {
                    Text("长振动")
                }

                OutlinedButton(
                    onClick = {
                        VibrationUtils.vibratePattern(context)
                        lastVibrationTime = System.currentTimeMillis()
                    }
                ) {
                    Text("模式振动")
                }
            }
        }
    }
}
