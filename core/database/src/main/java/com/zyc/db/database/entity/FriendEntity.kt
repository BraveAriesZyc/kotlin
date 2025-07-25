package com.zyc.db.database.entity

import kotlinx.serialization.Serializable
import com.zyc.core.model.entity.Friend
import com.zyc.core.model.entity.FriendStatus

@Serializable
data class FriendEntity(
    val id: Long = 0,
    val userId: Long,
    val friendUserId: Long,
    val friendUserIdStr: String,
    val nickname: String?, // 好友备注名
    val groupId: Long?,
    val groupName: String?,
    val status: Int = 0, // 对应 FriendStatus.value
    val isBlocked: Boolean = false,
    val isStarred: Boolean = false,
    val addTime: Long,
    val updateTime: Long
) {
    fun toFriend(): Friend {
        return Friend(
            id = id,
            userId = userId,
            friendUserId = friendUserId,
            friendUserIdStr = friendUserIdStr,
            nickname = nickname,
            groupId = groupId,
            groupName = groupName,
            status = FriendStatus.values().find { it.value == status } ?: FriendStatus.NORMAL,
            isBlocked = isBlocked,
            isStarred = isStarred,
            addTime = addTime,
            updateTime = updateTime
        )
    }

    companion object {
        fun fromFriend(friend: Friend): FriendEntity {
            return FriendEntity(
                id = friend.id,
                userId = friend.userId,
                friendUserId = friend.friendUserId,
                friendUserIdStr = friend.friendUserIdStr,
                nickname = friend.nickname,
                groupId = friend.groupId,
                groupName = friend.groupName,
                status = friend.status.value,
                isBlocked = friend.isBlocked,
                isStarred = friend.isStarred,
                addTime = friend.addTime,
                updateTime = friend.updateTime
            )
        }
    }
}
