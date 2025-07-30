package com.zyc.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*

/**
 * 认证模块的导航配置
 * 
 * 该模块提供用户认证相关功能，包括：
 * - 用户登录页面
 * - 用户注册页面
 * - 密码重置功能
 * - 第三方登录集成
 */
object AuthRootNavigation {
    const val GRAPH_ROUTE = "auth_graph"
}

/**
 * 添加认证模块的导航图
 * 
 * @param navController 导航控制器
 * @param onNavigateToLayout 导航到布局页面的回调
 * @param onBack 返回回调函数，默认为popBackStack
 */
fun NavGraphBuilder.authGraph(
    navController: NavController,
    onNavigateToLayout: () -> Unit = {},
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableSlide<Routes.Auth.Login> { 
        LoginScreen(
            onNavigateToRegister = { navController.navigate(Routes.Auth.Register) },
            onNavigateToLayout = onNavigateToLayout,
            onBack = onBack
        )
    }
    
    composableSlide<Routes.Auth.Register> { 
        RegisterScreen(
            onNavigateToLogin = { navController.navigate(Routes.Auth.Login) },
            onNavigateToLayout = onNavigateToLayout,
            onBack = onBack
        )
    }
}

/**
 * 导航到登录页面
 * 
 * @receiver NavController
 */
fun NavController.navigateToLogin() {
    navigate(Routes.Auth.Login)
}

/**
 * 导航到注册页面
 * 
 * @receiver NavController
 */
fun NavController.navigateToRegister() {
    navigate(Routes.Auth.Register)
}