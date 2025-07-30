package com.zyc.feature.permission.model

/**
 * 权限信息数据类
 */
data class PermissionInfo(
    val permission: String,
    val name: String,
    val description: String,
    val isGranted: Boolean = false,
    val isDangerous: Boolean = false,
    val group: PermissionGroup = PermissionGroup.OTHER
)

/**
 * 权限组枚举
 */
enum class PermissionGroup(val displayName: String) {
    CAMERA("相机"),
    MICROPHONE("麦克风"),
    LOCATION("位置"),
    STORAGE("存储"),
    CONTACTS("通讯录"),
    PHONE("电话"),
    SMS("短信"),
    CALENDAR("日历"),
    SENSORS("传感器"),
    NETWORK("网络"),
    NOTIFICATION("通知"),
    OTHER("其他")
}

/**
 * 权限状态枚举
 */
enum class PermissionStatus {
    GRANTED,        // 已授权
    DENIED,         // 被拒绝
    PERMANENTLY_DENIED, // 永久拒绝
    NOT_REQUESTED   // 未请求
}