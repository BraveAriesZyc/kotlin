package com.zyc.core.model.entity

import kotlinx.serialization.Serializable

/**
 * 会话成员实体类
 * 用于表示聊天会话中的成员信息
 */
@Serializable
data class SessionMember(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val nickname: String? = null,
    val avatar: String = "",
    val phone: String? = null,
    val email: String? = null,
    val isOnline: Boolean = false,
    val lastActiveTime: Long? = null,
    val sessionId: String = "",
    val joinTime: Long = System.currentTimeMillis(),
    val role: SessionMemberRole = SessionMemberRole.MEMBER
) {
    /**
     * 获取显示名称
     * 优先显示昵称，如果没有昵称则显示用户名
     */
    val displayName: String
        get() = nickname?.takeIf { it.isNotBlank() } ?: userName
    
    /**
     * 检查是否为管理员
     */
    val isAdmin: Boolean
        get() = role == SessionMemberRole.ADMIN || role == SessionMemberRole.OWNER
    
    /**
     * 检查是否为群主
     */
    val isOwner: Boolean
        get() = role == SessionMemberRole.OWNER
}

/**
 * 会话成员角色枚举
 */
@Serializable
enum class SessionMemberRole(val value: String, val displayName: String) {
    MEMBER("member", "成员"),
    ADMIN("admin", "管理员"),
    OWNER("owner", "群主")
}