package com.zyc.feature.common_page

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*
import com.zyc.feature.common_page.pages.layout.LayoutScreen
import com.zyc.feature.common_page.pages.start.StartScreen
import com.zyc.feature.common_page.pages.webList.WebListScreen
import com.zyc.feature.common_page.pages.web.WebViewScreen

/**
 * 通用页面模块的导航配置
 *
 * 该模块提供应用通用页面功能，包括：
 * - 启动页面
 * - 布局展示页面
 * - WebView网页浏览
 * - 网页列表管理
 * - 通用UI组件
 */

/**
 * 添加通用页面模块的导航图
 *
 * @param navController 导航控制器
 * @param onNavigateToAuth 导航到认证页面的回调
 * @param onNavigateToLayout 导航到布局页面的回调
 * @param onBack 返回回调函数，默认为popBackStack
 */
fun NavGraphBuilder.commonPageGraph(
    navController: NavController,
    onNavigateToAuth: () -> Unit = {},
    onNavigateToLayout: () -> Unit = {},
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableScale<Routes.Start> {
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

    composableScale<Routes.Common.WebView> { backStackEntry ->
        val url = backStackEntry.arguments?.getString("url") ?: ""
        WebViewScreen(
            url = url,
            onBack = onBack
        )
    }
    composableScale<Routes.Common.WebList> {
        WebListScreen(
            onBack = onBack
        )
    }
}

/**
 * 导航到布局页面
 *
 * @receiver NavController
 */
fun NavController.navigateToLayout() {
    navigate(Routes.Layout)
}

/**
 * 导航到启动页面
 *
 * @receiver NavController
 */
fun NavController.navigateToStart() {
    navigate(Routes.Start)
}

/**
 * 导航到WebView页面
 *
 * @receiver NavController
 * @param url 要加载的网页URL
 */
fun NavController.navigateToWebView(url: String) {
    navigate(Routes.Common.WebView(url))
}

/**
 * 导航到网页列表页面
 *
 * @receiver NavController
 */
fun NavController.navigateToWebList() {
    navigate(Routes.Common.WebList)
}

