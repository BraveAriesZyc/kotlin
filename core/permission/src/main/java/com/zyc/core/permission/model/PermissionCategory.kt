package com.zyc.core.permission.model

import android.Manifest

/**
 * 权限分类枚举
 */
enum class PermissionCategory {
    MEDIA,          // 媒体权限（相机、麦克风）
    LOCATION,       // 位置权限
    STORAGE,        // 存储权限
    COMMUNICATION,  // 通讯权限（联系人、电话、短信）
    CALENDAR,       // 日历权限
    SYSTEM,         // 系统权限（网络、通知等）
    OTHER           // 其他权限
}

/**
 * 权限分类信息
 */
data class PermissionCategoryInfo(
    val category: PermissionCategory,
    val displayName: String,
    val description: String,
    val permissions: List<String>
)

/**
 * 权限分类工具类
 */
object PermissionCategoryHelper {
    
    /**
     * 获取分类对应的图标字符
     */
    fun getCategoryIcon(category: PermissionCategory): String {
        return when (category) {
            PermissionCategory.MEDIA -> "\uEE5A"        // 媒体图标
            PermissionCategory.LOCATION -> "\uEE7B"     // 位置图标
            PermissionCategory.STORAGE -> "\uEE8F"      // 存储图标
            PermissionCategory.COMMUNICATION -> "\uEE4A" // 通讯图标
            PermissionCategory.CALENDAR -> "\uEE3A"     // 日历图标
            PermissionCategory.SYSTEM -> "\uEE9A"       // 系统图标
            PermissionCategory.OTHER -> "\uEE2A"        // 其他图标
        }
    }
    
    /**
     * 获取分类对应的颜色（用于IconBackground）
     */
    fun getCategoryColorHex(category: PermissionCategory): Long {
        return when (category) {
            PermissionCategory.MEDIA -> 0xFF6366F1        // 紫色
            PermissionCategory.LOCATION -> 0xFF10B981     // 绿色
            PermissionCategory.STORAGE -> 0xFFF59E0B      // 橙色
            PermissionCategory.COMMUNICATION -> 0xFF3B82F6 // 蓝色
            PermissionCategory.CALENDAR -> 0xFFEF4444     // 红色
            PermissionCategory.SYSTEM -> 0xFF8B5CF6       // 紫罗兰色
            PermissionCategory.OTHER -> 0xFF6B7280        // 灰色
        }
    }
    
    /**
     * 获取权限的简化显示名称
     */
    fun getPermissionSimpleName(permission: String): String {
        return when (permission) {
            Manifest.permission.CAMERA -> "相机"
            Manifest.permission.RECORD_AUDIO -> "录音"
            Manifest.permission.ACCESS_FINE_LOCATION -> "精确位置"
            Manifest.permission.ACCESS_COARSE_LOCATION -> "大致位置"
            Manifest.permission.ACCESS_BACKGROUND_LOCATION -> "后台位置"
            Manifest.permission.READ_EXTERNAL_STORAGE -> "读取存储"
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> "写入存储"
            Manifest.permission.MANAGE_EXTERNAL_STORAGE -> "管理存储"
            Manifest.permission.READ_CONTACTS -> "读取联系人"
            Manifest.permission.WRITE_CONTACTS -> "写入联系人"
            Manifest.permission.GET_ACCOUNTS -> "获取账户"
            Manifest.permission.CALL_PHONE -> "拨打电话"
            Manifest.permission.READ_PHONE_STATE -> "读取电话状态"
            Manifest.permission.READ_PHONE_NUMBERS -> "读取电话号码"
            Manifest.permission.ANSWER_PHONE_CALLS -> "接听电话"
            Manifest.permission.SEND_SMS -> "发送短信"
            Manifest.permission.READ_SMS -> "读取短信"
            Manifest.permission.RECEIVE_SMS -> "接收短信"
            Manifest.permission.RECEIVE_MMS -> "接收彩信"
            Manifest.permission.READ_CALENDAR -> "读取日历"
            Manifest.permission.WRITE_CALENDAR -> "写入日历"
            Manifest.permission.INTERNET -> "网络访问"
            Manifest.permission.ACCESS_NETWORK_STATE -> "网络状态"
            Manifest.permission.ACCESS_WIFI_STATE -> "WiFi状态"
            Manifest.permission.CHANGE_WIFI_STATE -> "修改WiFi"
            Manifest.permission.VIBRATE -> "震动"
            Manifest.permission.WAKE_LOCK -> "保持唤醒"
            Manifest.permission.SYSTEM_ALERT_WINDOW -> "悬浮窗"
            Manifest.permission.WRITE_SETTINGS -> "修改设置"
            else -> permission.substringAfterLast(".")
        }
    }
    
    /**
     * 获取权限所属分类
     */
    fun getPermissionCategory(permission: String): PermissionCategory {
        return when (permission) {
            // 媒体权限（相机、麦克风等）
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO -> PermissionCategory.MEDIA
            
            // 位置权限
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION -> PermissionCategory.LOCATION
            
            // 存储权限
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE -> PermissionCategory.STORAGE
            
            // 通讯权限（联系人、电话、短信等）
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS -> PermissionCategory.COMMUNICATION
            
            // 日历权限
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR -> PermissionCategory.CALENDAR
            
            // 系统权限（网络、通知、系统设置等）
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.VIBRATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.WRITE_SETTINGS -> PermissionCategory.SYSTEM
            
            else -> PermissionCategory.OTHER
        }
    }
    
    /**
     * 获取分类的显示名称
     */
    fun getCategoryDisplayName(category: PermissionCategory): String {
        return when (category) {
            PermissionCategory.MEDIA -> "媒体权限"
            PermissionCategory.LOCATION -> "位置权限"
            PermissionCategory.STORAGE -> "存储权限"
            PermissionCategory.COMMUNICATION -> "通讯权限"
            PermissionCategory.CALENDAR -> "日历权限"
            PermissionCategory.SYSTEM -> "系统权限"
            PermissionCategory.OTHER -> "其他权限"
        }
    }
    
    /**
     * 获取分类的描述
     */
    fun getCategoryDescription(category: PermissionCategory): String {
        return when (category) {
            PermissionCategory.MEDIA -> "访问相机、麦克风等媒体设备"
            PermissionCategory.LOCATION -> "获取设备位置信息"
            PermissionCategory.STORAGE -> "读写设备存储空间"
            PermissionCategory.COMMUNICATION -> "访问联系人、电话、短信等通讯功能"
            PermissionCategory.CALENDAR -> "访问日历信息"
            PermissionCategory.SYSTEM -> "网络连接、系统设置等系统级权限"
            PermissionCategory.OTHER -> "其他未分类权限"
        }
    }
    
    /**
     * 获取所有权限分类信息
     */
    fun getAllCategories(): List<PermissionCategoryInfo> {
        return PermissionCategory.values().map { category ->
            PermissionCategoryInfo(
                category = category,
                displayName = getCategoryDisplayName(category),
                description = getCategoryDescription(category),
                permissions = getPermissionsByCategory(category)
            )
        }
    }
    
    /**
     * 根据分类获取权限列表
     */
    fun getPermissionsByCategory(category: PermissionCategory): List<String> {
        return when (category) {
            PermissionCategory.MEDIA -> listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
            PermissionCategory.LOCATION -> listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            PermissionCategory.STORAGE -> listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            )
            PermissionCategory.COMMUNICATION -> listOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.ANSWER_PHONE_CALLS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.RECEIVE_MMS
            )
            PermissionCategory.CALENDAR -> listOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
            )
            PermissionCategory.SYSTEM -> listOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.VIBRATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.WRITE_SETTINGS
            )
            PermissionCategory.OTHER -> emptyList()
        }
    }
}