package com.zyc.feature.ui_showcase.hardware.audio

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.IconBackground

/**
 * 音频组件演示
 * 展示音频播放和录制功能的组件
 */
@Composable
fun AudioComponentDemo() {
    var isPlaying by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var volume by remember { mutableStateOf(0.5f) }

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
                    icon = "\uEF15",
                    color = Color(0xFFE91E63)
                )
                Text(
                    text = "音频控制",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "播放状态: ${if (isPlaying) "播放中" else "已停止"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "录制状态: ${if (isRecording) "录制中" else "已停止"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "音量: ${(volume * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // 音量滑块
            Slider(
                value = volume,
                onValueChange = { volume = it },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isPlaying = !isPlaying }
                ) {
                    Text(if (isPlaying) "停止播放" else "开始播放")
                }

                OutlinedButton(
                    onClick = { isRecording = !isRecording }
                ) {
                    Text(if (isRecording) "停止录制" else "开始录制")
                }
            }
        }
    }
}
