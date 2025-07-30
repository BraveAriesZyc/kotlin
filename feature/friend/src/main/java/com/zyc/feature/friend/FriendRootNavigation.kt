package com.zyc.feature.friend

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*
import com.zyc.feature.friend.components.AddFriendScreen

/**
 * 好友模块的导航配置
 *
 * 该模块提供好友管理相关功能，包括：
 * - 添加好友页面
 * - 好友列表管理
 * - 好友信息查看
 * - 好友聊天功能
 */

/**
 * 添加好友模块的导航图
 *
 * @param navController 导航控制器
 * @param onNavigateToSendMessage 导航到发送消息页面的回调
 * @param onBack 返回回调函数，默认为popBackStack
 */
fun NavGraphBuilder.friendGraph(
    navController: NavController,
    onNavigateToSendMessage: (String) -> Unit = {},
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableSlide<Routes.Friend.AddFriend> {
        AddFriendScreen(
            onBack = onBack
        )
    }
}

/**
 * 导航到添加好友页面
 *
 * @receiver NavController
 */
fun NavController.navigateToAddFriend() {
    navigate(Routes.Friend.AddFriend)
}
