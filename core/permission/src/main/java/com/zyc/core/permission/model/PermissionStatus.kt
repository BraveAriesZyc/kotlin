package com.zyc.core.permission.model
/**
 * 权限状态枚举
 */
enum class PermissionStatus(val description: String) {
    GRANTED("已授予"),         // 已授予
    TEMPORARILY_DENIED("临时拒绝"),  // 临时拒绝（可再次请求）
    PERMANENTLY_DENIED("永久拒绝")   // 永久拒绝（需前往设置）
}
