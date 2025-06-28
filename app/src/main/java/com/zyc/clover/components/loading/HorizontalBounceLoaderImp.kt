package com.zyc.clover.components.loading

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.random.Random

/**
 * 水平加载动画
 * 特点：多个圆点展示，使用旋转和渐变效果，无跳动
 */
@Composable
internal fun HorizontalLoaderImp(
    modifier: Modifier = Modifier,
    dotCount: Int = 5,
    dotSize: Dp = 12.dp,
    spaceBetweenDots: Dp = 8.dp,
    animationDuration: Int = 200,
    baseColor: Color = MaterialTheme.colorScheme.primary,
    useRandomColors: Boolean = false,
    dotShape: Shape = CircleShape,
    useRotation: Boolean = true,     // 是否使用旋转效果
    useGradient: Boolean = true,     // 是否使用渐变填充
    usePulseEffect: Boolean = true,  // 是否使用脉冲效果
) {
    // 使用单个无限过渡
    val infiniteTransition = rememberInfiniteTransition()

    // 旋转动画 - 使用正弦函数实现平滑循环
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    // 生成颜色列表
    val colors = remember {
        if (useRandomColors) {
            generateNonAdjacentColors(dotCount, baseColor)
        } else {
            List(dotCount) { baseColor }
        }
    }

    // 每个点的脉冲动画 - 使用无限循环动画替代关键帧
    val pulseFractions = List(dotCount) { index ->
        val phase = index.toFloat() / dotCount
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = animationDuration,
                    delayMillis = (phase * animationDuration).toInt(),
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            colors.forEachIndexed { index, color ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = spaceBetweenDots / 2)
                        .size(dotSize)
                        .graphicsLayer {
                            // 应用脉冲缩放效果
                            scaleX = pulseFractions[index].value
                            scaleY = pulseFractions[index].value
                        }
                        .clip(dotShape)
                        .then(
                            if (useGradient) {
                                // 修改渐变颜色，使用更明显的对比色
                                Modifier.background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            color.copy(alpha = 0.8f), // 降低透明度，使颜色更明显
                                            getComplementaryColor(color) // 使用互补色作为渐变的另一端
                                        ),
                                        start = Offset(0f, 0f),
                                        end = Offset(dotSize.value, dotSize.value)
                                    )
                                )
                            } else {
                                Modifier.background(color = color)
                            }
                        )
                )
            }
        }

        // 如果启用旋转，整个组件旋转
        if (useRotation) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .rotate(rotationAngle)
                    .graphicsLayer(alpha = 0.0f) // 不可见但参与布局
            )
        }
    }
}

/**
 * 生成不相邻的随机颜色列表
 */
private fun generateNonAdjacentColors(count: Int, baseColor: Color): List<Color> {
    // 如果只有1个或2个点，直接返回不同的颜色
    if (count <= 2) {
        return when (count) {
            1 -> listOf(baseColor)
            2 -> listOf(baseColor, baseColor.copy(red = 1f - baseColor.red))
            else -> emptyList()
        }
    }

    // 生成更丰富的颜色集合
    val baseColors = mutableListOf<Color>()

    // 添加基础颜色的变化
    baseColors.add(baseColor)
    baseColors.add(baseColor.copy(red = (baseColor.red + 0.3f) % 1f))
    baseColors.add(baseColor.copy(green = (baseColor.green + 0.3f) % 1f))
    baseColors.add(baseColor.copy(blue = (baseColor.blue + 0.3f) % 1f))

    // 添加更明亮和更暗的变体
    baseColors.add(baseColor.copy(alpha = 0.8f))
    baseColors.add(baseColor.copy(alpha = 0.6f))
    baseColors.add(baseColor.copy(red = (baseColor.red * 0.7f), green = (baseColor.green * 0.7f), blue = (baseColor.blue * 0.7f)))

    // 如果需要更多颜色，生成更多随机变体
    while (baseColors.size < count + 2) {
        val hue = (Random.nextFloat() * 360).toInt()
        val saturation = 0.7f + Random.nextFloat() * 0.3f
        val lightness = 0.5f + Random.nextFloat() * 0.3f

        baseColors.add(hslToColor(hue, saturation, lightness))
    }

    val result = mutableListOf<Color>()

    // 随机选择第一个颜色
    var prevColor = baseColors.random()
    result.add(prevColor)

    // 为每个后续点选择与前一个不同的颜色
    repeat(count - 1) {
        val availableColors = baseColors.filter {
            // 不仅要避免颜色相同，还要避免过于相似
            !isColorSimilar(it, prevColor)
        }
        val nextColor = availableColors.random()
        result.add(nextColor)
        prevColor = nextColor
    }

    return result
}

/**
 * HSL颜色转RGB颜色
 */
private fun hslToColor(h: Int, s: Float, l: Float): Color {
    val c = (1 - abs(2 * l - 1)) * s
    val x = c * (1 - abs((h / 60) % 2 - 1))
    val m = l - c / 2

    val (r1, g1, b1) = when (h) {
        in 0..59 -> Triple(c, x, 0f)
        in 60..119 -> Triple(x, c, 0f)
        in 120..179 -> Triple(0f, c, x)
        in 180..239 -> Triple(0f, x, c)
        in 240..299 -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }

    return Color(
        red = ((r1 + m) * 255).toInt(),
        green = ((g1 + m) * 255).toInt(),
        blue = ((b1 + m) * 255).toInt()
    )
}

/**
 * 判断两个颜色是否相似
 */
private fun isColorSimilar(color1: Color, color2: Color): Boolean {
    val deltaR = (color1.red - color2.red) * 255
    val deltaG = (color1.green - color2.green) * 255
    val deltaB = (color1.blue - color2.blue) * 255

    // 计算欧几里得距离
    val distance = kotlin.math.sqrt(deltaR * deltaR + deltaG * deltaG + deltaB * deltaB)

    // 如果距离小于阈值，则认为颜色相似
    return distance < 80
}

/**
 * 获取颜色的互补色
 */
private fun getComplementaryColor(color: Color): Color {
    return Color(
        red = 1.0f - color.red,
        green = 1.0f - color.green,
        blue = 1.0f - color.blue
    )
}
