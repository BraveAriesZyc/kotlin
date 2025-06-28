package com.zyc.clover.components.loading


import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * 加载中动画图标
 *
 * @param size 大小
 * @param color 颜色
 * @param isRotating 是否旋转
 */
@Composable
fun TextLoaderImp(size: Dp = 16.dp, color: Color = Color.Unspecified, isRotating: Boolean = true) {
    val angle by if (isRotating) {
        val transition = rememberInfiniteTransition(label = "")
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 1000, easing = LinearEasing),
                RepeatMode.Restart
            ),
            label = "WeLoadingAnimation"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    Icon(
        imageVector = Icons.Default.KeyboardArrowUp,
        contentDescription = "loading",
        modifier = Modifier
            .size(size)
            .rotate(angle),
        tint = color
    )
}

@Composable
fun WeLoadingMP(
    color: Color = MaterialTheme.colorScheme.outline,
    animationSpec: DurationBasedAnimationSpec<Float> = tween(durationMillis = 1000)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val currentIndex by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = animationSpec,
            repeatMode = RepeatMode.Restart
        ),
        label = "WeLoadingMPAnimation"
    )

    Canvas(modifier = Modifier.size(width = 44.dp, height = 20.dp)) {
        val dotRadius = 4.dp.toPx()
        val spacing = (size.width - 2 * dotRadius) / 2

        repeat(3) { index ->
            val isActive = index == currentIndex.roundToInt()
            val dotColor = color.copy(alpha = if (isActive) 0.8f else 0.4f)
            val center = Offset(
                x = dotRadius + spacing * index,
                y = size.height / 2
            )

            drawCircle(
                color = dotColor,
                radius = dotRadius,
                center = center
            )
        }
    }
}

@Composable
fun MiLoadingWeb(color: Color = MaterialTheme.colorScheme.primary) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animations = List(3) { index ->
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 400,
                    delayMillis = index * 100,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "MiLoadingWebAlphaAnimation"
        )
        val scaleY by infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 400,
                    delayMillis = index * 100,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "MiLoadingWebScaleAnimation"
        )
        Pair(alpha, scaleY)
    }

    Canvas(modifier = Modifier.size(24.dp)) {
        animations.forEachIndexed { index, item ->
            val strokeWidth = 4.dp.toPx()
            val spacing = (size.width - (3 * strokeWidth)) / 2

            scale(scaleX = 1f, scaleY = item.second) {
                drawLine(
                    color = color.copy(alpha = item.first),
                    start = Offset(
                        x = strokeWidth / 2 + (strokeWidth + spacing) * index,
                        y = 0f
                    ),
                    end = Offset(
                        x = strokeWidth / 2 + (strokeWidth + spacing) * index,
                        y = size.height
                    ),
                    strokeWidth
                )
            }
        }
    }
}

@Composable
fun MiLoadingMobile(
    borderColor: Color = MaterialTheme.colorScheme.onPrimary,
    dotColor: Color = borderColor,
    animationSpec: DurationBasedAnimationSpec<Float> = tween(
        durationMillis = 1200,
        easing = LinearEasing
    )
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val angle = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = animationSpec,
            repeatMode = RepeatMode.Restart
        ),
        label = "MiLoadingMobileAnimation"
    )

    Canvas(
        modifier = Modifier
            .size(34.dp)
            .border(2.dp, borderColor, CircleShape)
    ) {
        val circleRadius = size.minDimension / 2 - 8.dp.toPx()
        val dotRadius = 4.dp.toPx()
        val center = size.center
        val dotX = cos(Math.toRadians(angle.value.toDouble())) * circleRadius + center.x
        val dotY = sin(Math.toRadians(angle.value.toDouble())) * circleRadius + center.y

        drawCircle(dotColor, radius = dotRadius, center = Offset(dotX.toFloat(), dotY.toFloat()))
    }
}

@Composable
fun DotDanceLoading(color: Color = MaterialTheme.colorScheme.primary) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offsetY = LocalDensity.current.run { 3.dp.toPx() }
    val animations = List(3) { index ->
        val value by infiniteTransition.animateFloat(
            initialValue = -offsetY,
            targetValue = offsetY,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 400,
                    delayMillis = index * 100,
                    easing = FastOutLinearInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "DotDanceLoadingAnimation"
        )
        value
    }

    Canvas(modifier = Modifier.size(width = 44.dp, height = 20.dp)) {
        val dotRadius = 4.dp.toPx()
        val spacing = (size.width - 2 * dotRadius) / 2

        animations.forEachIndexed { index, item ->
            val center = Offset(
                x = dotRadius + spacing * index,
                y = size.height / 2 - item
            )

            drawCircle(
                color,
                radius = dotRadius,
                center = center
            )
        }
    }
}

enum class LoadMoreType {
    LOADING, // 加载中
    EMPTY_DATA, // 无数据
    ALL_LOADED // 全部加载完成
}

@Composable
fun WeLoadMore(
    modifier: Modifier = Modifier,
    type: LoadMoreType = LoadMoreType.LOADING,
    listState: LazyListState? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (type) {
            LoadMoreType.LOADING -> {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "正在加载...",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp
                )

                if (listState != null) {
                    LaunchedEffect(Unit) {
                        listState.scrollToItem(listState.layoutInfo.totalItemsCount)
                    }
                }
            }

            LoadMoreType.EMPTY_DATA -> {

                Text(
                    text = "暂无数据",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

            }

            LoadMoreType.ALL_LOADED -> {

                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(4.dp)
                        .background(MaterialTheme.colorScheme.outline, CircleShape)
                )

            }
        }
    }
}
