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
import androidx.core.content.ContextCompat
import com.zyc.core.permission.model.Permission
import com.zyc.core.permission.model.PermissionManagerModel
import com.zyc.core.permission.model.PermissionStatus
import com.zyc.core.permission.utils.PermissionTracker


/**
 * 权限管理器，自动识别权限状态并处理相应逻辑
 *
 * @param permission 要检查的权限
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
        if (isGranted) {
            // 权限被授予，清除跟踪记录
            PermissionTracker.clearPermissionRecord(context, permissionStr)
            permissionStatus = PermissionStatus.GRANTED
        } else {
            // 权限被拒绝，增加拒绝次数并重新检查状态
            PermissionTracker.incrementDeniedCount(context, permissionStr)
            permissionStatus = checkPermissionStatus(context, permissionStr)
        }
    }


    return PermissionManagerModel(
        permissionStatus = permissionStatus,
        permissionLauncher = {
            when (permissionStatus) {
                PermissionStatus.TEMPORARILY_DENIED -> {
                    // 标记权限已被请求过
                    PermissionTracker.markPermissionRequested(context, permissionStr)
                    launcher.launch(permissionStr)
                }
                PermissionStatus.PERMANENTLY_DENIED -> {
                    openAppSettings(context)
                }
                PermissionStatus.GRANTED -> {
                    // 权限已授予，无需操作
                    Log.d("PermissionManager", "权限已授予: $permissionStr")
                }
            }
        }
    )


}

/**
 * 检查权限当前状态
 * 
 * 权限状态判断逻辑：
 * 1. 如果权限已授予，返回 GRANTED
 * 2. 如果权限未授予且 shouldShowRequestPermissionRationale 返回 true，说明是临时拒绝
 * 3. 如果权限未授予且 shouldShowRequestPermissionRationale 返回 false，结合请求历史判断：
 *    - 如果从未请求过，返回临时拒绝（首次请求）
 *    - 如果已请求过且被拒绝，很可能是永久拒绝
 */
private fun checkPermissionStatus(context: Context, permission: String): PermissionStatus {
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        return PermissionStatus.GRANTED
    }
    val activity = context as? Activity ?: return PermissionStatus.TEMPORARILY_DENIED

    val shouldShowRationale = activity.shouldShowRequestPermissionRationale(permission)
    val isPossiblyPermanentlyDenied = PermissionTracker.isPossiblyPermanentlyDenied(
        context, permission, shouldShowRationale
    )
    
    Log.d("PermissionManager", 
        "Permission: $permission, shouldShowRationale: $shouldShowRationale, " +
        "isPossiblyPermanentlyDenied: $isPossiblyPermanentlyDenied, " +
        "deniedCount: ${PermissionTracker.getDeniedCount(context, permission)}"
    )
    
    return when {
        shouldShowRationale -> PermissionStatus.TEMPORARILY_DENIED
        isPossiblyPermanentlyDenied -> PermissionStatus.PERMANENTLY_DENIED
        else -> PermissionStatus.TEMPORARILY_DENIED // 首次请求
    }
}

/**
 * 打开应用设置页面
 * 当权限被永久拒绝时，引导用户到设置页面手动开启权限
 */
private fun openAppSettings(
    context: Context,
) {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        context.startActivity(intent)
        Log.d("PermissionManager", "跳转到应用设置页面")
    } catch (e: Exception) {
        Log.e("PermissionManager", "无法打开应用设置页面", e)
        // 如果无法打开应用详情页面，尝试打开通用设置页面
        try {
            val fallbackIntent = Intent(Settings.ACTION_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallbackIntent)
        } catch (fallbackException: Exception) {
            Log.e("PermissionManager", "无法打开设置页面", fallbackException)
        }
    }
}