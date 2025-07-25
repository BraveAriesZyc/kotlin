package com.zyc.core.common.manager

/**
 * 应用初始化状态枚举
 */
enum class InitializationState {
    /**
     * 空闲状态 - 尚未开始初始化
     */
    IDLE,
    
    /**
     * 初始化中 - 正在执行初始化流程
     */
    INITIALIZING,
    
    /**
     * 初始化完成 - 所有初始化任务已成功完成
     */
    COMPLETED,
    
    /**
     * 初始化错误 - 初始化过程中发生错误
     */
    ERROR
}