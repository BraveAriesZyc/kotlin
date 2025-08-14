package com.zyc.feature.ui_showcase.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zyc.core.router.Routes
import com.zyc.core.router.composableSlide
import com.zyc.feature.ui_showcase.screen.animation.AnimationComponentsScreen
import com.zyc.feature.ui_showcase.screen.common.CommonComponentsScreen
import com.zyc.feature.ui_showcase.screen.feedback.FeedbackComponentsScreen
import com.zyc.feature.ui_showcase.screen.form.FormComponentsScreen
import com.zyc.feature.ui_showcase.screen.hardware.HardwareComponentsScreen
import com.zyc.feature.ui_showcase.screen.interaction.InteractionComponentsScreen
import com.zyc.feature.ui_showcase.screen.layout.LayoutComponentsScreen
import com.zyc.feature.ui_showcase.screen.navigation.NavigationComponentsScreen
import com.zyc.feature.ui_showcase.screen.network.media.VideoComponentsScreen
import com.zyc.feature.ui_showcase.screen.network.upload.FileUploadComponentsScreen
import com.zyc.feature.ui_showcase.screen.permission.PermissionComponentsScreen
import com.zyc.feature.ui_showcase.screen.system.NotificationScreen

/**
 * UI展示模块的子页面路由配置
 *
 * 该文件负责配置UI展示模块内部的所有子页面导航，包括：
 * - UI展示首页
 * - 通用组件展示页面
 * - 表单组件展示页面
 * - 反馈组件展示页面
 * - 布局组件展示页面
 * - 导航组件展示页面
 * - 交互组件展示页面
 * - 动画组件展示页面
 * - 硬件组件展示页面
 */


/**
 * 安装UI展示模块的所有子页面导航
 *
 * @param navController 导航控制器
 * @param onBack 返回回调函数
 */
fun NavGraphBuilder.installUIShowcaseScreens(
    navController: NavController,
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableSlide<Routes.UIShowcase.CommonComponents> {
        CommonComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composableSlide<Routes.UIShowcase.FormComponents> {
        FormComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composableSlide<Routes.UIShowcase.FeedbackComponents> {
        FeedbackComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composableSlide<Routes.UIShowcase.LayoutComponents> {
        LayoutComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composableSlide<Routes.UIShowcase.NavigationComponents> {
        NavigationComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composableSlide<Routes.UIShowcase.InteractionComponents> {
        InteractionComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composableSlide<Routes.UIShowcase.AnimationComponents> {
        AnimationComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }

    composableSlide<Routes.UIShowcase.HardwareComponents> {
        HardwareComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }
    composableSlide<Routes.UIShowcase.SystemComponents> {
        NotificationScreen(
            onBack = { navController.popBackStack() }
        )
    }
    composableSlide<Routes.UIShowcase.PermissionComponents> {
        PermissionComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }
    composableSlide<Routes.UIShowcase.NetworkComponents.FileUploadComponents> {
        FileUploadComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }
    composableSlide<Routes.UIShowcase.NetworkComponents.VideoComponents> {
        VideoComponentsScreen(
            onBack = { navController.popBackStack() }
        )
    }

}
