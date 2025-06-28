package com.zyc.clover.route

import kotlinx.serialization.Serializable

@Serializable
data object StartRoute

@Serializable
data object LayoutRoute

@Serializable
data object HomeRoute

@Serializable
data class WebViewRoute(val url: String)

@Serializable
data object RegisterRoute

@Serializable
data object LoginRoute

@Serializable
data class SendMessageRoute(val conversationId: String)

@Serializable
data object MessageRoute
