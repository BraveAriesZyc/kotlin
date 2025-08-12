package com.zyc.core.permission.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.zyc.core.permission.model.Permission
import com.zyc.core.permission.model.PermissionManagerModel
import com.zyc.core.permission.model.PermissionStatus


/**
 * 权限管理器，自动识别权限状态并处理相应逻辑
 *
 * @param permission 要检查的权限
 * @param onStatusChanged 权限状态变化回调
 * @param autoHandle 是否自动处理权限请求（临时拒绝时申请，永久拒绝时跳转设置）
 * @return 手动触发权限检查的函数
 */
@Composable
fun rememberPermissionManager(
    permission: Permission,
): PermissionManagerModel {
    val context = LocalContext.current
    val permissionStr = permission.manifestPermission

    // 跟踪权限状态
    var permissionStatus by remember(permissionStr) {
        mutableStateOf(checkPermissionStatus(context, permissionStr))
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionStatus = if (isGranted) {
            PermissionStatus.GRANTED
        } else {
            PermissionStatus.TEMPORARILY_DENIED
        }
    }


    return PermissionManagerModel(
        permissionStatus = permissionStatus,
        permissionLauncher = {
            if (permissionStatus == PermissionStatus.TEMPORARILY_DENIED) {
                launcher.launch(permissionStr)
            } else if (permissionStatus == PermissionStatus.PERMANENTLY_DENIED) {
                openAppSettings(context)
            }
        }
    )


}

/**
 * 检查权限当前状态
 */
private fun checkPermissionStatus(context: Context, permission: String): PermissionStatus {
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        return PermissionStatus.GRANTED
    }
    val activity = context as? Activity ?: return PermissionStatus.TEMPORARILY_DENIED

    // 3. 核心判断逻辑
    val shouldShowRationale = activity.shouldShowRequestPermissionRationale(permission)
    Log.d("shouldShowRationale", "shouldShowRequestPermissionRationale: $shouldShowRationale")
    return if (shouldShowRationale) PermissionStatus.TEMPORARILY_DENIED else PermissionStatus.PERMANENTLY_DENIED
}

// 打开应用设置页面方法
private fun openAppSettings(
    context: Context,
) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    }
    context.startActivity(intent)
}