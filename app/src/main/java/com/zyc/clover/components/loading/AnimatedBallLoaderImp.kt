package com.zyc.clover.components.loading

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun AnimatedBallLoaderImp(r: Dp = 12.dp) {
    // 定义颜色列表
    val colors = listOf(
        Color(0xFF800080),
        Color(0xFF0000FF),
        Color(0xFFFF0000),
        Color(0xFF008000),
        Color(0xFFFFFF00)
    )

    // 创建无限循环的动画过渡
    val infiniteTransition = rememberInfiniteTransition()
    // 创建动画，从 0 到 1，时长 1 秒，线性插值，无限重复并反转
    val animation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        // 遍历颜色列表
        colors.forEachIndexed { index, color ->

            Spacer(modifier = Modifier.width(1.dp))
            // 计算缩放比例
            val scale = sin((animation * 2 * PI + index * PI / 2.5).toFloat()) * 0.5f + 0.5f


            Box(
                modifier = Modifier
                    .size(r)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.5f)),
            )
            Spacer(modifier = Modifier.width(3.dp))
        }
    }
}
