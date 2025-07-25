package com.zyc.core.model.entity

import kotlinx.serialization.Serializable

/**
 * 用户实体类
 */
@Serializable
data class User(
    val id: Long,
    val username: String,
    val email: String,
    val phone: String? = null,
    val avatar: String? = null,
    val nickname: String? = null,
    val gender: Gender = Gender.UNKNOWN,
    val birthday: String? = null,
    val signature: String? = null,
    val isOnline: Boolean = false,
    val lastLoginTime: Long? = null,
    val createTime: Long,
    val updateTime: Long
)

/**
 * 性别枚举
 */
@Serializable
enum class Gender {
    MALE,
    FEMALE,
    UNKNOWN
}

/**
 * 用户简要信息（用于列表显示）
 */
@Serializable
data class UserBrief(
    val id: Long,
    val username: String,
    val nickname: String?,
    val avatar: String?,
    val isOnline: Boolean = false
) {
    val displayName: String
        get() = nickname?.takeIf { it.isNotBlank() } ?: username
}