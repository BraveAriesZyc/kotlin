package com.zyc.core.router

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder

/**
 * 全局导航控制器的 CompositionLocal
 * 
 * 用于在Compose组件树中提供NavHostController实例，
 * 使得任何组件都可以通过CompositionLocal访问导航控制器
 */
val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided")
}

/**
 * 核心导航管理器
 * 
 * 提供统一的导航功能封装，包括：
 * - 基础导航操作（前进、后退）
 * - 导航栈管理
 * - 路由状态检查
 * - 导航选项配置
 * 
 * @param navController 底层的NavHostController实例
 */
class NavigationManager(private val navController: NavHostController) {

    // ==================== 基础导航方法 ====================

    /**
     * 导航到指定路由
     * 
     * @param route 目标路由
     * @param navOptions 导航选项配置
     */
    fun navigateTo(route: Any, navOptions: NavOptions? = null) {
        navController.navigate(route, navOptions)
    }

    /**
     * 导航到指定路由（使用构建器）
     * 
     * @param route 目标路由
     * @param builder 导航选项构建器
     */
    fun navigateTo(route: Any, builder: NavOptionsBuilder.() -> Unit) {
        navController.navigate(route, builder)
    }

    /**
     * 返回上一页
     * 
     * @return 是否成功返回
     */
    fun navigateBack(): Boolean {
        return navController.popBackStack()
    }

    /**
     * 返回到指定路由
     * 
     * @param route 目标路由
     * @param inclusive 是否包含目标路由本身
     * @return 是否成功返回
     */
    fun navigateBackTo(route: String, inclusive: Boolean = false): Boolean {
        return navController.popBackStack(route, inclusive)
    }

    /**
     * 清空导航栈并导航到新路由
     * 
     * @param route 目标路由
     */
    fun navigateAndClearStack(route: Any) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }


    /**
     * 获取当前路由
     * 
     * @return 当前路由字符串，如果无法获取则返回null
     */
    fun getCurrentRoute(): String? {
        return navController.currentDestination?.route
    }

    /**
     * 检查是否可以返回
     * 
     * @return 是否存在可返回的页面
     */
    fun canNavigateBack(): Boolean {
        return navController.previousBackStackEntry != null
    }

    /**
     * 检查当前是否在指定路由
     * 
     * @param route 要检查的路由
     * @return 是否在指定路由
     */
    fun isCurrentRoute(route: String): Boolean {
        return getCurrentRoute() == route
    }
}

/**
 * 创建导航管理器的扩展函数
 * 
 * 将NavHostController转换为NavigationManager实例
 * 
 * @receiver NavHostController
 * @return NavigationManager实例
 */
fun NavHostController.asNavigationManager(): NavigationManager {
    return NavigationManager(this)
}
