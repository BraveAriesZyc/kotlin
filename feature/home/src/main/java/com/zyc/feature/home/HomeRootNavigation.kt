package com.zyc.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*

/**
 * 首页模块的导航配置
 *
 * 该模块提供应用首页相关功能，包括：
 * - 首页主界面
 * - 首页内容展示
 * - 首页功能导航
 */

/**
 * 添加首页模块的导航图
 *
 * @param navController 导航控制器
 * @param onBack 返回回调函数，默认为popBackStack
 */
fun NavGraphBuilder.homeGraph(
    navController: NavController,
    onBack: () -> Unit = { navController.popBackStack() }
) {

}

/**
 * 导航到首页
 *
 * @receiver NavController
 */
fun NavController.navigateToHome() {
    navigate(Routes.Home.Home)
}

