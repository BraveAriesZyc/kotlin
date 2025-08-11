package com.zyc.core.router

import kotlinx.serialization.Serializable

/**
 * 应用程序所有路由的统一管理
 * 使用 Kotlin Serialization 支持类型安全的导航
 */
object Routes {
    // ==================== 根路由 ====================
    @Serializable
    data object Root

    @Serializable
    data object Start

    @Serializable
    data object Layout

    // ==================== 通用模块路由 ====================
    object Common {
        @Serializable
        data class WebView(val url: String)

        @Serializable
        data object WebList
    }

    // ==================== 认证模块路由 ====================
    object Auth {
        @Serializable
        data object Login

        @Serializable
        data object Register
    }

    // ==================== 主页模块路由 ====================
    object Home {
        @Serializable
        data object Home
    }

    // ==================== 消息模块路由 ====================
    object Message {
        @Serializable
        data object Message

        @Serializable
        data class SendMessage(val conversationId: String)
    }

    // ==================== 好友模块路由 ====================
    object Friend {
        @Serializable
        data object Friend

        @Serializable
        data object AddFriend
    }

    // ==================== 个人资料模块路由 ====================
    object Profile {
        @Serializable
        data object Profile

        @Serializable
        data object Setting

        @Serializable
        data object ThemeSetting

        @Serializable
        data object LanguageSetting
    }

    // ==================== UI展示模块路由 ====================
    object UIShowcase {
        @Serializable
        data object UIShowcase

        @Serializable
        data object UIShowcaseHome

        @Serializable
        data object CommonComponents

        @Serializable
        data object FormComponents

        @Serializable
        data object FeedbackComponents

        @Serializable
        data object LayoutComponents

        @Serializable
        data object NavigationComponents

        @Serializable
        data object InteractionComponents

        @Serializable
        data object AnimationComponents

        @Serializable
        data object HardwareComponents

        @Serializable
        data object PermissionComponents

        @Serializable
        data object SystemComponents
    }
}
