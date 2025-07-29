package com.zyc.feature.ui_showcase

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zyc.core.router.Routes
import com.zyc.core.router.composableSlide

/**
 * UI展示模块的导航配置
 */
object UIShowcaseRootNavigation {
    const val ROUTE = "ui_showcase"
}

/**
 * 添加UI展示模块的导航图
 */
fun NavGraphBuilder.uiShowcaseGraph(
    navController: NavController,
    onBack: () -> Unit = { navController.popBackStack() }
) {

    composableSlide<Routes.UIShowcase.UIShowcase> {
        UIShowcaseScreen(
            onBack = onBack
        )
    }
}

/**
 * 导航到UI展示模块
 */
fun NavController.navigateToUIShowcase() {
    navigate(UIShowcaseRootNavigation.ROUTE)
}
