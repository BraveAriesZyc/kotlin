package com.zyc.feature.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*

/**
 * 认证模块的导航配置
 */
object AuthRootNavigation {
    const val GRAPH_ROUTE = "auth_graph"
}

/**
 * 添加认证模块的导航图
 */
fun NavGraphBuilder.authGraph(
    navController: NavController,
    onNavigateToLayout: () -> Unit = {},
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableSlide<Routes.Login> { 
        LoginScreen(
            onNavigateToRegister = { navController.navigate(Routes.Register) },
            onNavigateToLayout = onNavigateToLayout,
            onBack = onBack
        )
    }
    
    composableSlide<Routes.Register> { 
        RegisterScreen(
            onNavigateToLogin = { navController.navigate(Routes.Login) },
            onNavigateToLayout = onNavigateToLayout,
            onBack = onBack
        )
    }
}

/**
 * 导航到登录页面
 */
fun NavController.navigateToLogin() {
    navigate(Routes.Login)
}

/**
 * 导航到注册页面
 */
fun NavController.navigateToRegister() {
    navigate(Routes.Register)
}