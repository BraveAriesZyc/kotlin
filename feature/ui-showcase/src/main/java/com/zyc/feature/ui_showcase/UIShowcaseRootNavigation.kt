package com.zyc.feature.ui_showcase

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.Routes
import com.zyc.core.router.composableSlide
import com.zyc.feature.ui_showcase.main.UIShowcaseScreen
import com.zyc.feature.ui_showcase.main.installUIShowcaseScreens

/**
 * UI展示模块的根导航配置
 *
 * 该模块提供了完整的UI组件展示功能，包括：
 * - UI展示主页面
 * - 各类组件分类页面（通用、表单、反馈、布局、导航、交互、动画、硬件等）
 */

/**
 * 安装UI展示模块的导航图
 *
 * @param navController 导航控制器
 * @param onBack 返回回调函数，默认为popBackStack
 */

fun NavGraphBuilder.installUiShowcaseGraph(
    navController: NavController,
    onBack: () -> Unit = { navController.popBackStack() }
) {
    // 注册UI展示主页面
    composableSlide<Routes.UIShowcase.UIShowcase> {
        UIShowcaseScreen(
            onNavigateToCategory = { route ->
                navController.navigate(route)
            },
            onBack = onBack
        )
    }

    // 安装所有UI展示子页面
    installUIShowcaseScreens(navController)
}

/**
 * 导航到UI展示模块
 *
 * @receiver NavController
 */
fun NavController.navigateToUIShowcase() {
    navigate(Routes.UIShowcase.UIShowcase)
}
