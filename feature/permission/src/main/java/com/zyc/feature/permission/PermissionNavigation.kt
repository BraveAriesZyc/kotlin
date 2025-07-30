package com.zyc.feature.permission

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * 权限管理路由常量
 */
const val PERMISSION_ROUTE = "permission"

/**
 * 权限管理导航扩展
 */
fun NavController.navigateToPermission() {
    this.navigate(PERMISSION_ROUTE)
}

/**
 * 权限管理导航图配置
 */
fun NavGraphBuilder.permissionScreen() {
    composable(route = PERMISSION_ROUTE) {
        PermissionScreen()
    }
}