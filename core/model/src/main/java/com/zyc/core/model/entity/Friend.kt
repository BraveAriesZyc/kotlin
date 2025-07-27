package com.zyc.core.model.entity

import kotlinx.serialization.Serializable

/**
 * 好友关系实体类
 */
@Serializable
data class Friend(
    val id: Long,
    val userId: Long,
    val friendUserId: Long,
    val friendUserIdStr: String,
    val nickname: String? = null, // 好友备注名
    val groupId: Long? = null, // 好友分组ID
    val groupName: String? = null, // 好友分组名称
    val status: FriendStatus = FriendStatus.NORMAL,
    val isBlocked: Boolean = false, // 是否被屏蔽
    val isStarred: Boolean = false, // 是否特别关注
    val addTime: Long, // 添加好友时间
    val updateTime: Long
) {
    val displayName: String
        get() = nickname?.takeIf { it.isNotBlank() } ?: "未知用户"
}

/**
 * 好友状态枚举
 */
@Serializable
enum class FriendStatus(val value: Int, val displayName: String) {
    NORMAL(0, "正常"),
    PENDING(1, "待确认"),
    BLOCKED(2, "已屏蔽"),
    DELETED(3, "已删除")
}

/**
 * 好友请求实体类
 */
@Serializable
data class FriendRequest(
    val id: Long,
    val fromUserId: Long,
    val fromUserIdStr: String,
    val fromUserName: String,
    val fromUserAvatar: String? = null,
    val toUserId: Long,
    val toUserIdStr: String,
    val message: String = "", // 申请消息
    val status: FriendRequestStatus = FriendRequestStatus.PENDING,
    val createTime: Long,
    val updateTime: Long
)

/**
 * 好友请求状态枚举
 */
@Serializable
enum class FriendRequestStatus(val value: Int, val displayName: String) {
    PENDING(0, "待处理"),
    ACCEPTED(1, "已同意"),
    REJECTED(2, "已拒绝"),
    EXPIRED(3, "已过期")
}
