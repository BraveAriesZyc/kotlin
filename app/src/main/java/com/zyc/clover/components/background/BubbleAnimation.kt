package com.zyc.clover.components.background

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun BubbleAnimationScreen(
    modifier: Modifier = Modifier,
    maxBubbles: Int = 30,
    minRadius: Float = 40f,
    maxRadius: Float = 80f,
    animationSpeed: Float = 1f,
    gravity: Float = 0f,
    horizontalWind: Float = 0f,
    bubbleAlpha: Float = 0.7f,
    // 新增参数：控制气泡生成的垂直范围（0f=底部，1f=顶部）
    spawnYRange: ClosedFloatingPointRange<Float> = 0.9f..1.0f,
    // 新增参数：是否允许气泡从顶部进入（模拟下落效果）
    enableFallFromTop: Boolean = false
) {
    var bubbles by remember { mutableStateOf(emptyList<Bubble>()) }
    val isInEditMode = LocalInspectionMode.current

    // 存储画布尺寸
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    // 预览模式下使用固定泡泡数据
    if (isInEditMode) {
        bubbles = remember {
            List(5) { index ->
                Bubble(
                    position = Offset(
                        x = (index * 100 + 50).toFloat(),
                        y = (index * 80 + 100).toFloat()
                    ),
                    velocity = Offset.Zero,
                    radius = minRadius + (maxRadius - minRadius) * index / 4,
                    color = Color(
                        red = (index * 0.2f).coerceIn(0.2f, 0.8f),
                        green = (1f - index * 0.2f).coerceIn(0.2f, 0.8f),
                        blue = 0.5f,
                        alpha = bubbleAlpha
                    )
                )
            }
        }
    } else {
        // 运行时动态生成泡泡
        LaunchedEffect(Unit) {
            while (true) {
                delay((800 / animationSpeed).toLong())

                // 控制最大气泡数量
                if (bubbles.size < maxBubbles && canvasSize != Size.Zero) {
                    // 计算生成位置的Y坐标
                    val spawnY = if (enableFallFromTop) {
                        // 从顶部随机位置生成（模拟下落）
                        Random.nextFloat() * canvasSize.height
                    } else {
                        // 在指定垂直范围内生成（默认底部附近）
                        spawnYRange.start * canvasSize.height +
                                Random.nextFloat() * (spawnYRange.endInclusive - spawnYRange.start) * canvasSize.height
                    }

                    val newBubble = Bubble(
                        position = Offset(
                            x = Random.nextFloat() * canvasSize.width,
                            y = spawnY
                        ),
                        velocity = Offset(
                            x = (Random.nextFloat() - 0.5f) * 200f * animationSpeed + horizontalWind,
                            y = if (enableFallFromTop) {
                                // 下落时垂直速度向下（正数）
                                Random.nextFloat() * 200f * animationSpeed + 100f * animationSpeed
                            } else {
                                // 上升时垂直速度向上（负数）
                                -Random.nextFloat() * 300f * animationSpeed - 100f * animationSpeed
                            }
                        ),
                        radius = minRadius + Random.nextFloat() * (maxRadius - minRadius),
                        color = Color(
                            red = Random.nextFloat(),
                            green = Random.nextFloat(),
                            blue = Random.nextFloat(),
                            alpha = bubbleAlpha
                        )
                    )

                    bubbles = bubbles + newBubble
                }
            }
        }

        // 动画更新逻辑
        LaunchedEffect(Unit) {
            val frameDuration = (16 / animationSpeed).toLong() // 约60fps，根据速度调整

            while (true) {
                delay(frameDuration)

                if (canvasSize != Size.Zero) {
                    bubbles = bubbles.mapNotNull { bubble ->
                        // 应用重力和风力
                        val gravityEffect = Offset(0f, gravity * animationSpeed)
                        val windEffect = Offset(horizontalWind * animationSpeed, 0f)

                        val newVelocity = bubble.velocity + gravityEffect + windEffect
                        val newPosition = bubble.position + newVelocity * frameDuration.toFloat() / 1000f

                        // 水平边界碰撞
                        val newVelocityX = when {
                            newPosition.x - bubble.radius < 0 -> abs(newVelocity.x) * 0.8f
                            newPosition.x + bubble.radius > canvasSize.width -> -abs(newVelocity.x) * 0.8f
                            else -> newVelocity.x
                        }

                        // 垂直边界碰撞
                        val newVelocityY = when {
                            newPosition.y - bubble.radius < 0 -> abs(newVelocity.y) * 0.8f
                            newPosition.y + bubble.radius > canvasSize.height -> -abs(newVelocity.y) * 0.8f
                            else -> newVelocity.y
                        }

                        // 移除超出屏幕的气泡
                        if (newPosition.y < -bubble.radius * 2 ||
                            newPosition.x < -bubble.radius * 2 ||
                            newPosition.x > canvasSize.width + bubble.radius * 2) {
                            null
                        } else {
                            bubble.copy(
                                position = newPosition,
                                velocity = Offset(newVelocityX, newVelocityY)
                            )
                        }
                    }
                }
            }
        }
    }

    // 使用Canvas绘制泡泡
    Canvas(modifier = modifier.fillMaxSize()) {
        // 保存画布尺寸
        if (canvasSize == Size.Zero) {
            canvasSize = size
        }

        // 绘制所有泡泡
        bubbles.forEach { bubble ->
            drawBubbleWithHighlight(
                color = bubble.color,
                radius = bubble.radius,
                center = bubble.position
            )
        }
    }
}

// 气泡数据类
data class Bubble(
    val position: Offset,
    val velocity: Offset,
    val radius: Float,
    val color: Color
)

// 绘制带高光效果的泡泡 - 优化了高光位置计算
private fun DrawScope.drawBubbleWithHighlight(
    color: Color,
    radius: Float,
    center: Offset
) {
    // 主气泡渐变
    val bubbleGradient = Brush.radialGradient(
        colors = listOf(
            color.copy(alpha = color.alpha * 0.8f),
            color.copy(alpha = color.alpha * 0.2f)
        ),
        center = center,
        radius = radius
    )

    // 绘制主气泡
    drawCircle(
        brush = bubbleGradient,
        radius = radius,
        center = center
    )

    // 高光大小和位置 - 基于画布位置计算
    val highlightSize = radius * 0.4f
    val highlightOffset = Offset(
        x = center.x - radius * 0.3f,
        y = center.y - radius * 0.3f
    )

    // 高光渐变
    val highlightGradient = Brush.radialGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.9f),
            Color.White.copy(alpha = 0.0f)
        ),
        center = highlightOffset,
        radius = highlightSize
    )

    // 绘制高光
    drawCircle(
        brush = highlightGradient,
        radius = highlightSize,
        center = highlightOffset
    )

    // 反光效果 - 优化了反光大小和位置
    val reflectionWidth = radius * 0.7f
    val reflectionHeight = radius * 0.3f

    rotate(degrees = 20f, pivot = center) {
        drawOval(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.3f),
                    Color.White.copy(alpha = 0.0f)
                ),
                start = Offset(center.x - reflectionWidth/2, center.y),
                end = Offset(center.x + reflectionWidth/2, center.y)
            ),
            topLeft = Offset(center.x - reflectionWidth/2, center.y - reflectionHeight/2),
            size = Size(reflectionWidth, reflectionHeight)
        )
    }
}
