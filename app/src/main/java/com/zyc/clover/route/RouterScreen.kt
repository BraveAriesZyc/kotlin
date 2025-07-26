package com.zyc.clover.route

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.zyc.core.ui.route.LocalNavController
import com.zyc.feature.auth.LoginScreen
import com.zyc.feature.auth.RegisterScreen
import com.zyc.feature.common_ui.pages.layout.LayoutScreen
import com.zyc.feature.message.MessageScreen
import com.zyc.feature.message.SendMessageScreen
import com.zyc.feature.common_ui.pages.start.StartScreen
import com.zyc.core.ui.components.WebViewScreen
import com.zyc.core.ui.route.LayoutRoute
import com.zyc.core.ui.route.LoginRoute
import com.zyc.core.ui.route.MessageRoute
import com.zyc.core.ui.route.RegisterRoute
import com.zyc.core.ui.route.SendMessageRoute
import com.zyc.core.ui.route.StartRoute
import com.zyc.core.ui.route.WebViewRoute
import com.zyc.core.ui.route.composableScale
import com.zyc.core.ui.route.composableSlide


import kotlinx.serialization.Serializable





@Composable
fun NavigationRouterScreen() {

    // 提供导航控制器和主题管理器
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalNavController provides navController,
    ) {
        NavHost(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            navController = navController,
            startDestination = RootRoute,
            builder = installRoot,
        )
    }
}


// 分发路由
@Serializable
data object RootRoute

val installRoot: (NavGraphBuilder.() -> Unit) = {
    navigation<RootRoute>(startDestination = StartRoute) {
        composableSlide<StartRoute> { StartScreen() }
        composableSlide<LoginRoute> { LoginScreen() }
        composableSlide<RegisterRoute> { RegisterScreen() }
        composableSlide<MessageRoute> { MessageScreen() }
        composableScale<WebViewRoute> { WebViewScreen(it.arguments?.getString("url") ?: "") }
        composableSlide<SendMessageRoute> { SendMessageScreen(it.arguments?.getString("conversationId") ?: "") }
        composableScale<LayoutRoute> { LayoutScreen() }

//        navigation<LayoutRoute>(startDestination = LayoutRoute, builder = installLayout)
    }
}
