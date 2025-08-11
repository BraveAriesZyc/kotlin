package com.zyc.core.permission.manager

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zyc.core.permission.model.Permission
import com.zyc.core.permission.model.PermissionStatus


class PermissionManager(private val context: Context) {
    /**
     * 检查单个权限的状态
     * @param permission 要检查的权限，如Manifest.permission.CAMERA
     * @return 权限状态：同意、临时拒绝或永久拒绝
     */
    fun checkPermissionStatus(permission: Permission, activity: Activity? = null): PermissionStatus {
        // 检查权限是否已授予
        if (ContextCompat.checkSelfPermission(
                context,
                permission.manifestPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return PermissionStatus.GRANTED
        }

        // 判断是否为永久拒绝（需要Activity来调用shouldShowRequestPermissionRationale）
        if (activity != null && !activity.shouldShowRequestPermissionRationale(permission.manifestPermission)) {
            return PermissionStatus.PERMANENTLY_DENIED
        }

        // 否则为临时拒绝
        return PermissionStatus.TEMPORARILY_DENIED
    }

    /**
     * 检查多个权限的状态
     * @param permissions 要检查的权限列表
     * @return 权限名称与权限状态的映射
     */
    fun checkPermissionsStatus(
        permissions: List<Permission>,
        activity: Activity? = null
    ): Map<Permission, PermissionStatus> {
        return permissions.associateWith { it ->
            checkPermissionStatus(it, activity)
        }
    }

    //请求权限
    fun requestPermission(permission: Permission) {

        try {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(permission.manifestPermission),
                permission.ordinal
            )

        }catch (e:Exception){
            Log.e("PermissionManager", "requestPermission: ${e.message}")
        }
    }

}
