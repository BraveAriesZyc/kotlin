package com.zyc.clover.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*
import com.zyc.feature.auth.authGraph
import com.zyc.feature.auth.navigateToLogin
import com.zyc.feature.common_page.commonPageGraph

import com.zyc.feature.friend.friendGraph
import com.zyc.feature.home.homeGraph
import com.zyc.feature.message.messageGraph
import com.zyc.feature.profile.profileGraph
import com.zyc.feature.ui_showcase.installUiShowcaseGraph

/**
 * 应用级导航管理器
 *
 * 统一管理所有功能模块的导航配置，包括：
 * - 通用页面模块（启动页、布局页、WebView等）
 * - 认证模块（登录、注册）
 * - 消息模块（消息列表、发送消息）
 * - 好友模块（添加好友、好友列表）
 * - 首页模块
 * - 个人资料模块
 * - UI展示模块（组件展示）
 *
 * 该管理器负责协调各模块间的导航关系和依赖
 */
object NavigationManager {

    /**
     * 安装所有模块的导航图
     *
     * 按照模块依赖关系顺序安装各功能模块的导航配置，
     * 并设置模块间的导航回调关系
     *
     * @param navController 主导航控制器
     */
    fun NavGraphBuilder.installAllModules(
        navController: NavController
    ) {
        // 通用页面模块
        commonPageGraph(navController = navController)

        // 认证模块
        authGraph(
            navController = navController,

            )

        // 消息模块
        messageGraph(
            navController = navController
        )

        // 好友模块
        friendGraph(
            navController = navController,
            onNavigateToSendMessage = { conversationId ->
                navController.navigate(Routes.Message.SendMessage(conversationId))
            }
        )

        // 首页模块
        homeGraph(
            navController = navController
        )

        // 个人资料模块
        profileGraph(
            navController = navController,
            onNavigateToAuth = { navController.navigateToLogin() }
        )

        // UI展示模块
        installUiShowcaseGraph(
            navController = navController
        )
    }
}
