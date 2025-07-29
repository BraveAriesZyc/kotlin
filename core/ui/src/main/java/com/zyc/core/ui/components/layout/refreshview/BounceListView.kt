package com.zyc.core.ui.components.layout.refreshview

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.launch

/**
 * 带回弹效果的列表
 *
 * @param modifier 修饰符
 * @param contentPadding 内边距
 * @param listState 列表状态
 * @param content 列表内容
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BounceListView(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    listState: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    // 回弹偏移量状态 - 使用State确保同步更新
    var bounceOffset by remember { mutableFloatStateOf(0f) }
    // 用于动画的Animatable
    val bounceAnimatable = remember { Animatable(0f) }

    // 嵌套滚动连接器
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // 当有向上滚动且当前有回弹偏移时，先消耗回弹偏移
                if (available.y < 0 && bounceOffset > 0) {
                    val consumed = minOf(-available.y, bounceOffset)
                    bounceOffset = (bounceOffset - consumed).coerceAtLeast(0f)
                    return Offset(0f, -consumed)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // 当列表已经滚动到顶部且还有向下的滚动量时，产生回弹效果
                if (available.y > 0) {
                    val isAtTop = listState.firstVisibleItemIndex == 0 &&
                                 listState.firstVisibleItemScrollOffset == 0
                    if (isAtTop) {
                        val resistance = 0.3f
                        val bounceAmount = available.y * resistance
                        bounceOffset = (bounceOffset + bounceAmount).coerceAtLeast(0f)
                        return Offset(0f, available.y)
                    }
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                // 松手时回弹到原位置
                if (bounceOffset > 0) {
                    coroutineScope.launch {
                        try {
                            // 同步bounceAnimatable的起始值
                            bounceAnimatable.snapTo(bounceOffset)
                            // 执行动画并同步更新bounceOffset
                            bounceAnimatable.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(
                                    dampingRatio = 0.9f,
                                    stiffness = 350f
                                )
                            ) { 
                                // 动画过程中同步更新bounceOffset
                                bounceOffset = value
                            }
                            // 确保动画完成后状态为0
                            bounceOffset = 0f
                        } catch (e: Exception) {
                            // 如果动画被中断，直接设置为0
                            bounceOffset = 0f
                        }
                    }
                }
                return Velocity.Zero
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .graphicsLayer {
                translationY = bounceOffset
            },
        contentPadding = contentPadding
    ) {
        content()
    }
}
