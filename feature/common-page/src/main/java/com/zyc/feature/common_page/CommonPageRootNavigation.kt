package com.zyc.feature.common_page

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*
import com.zyc.feature.common_page.pages.layout.LayoutScreen
import com.zyc.feature.common_page.pages.start.StartScreen
import com.zyc.feature.common_page.pages.web.WebViewScreen

/**
 * 通用页面模块的导航配置
 */
object CommonPageRootNavigation {
    const val GRAPH_ROUTE = "common_page_graph"
}

/**
 * 添加通用页面模块的导航图
 */
fun NavGraphBuilder.commonPageGraph(
    navController: NavController,
    onNavigateToAuth: () -> Unit = {},
    onNavigateToLayout: () -> Unit = {},
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableSlide<Routes.Start> { 
        StartScreen(
            onNavigateToAuth = onNavigateToAuth,
            onNavigateToLayout = onNavigateToLayout
        )
    }
    
    composableScale<Routes.Layout> { 
        LayoutScreen(
            onBack = onBack
        )
    }
    
    composableScale<Routes.WebView> { backStackEntry ->
        val url = backStackEntry.arguments?.getString("url") ?: ""
        WebViewScreen(
            url = url,
            onBack = onBack
        )
    }
}

/**
 * 导航到启动页面
 */
fun NavController.navigateToStart() {
    navigate(Routes.Start)
}

/**
 * 导航到布局页面
 */
fun NavController.navigateToLayout() {
    navigate(Routes.Layout)
}

/**
 * 导航到WebView页面
 */
fun NavController.navigateToWebView(url: String) {
    navigate(Routes.WebView(url))
}