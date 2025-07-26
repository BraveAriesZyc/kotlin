package com.zyc.core.ui.components.refreshview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.components.loading.TextLoaderImp
import com.zyc.core.ui.utils.refresh.CustomOverscrollEffect
import kotlinx.coroutines.launch

// 创建弹性滚动效果
@Composable
fun rememberBounceOverscrollEffect(): CustomOverscrollEffect {
    val coroutineScope = rememberCoroutineScope()
    return remember {
        CustomOverscrollEffect(coroutineScope, Orientation.Vertical)
    }
}

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
    bgColor: Color = MaterialTheme.colorScheme.background,
    content: LazyListScope.() -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val refreshingTips = getRefreshingTips(isRefreshing, pullToRefreshState)
    // 创建弹性滚动效果
    val bounceOverscrollEffect = rememberBounceOverscrollEffect()
    
    // 创建加载更多状态
    val loadMoreState = rememberLoadMoreState(
        enabled = { enableLoadMore && !isLoadingMore },
        onReachBottom = {
            onLoadMore?.invoke()
        }
    )
    
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
                .overscroll(bounceOverscrollEffect)
                .pullToRefresh(isRefreshing, pullToRefreshState, onRefresh = handleRefresh)
                .nestedScroll(loadMoreState.nestedScrollConnection)
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
            if (isRefreshing || pullToRefreshState.distanceFraction > 0.1f) {
                item {
                    val progress = pullToRefreshState.distanceFraction
                    val canRefresh = progress >= refreshThreshold
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp)
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
                                // 优化旋转缩放效果，与阈值联动
                                val scale = when {
                                    isRefreshing -> 1f
                                    canRefresh -> 1f
                                    progress >= 0.5f -> (progress * 0.8f + 0.4f).coerceIn(0.4f, 1f)
                                    else -> (progress * 1.2f + 0.2f).coerceIn(0.2f, 0.8f)
                                }
                                scaleX = scale
                                scaleY = scale
                                // 当接近阈值时添加轻微的脉冲效果
                                if (canRefresh && !isRefreshing) {
                                    val pulse = kotlin.math.sin(System.currentTimeMillis() * 0.01f) * 0.05f + 1f
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
                                // 优化文字淡入效果
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