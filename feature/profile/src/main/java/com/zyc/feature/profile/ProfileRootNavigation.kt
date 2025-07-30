package com.zyc.feature.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*

/**
 * 个人资料模块的导航配置
 *
 * 该模块提供用户个人资料相关功能，包括：
 * - 个人资料查看页面
 * - 个人信息编辑页面
 * - 用户设置页面
 * - 账户管理功能
 */

/**
 * 添加个人资料模块的导航图
 *
 * @param navController 导航控制器
 * @param onNavigateToAuth 导航到认证页面的回调
 * @param onBack 返回回调函数，默认为popBackStack
 */
fun NavGraphBuilder.profileGraph(
    navController: NavController,
    onNavigateToAuth: () -> Unit = {},
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableSlide<Routes.Profile.Profile> {

    }
}

/**
 * 导航到个人资料页面
 *
 * @receiver NavController
 */
fun NavController.navigateToProfile() {
    navigate(Routes.Profile.Profile)
}
