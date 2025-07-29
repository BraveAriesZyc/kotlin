package com.zyc.feature.message

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*

/**
 * 消息模块的导航配置
 */
object MessageRootNavigation {
    const val GRAPH_ROUTE = "message_graph"
}

/**
 * 添加消息模块的导航图
 */
fun NavGraphBuilder.messageGraph(
    navController: NavController,
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableSlide<Routes.Message> { 
        MessageScreen(
            onNavigateToSendMessage = { conversationId ->
                navController.navigate(Routes.SendMessage(conversationId))
            },
            onBack = onBack
        )
    }
    
    composableSlide<Routes.SendMessage> { backStackEntry ->
        val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
        SendMessageScreen(
            conversationId = conversationId,
            onBack = onBack
        )
    }
}

/**
 * 导航到消息列表页面
 */
fun NavController.navigateToMessage() {
    navigate(Routes.Message)
}

/**
 * 导航到发送消息页面
 */
fun NavController.navigateToSendMessage(conversationId: String) {
    navigate(Routes.SendMessage(conversationId))
}