package com.zyc.core.model.entity

import kotlinx.serialization.Serializable

/**
 * 用户实体类
 */
@Serializable
data class User(
    val id: Long,
    val userId: String,
    val username: String,
    val email: String? = null,
    val phone: String? = null,
    val avatar: String? = null,
    val nickname: String? = null,
    val gender: Gender = Gender.UNKNOWN,
    val birthday: String? = null,
    val signature: String? = null,
    val background: String? = null,
    val status: UserStatus = UserStatus.NORMAL,
    val isOnline: Boolean = false,
    val lastLoginTime: Long? = null,
    val lastLoginIp: String? = null,
    val createTime: Long,
    val updateTime: Long
)

/**
 * 性别枚举
 */
@Serializable
enum class Gender(val value: Int, val displayName: String) {
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女")
}

/**
 * 用户状态枚举
 */
@Serializable
enum class UserStatus(val value: Int, val displayName: String) {
    NORMAL(0, "正常"),
    DISABLED(1, "禁用"),
    LOCKED(2, "锁定")
}

/**
 * 用户简要信息（用于列表显示）
 */
@Serializable
data class UserBrief(
    val id: Long,
    val userId: String,
    val username: String,
    val nickname: String?,
    val avatar: String?,
    val isOnline: Boolean = false
) {
    val displayName: String
        get() = nickname?.takeIf { it.isNotBlank() } ?: username

    companion object {
        fun fromUser(user: User): UserBrief {
            return UserBrief(
                id = user.id,
                userId = user.userId,
                username = user.username,
                nickname = user.nickname,
                avatar = user.avatar,
                isOnline = user.isOnline
            )
        }
    }
}
