package com.zyc.core.router

import kotlinx.serialization.Serializable

/**
 * 应用程序所有路由的统一管理
 * 使用 Kotlin Serialization 支持类型安全的导航
 */
object Routes {
    
    // ==================== 根路由 ====================
    @Serializable
    data object Root
    
    @Serializable
    data object Start
    
    @Serializable
    data object Layout
    
    // ==================== 通用页面路由 ====================
    @Serializable
    data class WebView(val url: String)
    
    // ==================== 认证模块路由 ====================
    @Serializable
    data object Login
    
    @Serializable
    data object Register
    
    // ==================== 主要功能模块路由 ====================
    @Serializable
    data object Home
    
    @Serializable
    data object Message
    
    @Serializable
    data class SendMessage(val conversationId: String)
    
    @Serializable
    data object Friend
    
    @Serializable
    data object AddFriend
    
    @Serializable
    data object Profile
    
    @Serializable
    data object Setting
    
    // ==================== UI展示模块路由 ====================
    @Serializable
    data object UIShowcase
    
    @Serializable
    data object UIShowcaseHome
    
    @Serializable
    data object CommonComponents
    
    @Serializable
    data object FormComponents
    
    @Serializable
    data object FeedbackComponents
    
    @Serializable
    data object LayoutComponents
    
    @Serializable
    data object NavigationComponents
    
    @Serializable
    data object InteractionComponents
    
    @Serializable
    data object AnimationComponents
    
    @Serializable
    data object HardwareComponents
    

    

}