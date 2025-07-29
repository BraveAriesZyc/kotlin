package com.zyc.core.router

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder

/**
 * 全局导航控制器的 CompositionLocal
 */
val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided")
}

/**
 * 导航管理器 - 提供统一的导航功能
 */
class NavigationManager(private val navController: NavHostController) {
    
    // ==================== 基础导航方法 ====================
    
    /**
     * 导航到指定路由
     */
    fun navigateTo(route: Any, navOptions: NavOptions? = null) {
        navController.navigate(route, navOptions)
    }
    
    /**
     * 导航到指定路由（使用构建器）
     */
    fun navigateTo(route: Any, builder: NavOptionsBuilder.() -> Unit) {
        navController.navigate(route, builder)
    }
    
    /**
     * 返回上一页
     */
    fun navigateBack(): Boolean {
        return navController.popBackStack()
    }
    
    /**
     * 返回到指定路由
     */
    fun navigateBackTo(route: String, inclusive: Boolean = false): Boolean {
        return navController.popBackStack(route, inclusive)
    }
    
    /**
     * 清空栈并导航到新路由
     */
    fun navigateAndClearStack(route: Any) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
    
    // ==================== 具体功能导航方法 ====================
    
    /**
     * 导航到登录页面
     */
    fun navigateToLogin() {
        navigateTo(Routes.Login)
    }
    
    /**
     * 导航到注册页面
     */
    fun navigateToRegister() {
        navigateTo(Routes.Register)
    }
    
    /**
     * 导航到主页
     */
    fun navigateToHome() {
        navigateTo(Routes.Home)
    }
    
    /**
     * 导航到消息页面
     */
    fun navigateToMessage() {
        navigateTo(Routes.Message)
    }
    
    /**
     * 导航到发送消息页面
     */
    fun navigateToSendMessage(conversationId: String) {
        navigateTo(Routes.SendMessage(conversationId))
    }
    
    /**
     * 导航到好友页面
     */
    fun navigateToFriend() {
        navigateTo(Routes.Friend)
    }
    
    /**
     * 导航到添加好友页面
     */
    fun navigateToAddFriend() {
        navigateTo(Routes.AddFriend)
    }
    
    /**
     * 导航到个人资料页面
     */
    fun navigateToProfile() {
        navigateTo(Routes.Profile)
    }
    
    /**
     * 导航到设置页面
     */
    fun navigateToSetting() {
        navigateTo(Routes.Setting)
    }
    
    /**
     * 导航到UI展示页面
     */
    fun navigateToUIShowcase() {
        navigateTo(Routes.UIShowcase)
    }
    
    /**
     * 导航到WebView页面
     */
    fun navigateToWebView(url: String) {
        navigateTo(Routes.WebView(url))
    }
    
    /**
     * 导航到布局页面
     */
    fun navigateToLayout() {
        navigateTo(Routes.Layout)
    }
    
    /**
     * 导航到启动页面
     */
    fun navigateToStart() {
        navigateTo(Routes.Start)
    }
    
    // ==================== 认证相关导航 ====================
    
    /**
     * 登录成功后的导航
     */
    fun navigateAfterLogin() {
        navigateAndClearStack(Routes.Layout)
    }
    
    /**
     * 登出后的导航
     */
    fun navigateAfterLogout() {
        navigateAndClearStack(Routes.Start)
    }
    
    // ==================== 工具方法 ====================
    
    /**
     * 获取当前路由
     */
    fun getCurrentRoute(): String? {
        return navController.currentDestination?.route
    }
    
    /**
     * 检查是否可以返回
     */
    fun canNavigateBack(): Boolean {
        return navController.previousBackStackEntry != null
    }
    
    /**
     * 检查当前是否在指定路由
     */
    fun isCurrentRoute(route: String): Boolean {
        return getCurrentRoute() == route
    }
}

/**
     * 创建导航管理器的扩展函数
     */
fun NavHostController.asNavigationManager(): NavigationManager {
    return NavigationManager(this)
}