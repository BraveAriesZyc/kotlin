package com.zyc.feature.common_page

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*
import com.zyc.feature.common_page.pages.layout.LayoutScreen
import com.zyc.feature.common_page.pages.start.StartScreen
import com.zyc.feature.common_page.pages.web.WebListScreen
import com.zyc.feature.common_page.pages.web.WebViewScreen


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
 */
fun NavController.navigateToLayout() {
    navigate(Routes.Layout)
}

