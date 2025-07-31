package com.zyc.clover.route

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.zyc.core.router.*
import com.zyc.clover.route.NavigationManager.installAllModules


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
            startDestination = Routes.Root,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None},
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None}
        ) {
            installRoot(navController)
        }
    }
}


val installRoot: NavGraphBuilder.(navController: androidx.navigation.NavHostController) -> Unit = { navController ->
    navigation<Routes.Root>(startDestination = Routes.Start) {
        // 使用导航管理器统一安装所有模块的导航配置
        installAllModules(
            navController = navController
        )
    }
}
