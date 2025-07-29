package com.zyc.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*

/**
 * 首页模块的导航配置
 */
object HomeRootNavigation {
    const val GRAPH_ROUTE = "home_graph"
}

/**
 * 添加首页模块的导航图
 */
fun NavGraphBuilder.homeGraph(
    navController: NavController,
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableSlide<Routes.Home> { 
        HomeScreen(
            onBack = onBack
        )
    }
}

/**
 * 导航到首页
 */
fun NavController.navigateToHome() {
    navigate(Routes.Home)
}