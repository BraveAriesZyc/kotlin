package com.zyc.core.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel 基类
 * 
 * 提供通用的 ViewModel 功能：
 * - 统一的错误处理
 * - 加载状态管理
 * - 协程作用域管理
 */
abstract class BaseViewModel : ViewModel() {
    
    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // 错误状态
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // 错误处理器
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }
    
    /**
     * 安全执行协程
     * 
     * @param showLoading 是否显示加载状态
     * @param block 要执行的代码块
     */
    protected fun launchSafely(
        showLoading: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(exceptionHandler) {
            try {
                if (showLoading) {
                    _isLoading.value = true
                }
                _error.value = null
                block()
            } finally {
                if (showLoading) {
                    _isLoading.value = false
                }
            }
        }
    }
    
    /**
     * 处理错误
     * 
     * @param throwable 异常
     */
    private fun handleError(throwable: Throwable) {
        _isLoading.value = false
        _error.value = throwable.message ?: "未知错误"
        // 可以在这里添加日志记录
        throwable.printStackTrace()
    }
    
    /**
     * 清除错误状态
     */
    fun clearError() {
        _error.value = null
    }
}