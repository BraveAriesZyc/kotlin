package com.zyc.core.permission.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.zyc.core.permission.model.PermissionGroup
import com.zyc.core.permission.model.PermissionInfo
import com.zyc.core.permission.model.PermissionStatus

/**
 * 权限管理器
 * 负责权限的检查、分类和状态管理
 */
class PermissionManager(private val context: Context) {
    
    /**
     * 获取所有已声明的权限信息
     */
    fun getAllDeclaredPermissions(): List<PermissionInfo> {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_PERMISSIONS
            )
            
            packageInfo.requestedPermissions?.map { permission ->
                createPermissionInfo(permission)
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * 检查单个权限状态
     */
    fun checkPermissionStatus(permission: String): PermissionStatus {
        return when (ContextCompat.checkSelfPermission(context, permission)) {
            PackageManager.PERMISSION_GRANTED -> PermissionStatus.GRANTED
            PackageManager.PERMISSION_DENIED -> PermissionStatus.DENIED
            else -> PermissionStatus.NOT_REQUESTED
        }
    }
    
    /**
     * 获取危险权限列表
     */
    fun getDangerousPermissions(): List<PermissionInfo> {
        return getAllDeclaredPermissions().filter { it.isDangerous }
    }
    
    /**
     * 按组分类权限
     */
    fun getPermissionsByGroup(): Map<PermissionGroup, List<PermissionInfo>> {
        return getAllDeclaredPermissions().groupBy { it.group }
    }
    
    /**
     * 创建权限信息对象
     */
    private fun createPermissionInfo(permission: String): PermissionInfo {
        val isGranted = checkPermissionStatus(permission) == PermissionStatus.GRANTED
        val isDangerous = isDangerousPermission(permission)
        val group = getPermissionGroup(permission)
        val name = getPermissionDisplayName(permission)
        val description = getPermissionDescription(permission)
        
        return PermissionInfo(
            permission = permission,
            name = name,
            description = description,
            isGranted = isGranted,
            isDangerous = isDangerous,
            group = group
        )
    }
    
    /**
     * 判断是否为危险权限
     */
    private fun isDangerousPermission(permission: String): Boolean {
        return when (permission) {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR -> true
            else -> false
        }
    }
    
    /**
     * 获取权限组
     */
    private fun getPermissionGroup(permission: String): PermissionGroup {
        return when {
            permission.contains("CAMERA") -> PermissionGroup.CAMERA
            permission.contains("AUDIO") || permission.contains("MICROPHONE") -> PermissionGroup.MICROPHONE
            permission.contains("LOCATION") -> PermissionGroup.LOCATION
            permission.contains("STORAGE") || permission.contains("EXTERNAL_STORAGE") -> PermissionGroup.STORAGE
            permission.contains("CONTACTS") -> PermissionGroup.CONTACTS
            permission.contains("PHONE") || permission.contains("CALL") -> PermissionGroup.PHONE
            permission.contains("SMS") -> PermissionGroup.SMS
            permission.contains("CALENDAR") -> PermissionGroup.CALENDAR
            permission.contains("SENSOR") -> PermissionGroup.SENSORS
            permission.contains("INTERNET") || permission.contains("NETWORK") -> PermissionGroup.NETWORK
            permission.contains("NOTIFICATION") -> PermissionGroup.NOTIFICATION
            else -> PermissionGroup.OTHER
        }
    }
    
    /**
     * 获取权限显示名称
     */
    private fun getPermissionDisplayName(permission: String): String {
        return when (permission) {
            Manifest.permission.CAMERA -> "相机权限"
            Manifest.permission.RECORD_AUDIO -> "录音权限"
            Manifest.permission.ACCESS_FINE_LOCATION -> "精确位置权限"
            Manifest.permission.ACCESS_COARSE_LOCATION -> "大致位置权限"
            Manifest.permission.READ_EXTERNAL_STORAGE -> "读取存储权限"
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> "写入存储权限"
            Manifest.permission.READ_CONTACTS -> "读取通讯录权限"
            Manifest.permission.WRITE_CONTACTS -> "写入通讯录权限"
            Manifest.permission.CALL_PHONE -> "拨打电话权限"
            Manifest.permission.READ_PHONE_STATE -> "读取电话状态权限"
            Manifest.permission.SEND_SMS -> "发送短信权限"
            Manifest.permission.READ_SMS -> "读取短信权限"
            Manifest.permission.READ_CALENDAR -> "读取日历权限"
            Manifest.permission.WRITE_CALENDAR -> "写入日历权限"
            Manifest.permission.INTERNET -> "网络权限"
            Manifest.permission.POST_NOTIFICATIONS -> "通知权限"
            else -> permission.substringAfterLast(".")
        }
    }
    
    /**
     * 获取权限描述
     */
    private fun getPermissionDescription(permission: String): String {
        return when (permission) {
            Manifest.permission.CAMERA -> "允许应用使用相机拍照和录制视频"
            Manifest.permission.RECORD_AUDIO -> "允许应用录制音频"
            Manifest.permission.ACCESS_FINE_LOCATION -> "允许应用获取精确的位置信息"
            Manifest.permission.ACCESS_COARSE_LOCATION -> "允许应用获取大致的位置信息"
            Manifest.permission.READ_EXTERNAL_STORAGE -> "允许应用读取外部存储中的文件"
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> "允许应用向外部存储写入文件"
            Manifest.permission.READ_CONTACTS -> "允许应用读取联系人信息"
            Manifest.permission.WRITE_CONTACTS -> "允许应用修改联系人信息"
            Manifest.permission.CALL_PHONE -> "允许应用直接拨打电话"
            Manifest.permission.READ_PHONE_STATE -> "允许应用读取电话状态和身份"
            Manifest.permission.SEND_SMS -> "允许应用发送短信"
            Manifest.permission.READ_SMS -> "允许应用读取短信内容"
            Manifest.permission.READ_CALENDAR -> "允许应用读取日历事件"
            Manifest.permission.WRITE_CALENDAR -> "允许应用添加或修改日历事件"
            Manifest.permission.INTERNET -> "允许应用访问网络"
            Manifest.permission.POST_NOTIFICATIONS -> "允许应用发送通知"
            else -> "系统权限"
        }
    }
}