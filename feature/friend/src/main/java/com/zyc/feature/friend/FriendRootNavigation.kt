package com.zyc.feature.friend

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*
import com.zyc.feature.friend.components.AddFriendScreen

/**
 * 好友模块的导航配置
 */
object FriendRootNavigation {
    const val GRAPH_ROUTE = "friend_graph"
}

/**
 * 添加好友模块的导航图
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
 */
fun NavController.navigateToAddFriend() {
    navigate(Routes.Friend.AddFriend)
}