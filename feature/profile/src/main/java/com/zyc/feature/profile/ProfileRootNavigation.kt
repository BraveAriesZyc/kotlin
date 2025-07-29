package com.zyc.feature.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*


/**
 * 添加个人资料模块的导航图
 */
fun NavGraphBuilder.profileGraph(
    navController: NavController,
    onNavigateToAuth: () -> Unit = {},
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableSlide<Routes.Profile> {

    }
}
