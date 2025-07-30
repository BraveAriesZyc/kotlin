package com.zyc.feature.message

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zyc.core.router.*

/**
 * 消息模块的导航配置
 * 
 * 该模块提供消息通讯相关功能，包括：
 * - 消息列表页面
 * - 发送消息页面
 * - 聊天会话管理
 * - 消息历史记录
 */
object MessageRootNavigation {
    const val GRAPH_ROUTE = "message_graph"
}

/**
 * 添加消息模块的导航图
 * 
 * @param navController 导航控制器
 * @param onBack 返回回调函数，默认为popBackStack
 */
fun NavGraphBuilder.messageGraph(
    navController: NavController,
    onBack: () -> Unit = { navController.popBackStack() }
) {
    composableSlide<Routes.Message.Message> { 
        MessageScreen(
            onNavigateToSendMessage = { conversationId ->
                navController.navigate(Routes.Message.SendMessage(conversationId))
            },
            onBack = onBack
        )
    }
    
    composableSlide<Routes.Message.SendMessage> { backStackEntry ->
        val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
        SendMessageScreen(
            conversationId = conversationId,
            onBack = onBack
        )
    }
}

/**
 * 导航到消息列表页面
 * 
 * @receiver NavController
 */
fun NavController.navigateToMessage() {
    navigate(Routes.Message.Message)
}

/**
 * 导航到发送消息页面
 * 
 * @receiver NavController
 * @param conversationId 会话ID
 */
fun NavController.navigateToSendMessage(conversationId: String) {
    navigate(Routes.Message.SendMessage(conversationId))
}