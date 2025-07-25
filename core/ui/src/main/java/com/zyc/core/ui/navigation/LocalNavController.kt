package com.zyc.core.ui.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

// 定义导航控制器的 CompositionLocal
val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavController provided")
}