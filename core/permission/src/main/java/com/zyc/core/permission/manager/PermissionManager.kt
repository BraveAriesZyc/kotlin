package com.zyc.core.permission.manager

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionManager(private val context: Context) {

    /**
     * 检查单个权限的状态
     * @param permission 要检查的权限，如Manifest.permission.CAMERA
     * @return true 如果权限已授予，false 否则
     */
    fun checkPermissionStatus(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查多个权限的状态
     * @param permissions 要检查的权限列表，如 listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
     * @return 权限名称与权限状态（已授予为true，未授予为false）的映射
     */
    fun checkPermissionsStatus(permissions: List<String>): Map<String, Boolean> {
        return permissions.associateWith {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
