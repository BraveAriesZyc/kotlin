package com.zyc.clover.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyc.clover.manager.AppInitializationManager
import com.zyc.core.common.manager.InitializationState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 优化后的初始化应用ViewModel
 * 使用AppInitializationManager来管理初始化流程
 */
class InitAppViewModel(
    private val initializationManager: AppInitializationManager
) : ViewModel() {
    
    // 暴露初始化状态给UI层
    val initializationState: StateFlow<InitializationState> = initializationManager.initializationState
    val progress: StateFlow<Float> = initializationManager.progress
    
    /**
     * 异步初始化应用
     */
    fun initApp() {
        viewModelScope.launch {
            try {
                initializationManager.initializeApp()
            } catch (e: Exception) {
                // 处理初始化错误
                println("应用初始化失败: ${e.message}")
            }
        }
    }
    
    /**
     * 预加载关键数据
     * 可以在应用启动早期调用，不阻塞UI
     */
    fun preloadData() {
        initializationManager.preloadCriticalData()
    }
    
    /**
     * 重置初始化状态
     */
    fun resetInitialization() {
        initializationManager.reset()
    }
}