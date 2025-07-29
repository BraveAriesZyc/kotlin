package com.zyc.clover.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*
import com.zyc.feature.auth.authGraph
import com.zyc.feature.auth.navigateToLogin
import com.zyc.feature.common_page.commonPageGraph
import com.zyc.feature.common_page.navigateToLayout
import com.zyc.feature.friend.friendGraph
import com.zyc.feature.home.homeGraph
import com.zyc.feature.message.messageGraph
import com.zyc.feature.profile.profileGraph
import com.zyc.feature.ui_showcase.uiShowcaseGraph

/**
 * 导航管理器，统一管理所有模块的导航配置
 */
object NavigationManager {
    
    /**
     * 安装所有模块的导航图
     */
    fun NavGraphBuilder.installAllModules(
        navController: NavController
    ) {
        // 通用页面模块
        commonPageGraph(
            navController = navController,
            onNavigateToAuth = { navController.navigateToLogin() },
            onNavigateToLayout = { navController.navigateToLayout() }
        )
        
        // 认证模块
        authGraph(
            navController = navController,
            onNavigateToLayout = { navController.navigateToLayout() }
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
        uiShowcaseGraph(
            navController = navController
        )
    }
}