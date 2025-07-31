package com.zyc.feature.common_page.components.slidedrawer

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 侧边栏基础ViewModel，管理拖拽状态和偏移量
 */
class BaseDrawerViewModel : ViewModel() {
    
    // 左侧抽屉状态
    private var _isLeftDrawerOpen = MutableStateFlow(false)
    val isLeftDrawerOpen: StateFlow<Boolean> = _isLeftDrawerOpen
    
    private var _isRightDrawerOpen = MutableStateFlow(false)
    val isRightDrawerOpen: StateFlow<Boolean> = _isRightDrawerOpen
    
    // 抽屉拖拽偏移量
    private var _leftDrawerOffset by mutableFloatStateOf(0f)
    val leftDrawerOffset: Float get() = _leftDrawerOffset
    
    private var _rightDrawerOffset by mutableFloatStateOf(0f)
    val rightDrawerOffset: Float get() = _rightDrawerOffset
    
    // 拖拽状态
    private var _isLeftDragging by mutableStateOf(false)
    val isLeftDragging: Boolean get() = _isLeftDragging
    
    private var _isRightDragging by mutableStateOf(false)
    val isRightDragging: Boolean get() = _isRightDragging
    
    // 左侧抽屉控制
    fun openLeftDrawer() {
        _isLeftDrawerOpen.value = true
        _isRightDrawerOpen.value = false // 确保只有一个抽屉打开
    }
    
    fun closeLeftDrawer() {
        _isLeftDrawerOpen.value = false
    }
    
    fun toggleLeftDrawer() {
        _isLeftDrawerOpen.value = !_isLeftDrawerOpen.value
        if (_isLeftDrawerOpen.value) {
            _isRightDrawerOpen.value = false
        }
    }
    
    // 右侧抽屉控制
    fun openRightDrawer() {
        _isRightDrawerOpen.value = true
        _isLeftDrawerOpen.value = false // 确保只有一个抽屉打开
    }
    
    fun closeRightDrawer() {
        _isRightDrawerOpen.value = false
    }
    
    fun toggleRightDrawer() {
        _isRightDrawerOpen.value = !_isRightDrawerOpen.value
        if (_isRightDrawerOpen.value) {
            _isLeftDrawerOpen.value = false
        }
    }
    
    // 关闭所有抽屉
    fun closeAllDrawers() {
        _isLeftDrawerOpen.value = false
        _isRightDrawerOpen.value = false
    }
    
    // 设置左侧抽屉偏移量
    fun setLeftDrawerOffset(offset: Float) {
        _leftDrawerOffset = offset
    }
    
    // 设置右侧抽屉偏移量
    fun setRightDrawerOffset(offset: Float) {
        _rightDrawerOffset = offset
    }
    
    // 设置左侧拖拽状态
    fun setLeftDragging(isDragging: Boolean) {
        _isLeftDragging = isDragging
    }
    
    // 设置右侧拖拽状态
    fun setRightDragging(isDragging: Boolean) {
        _isRightDragging = isDragging
    }
}

/**
 * 侧边栏拖拽配置
 */
object BaseDrawerConfig {
    const val DRAWER_WIDTH_RATIO = 0.7f
    const val SWIPE_THRESHOLD_RATIO = 0.25f
    const val MAX_DRAG_RATIO = 0.85f // 稍微增加最大拖拽距离，提供更好的视觉反馈
    const val MIN_DRAG_THRESHOLD = 8f // 降低阈值，提高敏感度
    const val OPEN_THRESHOLD_RATIO = 0.25f // 降低打开阈值，更容易触发
    const val ANIMATION_DURATION_MS = 350 // 动画持续时间，更平滑
    const val DRAG_RESISTANCE_FACTOR = 0.8f // 拖拽阻力系数，提供更自然的手感
    const val VELOCITY_THRESHOLD = 800f // 速度阈值，用于快速滑动判断
    const val MAX_DRAG_SPEED = 15f // 最大单次拖拽距离，防止闪烁
    const val SMOOTH_FACTOR = 0.7f // 平滑系数，减少突兀变化
    
    // 限制拖拽速度，防止视觉闪烁
    fun limitDragSpeed(dragValue: Float): Float {
        val maxSpeed = MAX_DRAG_SPEED
        return when {
            dragValue > maxSpeed -> maxSpeed
            dragValue < -maxSpeed -> -maxSpeed
            else -> dragValue
        }
    }
    
    // 平滑拖拽变化
    fun smoothDrag(currentOffset: Float, targetOffset: Float): Float {
        return currentOffset + (targetOffset - currentOffset) * SMOOTH_FACTOR
    }
}

/**
 * 侧边栏Modifier扩展函数，用于在拖拽时禁用内容滚动
 */
fun Modifier.baseDrawer(isDragging: Boolean): Modifier {
    return this.let { modifier ->
        if (isDragging) {
            // 在拖拽时添加一个拦截所有滚动事件的nestedScroll
            modifier.nestedScroll(object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    return available // 消费所有滚动事件
                }
                override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                    return available // 消费所有滚动事件
                }
            })
        } else {
            modifier
        }
    }
}
