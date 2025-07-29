package com.zyc.core.ui.components.feedback.progress

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun CustomProgressBar(
    progress: Float, // 0.0 到 1.0
    onProgressChange: (Float) -> Unit,
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {},
    modifier: Modifier = Modifier,
    trackColor: Color = Color.White.copy(alpha = 0.3f),
    progressColor: Color = Color.White,
    thumbColor: Color = Color.White,
    bufferColor: Color = Color.White.copy(alpha = 0.5f),
    bufferProgress: Float = 0f, // 缓冲进度 0.0 到 1.0
    trackHeight: Float = 2f, // dp
    thumbRadius: Float = 4f, // dp
    showTimeOnDrag: Boolean = true,
    currentTime: String = "",
    totalTime: String = ""
) {
    val density = LocalDensity.current
    var isDragging by remember { mutableStateOf(false) }
    var dragProgress by remember { mutableFloatStateOf(progress) }

    // 当外部progress变化时，更新内部状态（仅在非拖拽状态下）
    LaunchedEffect(progress) {
        if (!isDragging) {
            dragProgress = progress
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp) // 固定高度，不会影响布局
    ) {
        // 时间显示 - 只在拖拽时显示，使用绝对定位
        AnimatedVisibility(
            visible = isDragging && showTimeOnDrag,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-30).dp) // 向上偏移，不占用布局空间
        ) {
            Row(
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentTime,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    " / ",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = totalTime,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    // 自定义进度条
    Canvas(
        modifier = Modifier
            .height(4.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    // 点击时直接跳转到对应位置
                    val newProgress = (offset.x / size.width).coerceIn(0f, 1f)
                    dragProgress = newProgress
                    onProgressChange(newProgress)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        onDragStart()
                        // 计算初始位置的进度
                        val newProgress = (offset.x / size.width).coerceIn(0f, 1f)
                        dragProgress = newProgress
                        onProgressChange(newProgress)
                    },
                    onDragEnd = {
                        isDragging = false
                        onDragEnd()
                    },
                    onDrag = { change, _ ->
                        // 根据当前触摸位置计算进度
                        val newProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                        dragProgress = newProgress
                        onProgressChange(newProgress)
                    }
                )
            }
    ) {
        val trackHeightPx = with(density) { trackHeight.dp.toPx() }
        val thumbRadiusPx = with(density) { thumbRadius.dp.toPx() }
        val centerY = size.height / 2f

        // 绘制背景轨道
        drawLine(
            color = trackColor,
            start = Offset(thumbRadiusPx, centerY),
            end = Offset(size.width - thumbRadiusPx, centerY),
            strokeWidth = trackHeightPx,
            cap = StrokeCap.Round
        )

        // 绘制缓冲进度轨道
        val bufferWidth = (size.width - 2 * thumbRadiusPx) * bufferProgress
        if (bufferWidth > 0) {
            drawLine(
                color = bufferColor,
                start = Offset(thumbRadiusPx, centerY),
                end = Offset(thumbRadiusPx + bufferWidth, centerY),
                strokeWidth = trackHeightPx,
                cap = StrokeCap.Round
            )
        }

        // 绘制播放进度轨道
        val progressWidth = (size.width - 2 * thumbRadiusPx) * dragProgress
        if (progressWidth > 0) {
            drawLine(
                color = progressColor,
                start = Offset(thumbRadiusPx, centerY),
                end = Offset(thumbRadiusPx + progressWidth, centerY),
                strokeWidth = trackHeightPx,
                cap = StrokeCap.Round
            )
        }

        // 绘制滑块
        val thumbX = thumbRadiusPx + (size.width - 2 * thumbRadiusPx) * dragProgress
        drawCircle(
            color = thumbColor,
            radius = thumbRadiusPx,
            center = Offset(thumbX, centerY)
        )
    }
}
