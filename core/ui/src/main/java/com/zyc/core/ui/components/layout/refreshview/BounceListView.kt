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
 * 带回弹效果的列表，支持下拉回弹和上拉阻尼
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

    // 下拉回弹偏移量状态 - 使用State确保同步更新
    var topBounceOffset by remember { mutableFloatStateOf(0f) }
    // 上拉阻尼偏移量状态
    var bottomBounceOffset by remember { mutableFloatStateOf(0f) }
    // 用于动画的Animatable
    val topBounceAnimatable = remember { Animatable(0f) }
    val bottomBounceAnimatable = remember { Animatable(0f) }
    // 动画是否正在执行的状态
    var isTopAnimating by remember { mutableStateOf(false) }
    var isBottomAnimating by remember { mutableStateOf(false) }
    
    // 优化：使用derivedStateOf计算总偏移量，减少重组
    val totalOffset by remember {
        derivedStateOf { topBounceOffset + bottomBounceOffset }
    }
    
    // 优化：提取动画停止逻辑为内联函数
    val stopTopAnimation = {
        if (isTopAnimating) {
            isTopAnimating = false
            coroutineScope.launch { topBounceAnimatable.stop() }
        }
    }
    
    val stopBottomAnimation = {
        if (isBottomAnimating) {
            isBottomAnimating = false
            coroutineScope.launch { bottomBounceAnimatable.stop() }
        }
    }
    
    // 优化：常量提取，避免重复计算
    val resistance = 0.3f
    val animationStiffness = 500f // 提高刚度，减少动画时间
    val dampingRatio = 0.85f // 稍微降低阻尼，让动画更快结束

    // 嵌套滚动连接器
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // 当有向上滚动且当前有下拉回弹偏移时，先消耗下拉回弹偏移
                if (available.y < 0 && topBounceOffset > 0) {
                    stopTopAnimation()
                    val consumed = minOf(-available.y, topBounceOffset)
                    topBounceOffset = (topBounceOffset - consumed).coerceAtLeast(0f)
                    return Offset(0f, -consumed)
                }
                // 当有向下滚动且当前有上拉阻尼偏移时，先消耗上拉阻尼偏移
                if (available.y > 0 && bottomBounceOffset < 0) {
                    stopBottomAnimation()
                    val consumed = minOf(available.y, -bottomBounceOffset)
                    bottomBounceOffset = (bottomBounceOffset + consumed).coerceAtMost(0f)
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // 当列表已经滚动到顶部且还有向下的滚动量时，产生下拉回弹效果
                if (available.y > 0) {
                    val isAtTop = listState.firstVisibleItemIndex == 0 &&
                                 listState.firstVisibleItemScrollOffset == 0
                    if (isAtTop) {
                        stopTopAnimation()
                        val bounceAmount = available.y * resistance
                        topBounceOffset = (topBounceOffset + bounceAmount).coerceAtLeast(0f)
                        return Offset(0f, available.y)
                    }
                }
                
                // 当列表已经滚动到底部且还有向上的滚动量时，产生上拉阻尼效果
                if (available.y < 0) {
                    val layoutInfo = listState.layoutInfo
                    val isAtBottom = layoutInfo.visibleItemsInfo.lastOrNull()?.let { lastItem ->
                        lastItem.index == layoutInfo.totalItemsCount - 1 &&
                        lastItem.offset + lastItem.size <= layoutInfo.viewportEndOffset
                    } ?: false
                    
                    if (isAtBottom) {
                        stopBottomAnimation()
                        val bounceAmount = available.y * resistance
                        bottomBounceOffset = (bottomBounceOffset + bounceAmount).coerceAtMost(0f)
                        return Offset(0f, available.y)
                    }
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                // 松手时下拉回弹到原位置
                if (topBounceOffset > 0 && !isTopAnimating) {
                    isTopAnimating = true
                    coroutineScope.launch {
                        try {
                            // 同步topBounceAnimatable的起始值
                            topBounceAnimatable.snapTo(topBounceOffset)
                            // 执行动画并同步更新topBounceOffset
                            topBounceAnimatable.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(
                                    dampingRatio = dampingRatio,
                                    stiffness = animationStiffness
                                )
                            ) { 
                                // 动画过程中同步更新topBounceOffset
                                topBounceOffset = value
                            }
                        } catch (e: Exception) {
                            // 如果动画被中断，直接设置为0
                            topBounceOffset = 0f
                        } finally {
                            // 确保动画完成后状态为0
                            topBounceOffset = 0f
                            isTopAnimating = false
                        }
                    }
                }
                
                // 松手时上拉阻尼回弹到原位置
                if (bottomBounceOffset < 0 && !isBottomAnimating) {
                    isBottomAnimating = true
                    coroutineScope.launch {
                        try {
                            // 同步bottomBounceAnimatable的起始值
                            bottomBounceAnimatable.snapTo(bottomBounceOffset)
                            // 执行动画并同步更新bottomBounceOffset
                            bottomBounceAnimatable.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(
                                    dampingRatio = dampingRatio,
                                    stiffness = animationStiffness
                                )
                            ) { 
                                // 动画过程中同步更新bottomBounceOffset
                                bottomBounceOffset = value
                            }
                        } catch (e: Exception) {
                            // 如果动画被中断，直接设置为0
                            bottomBounceOffset = 0f
                        } finally {
                            // 确保动画完成后状态为0
                            bottomBounceOffset = 0f
                            isBottomAnimating = false
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
                translationY = totalOffset
            },
        contentPadding = contentPadding
    ) {
        content()
    }
}
