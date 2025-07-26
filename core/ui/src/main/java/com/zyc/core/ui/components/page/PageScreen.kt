package com.zyc.core.ui.components.page

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import java.lang.Math.toDegrees
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot
data class PageScreenData(
    val pageContents: List<@Composable () -> Unit>,
    val onPageChange: (Int) -> Unit = {},
    val pagerState: PagerState,
    val maxAllowedAngle: Float = 10f // 最大允许角度，0度为纯水平
)

@Composable
fun PageScreen(data: PageScreenData) {
    var startX by remember { mutableFloatStateOf(0f) }
    var startY by remember { mutableFloatStateOf(0f) }
    var isHorizontalSwipe by remember { mutableStateOf<Boolean?>(null) } // null: 未判断, true: 水平, false: 非水平
    val velocityTracker = remember { VelocityTracker() }

    // 页面变化回调
    LaunchedEffect(data.pagerState.currentPage) {
        data.onPageChange(data.pagerState.currentPage)
    }

    // 使用自定义触摸处理修饰符，确保优先判断
    val swipeModifier = Modifier.pointerInput(data.maxAllowedAngle) {
        detectDragGestures(
            onDragStart = { offset ->
                startX = offset.x
                startY = offset.y
                isHorizontalSwipe = null // 重置判断状态
                velocityTracker.resetTracking()
            },
            onDrag = { change, _ ->
                velocityTracker.addPosition(change.uptimeMillis, change.position)

                // 仅在未判断状态时进行角度检测
                if (isHorizontalSwipe == null) {
                    val dx = change.position.x - startX
                    val dy = change.position.y - startY
                    val totalDistance = hypot(dx, dy)

                    // 滑动超过一定距离才进行判断
                    if (totalDistance > 15f) {
                        // 计算水平和垂直分量的比例
                        val horizontalRatio = abs(dx) / totalDistance
                        // 计算角度（与水平线的夹角）
                        val angle = toDegrees(atan2(abs(dy).toDouble(), abs(dx).toDouble())).toFloat()

                        // 双重判断：角度和水平分量比例
                        isHorizontalSwipe = angle < data.maxAllowedAngle && horizontalRatio > 0.95f
                    }
                }

                // 如果判断为非水平滑动，消费事件阻止Pager响应
                if (isHorizontalSwipe == false) {
                    change.consume()
                }
            },
            onDragEnd = {
                isHorizontalSwipe = null // 重置判断状态
            }
        )
    }

    HorizontalPager(
        state = data.pagerState,
        modifier = Modifier
            .fillMaxSize()
            .then(swipeModifier) // 应用自定义滑动判断
    ) { page ->
        data.pageContents[page]()
    }
}
