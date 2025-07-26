package com.zyc.core.ui.components.refreshview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.components.loading.TextLoaderImp
import kotlinx.coroutines.launch

/**
 * 封装下拉刷新和加载更多的列表
 *
 * @param modifier 修饰符
 * @param onRefresh 刷新回调
 * @param onLoadMore 加载更多回调
 * @param isRefreshing 是否正在刷新
 * @param isLoadingMore 是否正在加载更多
 * @param enableLoadMore 是否启用加载更多
 * @param contentPadding 列表内容填充
 * @param listState 列表状态
 * @param content 列表内容
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ZRefreshView(
    modifier: Modifier = Modifier,
    onRefresh: (suspend () -> Unit)? = null,
    onLoadMore: (suspend () -> Unit)? = null,
    isRefreshing: Boolean = false,
    isLoadingMore: Boolean = false,
    enableLoadMore: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    listState: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val refreshingTips = getRefreshingTips(isRefreshing, pullToRefreshState)

    // 监听滚动到底部
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && enableLoadMore && !isLoadingMore && !isRefreshing) {
            onLoadMore?.invoke()
        }
    }

    // 定义下拉刷新的触发阈值
    val refreshThreshold = 0.8f // 需要下拉到80%才能触发刷新

    val handleRefresh: () -> Unit = {
        // 只有当下拉距离达到阈值时才触发刷新
        if (pullToRefreshState.distanceFraction >= refreshThreshold) {
            coroutineScope.launch {
                onRefresh?.invoke()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // LazyColumn 内容
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pullToRefresh(isRefreshing, pullToRefreshState, onRefresh = handleRefresh)
                .graphicsLayer {
                    // 让列表内容跟随下拉手势移动，使用更平滑的曲线
                    val positionalThreshold = PullToRefreshDefaults.PositionalThreshold.roundToPx()
                    val progress = pullToRefreshState.distanceFraction.coerceIn(0f, 1.5f)
                    // 使用缓动函数让移动更自然
                    val easedProgress = if (progress <= 1f) {
                        // 前半段使用二次缓动
                        progress * progress * (3f - 2f * progress)
                    } else {
                        // 超过阈值后减缓移动速度
                        1f + (progress - 1f) * 0.3f
                    }
                    translationY = (easedProgress * positionalThreshold * 0.4f).coerceAtLeast(0f)
                },
            state = listState,
            contentPadding = contentPadding
        ) {
            // 下拉刷新指示器 - 显示在列表上方
            if (isRefreshing || pullToRefreshState.distanceFraction > 0.05f) {
                item {
                    val progress = pullToRefreshState.distanceFraction
                    val canRefresh = progress >= refreshThreshold

                    // 计算指示器的高度，从0开始根据下拉距离增加
                    val indicatorHeight = when {
                        isRefreshing -> 48.dp // 刷新时固定高度
                        else -> {
                            val maxHeight = 48.dp
                            val heightProgress = (progress * 2f).coerceIn(0f, 1f) // 加速显示
                            maxHeight * heightProgress
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(indicatorHeight)
                            .padding(horizontal = 16.dp)
                            .graphicsLayer {
                                // 根据阈值调整透明度动画效果
                                alpha = when {
                                    isRefreshing -> 1f
                                    progress >= refreshThreshold -> 1f
                                    progress >= 0.3f -> (progress * 1.5f).coerceIn(0.7f, 1f)
                                    else -> (progress * 3f).coerceIn(0.3f, 0.7f)
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.graphicsLayer {
                                // 根据下拉距离进行缩放，设置合理的最大值
                                val baseScale = when {
                                    isRefreshing -> 1.2f // 刷新时保持较大尺寸
                                    else -> {
                                        // 基础缩放：从0.6开始，随下拉距离增加到最大1.5倍
                                        val minScale = 0.6f
                                        val maxScale = 1.5f
                                        val scaleFactor = progress.coerceIn(0f, 1.2f) // 限制进度范围
                                        (minScale + scaleFactor * (maxScale - minScale)).coerceIn(minScale, maxScale)
                                    }
                                }
                                scaleX = baseScale
                                scaleY = baseScale

                                // 当接近阈值时添加轻微的脉冲效果
                                if (canRefresh && !isRefreshing) {
                                    val pulse = kotlin.math.sin(System.currentTimeMillis() * 0.008f) * 0.08f + 1f
                                    scaleX *= pulse
                                    scaleY *= pulse
                                }
                            }
                        ) {
                            TextLoaderImp(isRotating = isRefreshing || canRefresh)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = refreshingTips,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = when {
                                    isRefreshing -> 1f
                                    canRefresh -> 1f
                                    progress >= 0.3f -> (progress * 1.2f).coerceIn(0.7f, 1f)
                                    else -> (progress * 2f).coerceIn(0.5f, 0.8f)
                                }
                            ),
                            fontSize = 14.sp,
                            modifier = Modifier.graphicsLayer {
                                // 文字缩放效果，与指示器保持一致
                                val textScale = when {
                                    isRefreshing -> 1.1f // 刷新时稍微放大
                                    else -> {
                                        // 基础缩放：从0.8开始，随下拉距离增加到最大1.2倍
                                        val minScale = 0.8f
                                        val maxScale = 1.2f
                                        val scaleFactor = progress.coerceIn(0f, 1.2f)
                                        (minScale + scaleFactor * (maxScale - minScale)).coerceIn(minScale, maxScale)
                                    }
                                }
                                scaleX = textScale
                                scaleY = textScale

                                // 优化文字透明度效果
                                alpha = when {
                                    isRefreshing -> 1f
                                    canRefresh -> 1f
                                    progress >= 0.2f -> (progress * 1.5f).coerceIn(0.6f, 1f)
                                    else -> (progress * 4f).coerceIn(0.3f, 0.8f)
                                }
                            }
                        )
                    }
                }
            }

            content()

            // 加载更多指示器
            if (isLoadingMore) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                            .graphicsLayer {
                                // 添加淡入动画
                                alpha = 0.9f
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.graphicsLayer {
                                scaleX = 0.9f
                                scaleY = 0.9f
                            }
                        ) {
                            TextLoaderImp(isRotating = true)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "加载更多...",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
         }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun getRefreshingTips(isRefreshing: Boolean, state: PullToRefreshState): String {
    val refreshThreshold = 0.8f
    return when {
        isRefreshing -> "刷新中..."
        state.distanceFraction >= refreshThreshold -> "释放立即刷新"
        state.distanceFraction >= 0.6f -> "继续下拉到${(refreshThreshold * 100).toInt()}%"
        state.distanceFraction >= 0.4f -> "下拉刷新 ${(state.distanceFraction * 100).toInt()}%"
        state.distanceFraction >= 0.2f -> "轻拉刷新"
        state.distanceFraction > 0f -> "下拉刷新"
        else -> "刷新中..."
    }
}
