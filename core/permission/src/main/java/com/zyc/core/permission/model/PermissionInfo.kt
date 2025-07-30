package com.zyc.core.permission.model

/**
 * 权限信息数据类
 */
data class PermissionInfo(
    val permission: String,
    val isGranted: Boolean = false,
    val isDangerous: Boolean = false
)

/**
 * 权限状态枚举
 */
enum class PermissionStatus {
    GRANTED,        // 已授权
    DENIED,         // 被拒绝
    PERMANENTLY_DENIED, // 永久拒绝
    NOT_REQUESTED   // 未请求
}