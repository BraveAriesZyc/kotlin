package com.zyc.feature.ui_showcase.screen.hardware.camera

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.IconBackground

/**
 * 相机组件演示
 * 展示相机拍照和录像功能的组件
 */
@Composable
fun CameraComponentDemo() {
    var isCameraOpen by remember { mutableStateOf(false) }

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
                    icon = "\uEADD",
                    color = Color(0xff00d2ff)
                )
                Text(
                    text = "相机控制",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "状态: ${if (isCameraOpen) "已开启" else "已关闭"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isCameraOpen = !isCameraOpen }
                ) {
                    Text(if (isCameraOpen) "关闭相机" else "打开相机")
                }

                OutlinedButton(
                    onClick = { /* 拍照逻辑 */ }
                ) {
                    Text("拍照")
                }

                OutlinedButton(
                    onClick = { /* 录像逻辑 */ }
                ) {
                    Text("录像")
                }
            }
        }
    }
}
