package com.zyc.core.ui.components.refreshview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Stable
interface LoadMoreState {
    /**
     * 是否在加载更多
     */
    val isLoadingMore: Boolean

    /**
     * 滚动协调器
     */
    val nestedScrollConnection: NestedScrollConnection
    
    /**
     * 手动设置加载状态
     */
    fun setLoadingMore(loading: Boolean)
}

@Composable
fun rememberLoadMoreState(
    enabled: () -> Boolean = { true },
    onReachBottom: suspend () -> Unit
): LoadMoreState {
    val coroutineScope = rememberCoroutineScope()

    return remember {
        LoadMoreStateImpl(enabled, onReachBottom, coroutineScope)
    }
}

private class LoadMoreStateImpl(
    private val enabled: () -> Boolean,
    private val onReachBottom: suspend () -> Unit,
    private val coroutineScope: CoroutineScope
) : LoadMoreState {
    private var _isLoadingMore by mutableStateOf(false)
    override val isLoadingMore: Boolean get() = _isLoadingMore
    
    private var isTriggering = false
    
    override val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            return Offset.Zero
        }
        
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            // 检测向上滚动到底部
             if (available.y < 0 && enabled() && !_isLoadingMore && !isTriggering) {
                 triggerLoadMore()
             }
            return Offset.Zero
        }
        
        override suspend fun onPostFling(
            consumed: Velocity,
            available: Velocity
        ): Velocity {
            // 检测快速向上滑动到底部
             if (available.y < -100 && enabled() && !_isLoadingMore && !isTriggering) {
                 triggerLoadMore()
             }
            return Velocity.Zero
        }
    }
    
    private fun triggerLoadMore() {
        if (isTriggering || isLoadingMore || !enabled()) return
        
        isTriggering = true
        coroutineScope.launch {
            try {
                _isLoadingMore = true
                delay(100) // 防抖延迟
                onReachBottom()
            } finally {
                _isLoadingMore = false
                isTriggering = false
            }
        }
    }
    
    override fun setLoadingMore(loading: Boolean) {
         _isLoadingMore = loading
         if (!loading) {
             isTriggering = false
         }
     }
}