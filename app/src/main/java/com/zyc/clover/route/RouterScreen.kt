package com.zyc.clover.route

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.zyc.clover.pages.auth.LoginScreen
import com.zyc.clover.pages.auth.RegisterScreen
import com.zyc.clover.pages.layout.LayoutScreen
import com.zyc.clover.pages.layout.children.message.MessageScreen
import com.zyc.clover.pages.layout.message.children.SendMessageScreen
import com.zyc.clover.pages.start.StartScreen
import com.zyc.clover.pages.web.WebViewScreen

import kotlinx.serialization.Serializable


// 定义导航控制器的 CompositionLocal
val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided")
}


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
        composableSlide<WebViewRoute> { WebViewScreen(it.arguments?.getString("url") ?: "") }
        composableSlide<SendMessageRoute> { SendMessageScreen(it.arguments?.getString("conversationId") ?: "") }
        composableScale<LayoutRoute> { LayoutScreen() }
//        navigation<LayoutRoute>(startDestination = LayoutRoute, builder = installLayout)
    }
}
