package com.zyc.core.ui.route

import kotlinx.serialization.Serializable

// 分发路由
@Serializable
data object RootRoute

@Serializable
data object StartRoute

@Serializable
data object LayoutRoute

@Serializable
data class WebViewRoute(val url: String)

@Serializable
data object RegisterRoute

@Serializable
data object LoginRoute

@Serializable
data object AddFriendRoute

@Serializable
data class SendMessageRoute(val conversationId: String)

@Serializable
data object MessageRoute
