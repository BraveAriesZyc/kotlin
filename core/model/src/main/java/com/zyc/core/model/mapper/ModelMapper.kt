package com.zyc.core.model.mapper

import com.zyc.core.model.entity.*

/**
 * 模型映射工具类
 * 提供各种数据模型之间的转换方法
 */
object ModelMapper {

    /**
     * 创建用户简要信息列表
     */
    fun List<User>.toBriefList(): List<UserBrief> {
        return map { UserBrief.fromUser(it) }
    }

    /**
     * 根据消息类型判断是否为媒体消息
     */
    fun MessageType.isMediaType(): Boolean {
        return this in listOf(MessageType.IMAGE, MessageType.AUDIO, MessageType.FILE)
    }

    /**
     * 根据消息状态判断是否为最终状态
     */
    fun MessageStatus.isFinalStatus(): Boolean {
        return this in listOf(MessageStatus.READ, MessageStatus.FAILED, MessageStatus.RECALLED)
    }

    /**
     * 获取聊天显示名称
     */
    fun Chat.getDisplayName(currentUserId: String): String {
        return when {
            !name.isNullOrBlank() -> name
            type == ChatType.PRIVATE -> {
                // 私聊显示对方用户名
                val otherUserId = participantUserIds.find { it != currentUserId }
                otherUserId ?: "未知用户"
            }
            type == ChatType.GROUP -> "群聊"
            type == ChatType.CHANNEL -> "频道"
            else -> "聊天"
        }
    }

    /**
     * 获取最后消息的简短显示文本
     */
    fun Chat.getLastMessageDisplay(): String {
        return when {
            lastMessageContent.isNullOrBlank() -> "暂无消息"
            lastMessageContent.length > 50 -> lastMessageContent.take(50) + "..."
            else -> lastMessageContent
        }
    }

    /**
     * 格式化文件大小
     */
    fun Long.formatFileSize(): String {
        val kb = 1024
        val mb = kb * 1024
        val gb = mb * 1024

        return when {
            this >= gb -> String.format("%.1f GB", this.toDouble() / gb)
            this >= mb -> String.format("%.1f MB", this.toDouble() / mb)
            this >= kb -> String.format("%.1f KB", this.toDouble() / kb)
            else -> "$this B"
        }
    }

    /**
     * 格式化时长（秒转为分:秒格式）
     */
    fun Long.formatDuration(): String {
        val minutes = this / 60
        val seconds = this % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    /**
     * 检查用户是否在线
     */
    fun User.isCurrentlyOnline(): Boolean {
        return isOnline && lastLoginTime != null && 
               (System.currentTimeMillis() - lastLoginTime) < 5 * 60 * 1000 // 5分钟内活跃
    }

    /**
     * 获取好友显示名称
     */
    fun Friend.getDisplayName(): String {
        return nickname?.takeIf { it.isNotBlank() } ?: "好友"
    }

    /**
     * 检查好友关系是否有效
     */
    fun Friend.isValidRelation(): Boolean {
        return status == FriendStatus.NORMAL && !isBlocked
    }

    /**
     * 创建系统消息
     */
    fun createSystemMessage(
        chatId: Long,
        sessionId: String,
        content: String,
        timestamp: Long = System.currentTimeMillis()
    ): Message {
        return Message(
            id = 0,
            messageId = "sys_${System.currentTimeMillis()}",
            senderId = 0,
            senderUserId = "system",
            receiverId = null,
            receiverUserId = null,
            chatId = chatId,
            sessionId = sessionId,
            type = MessageType.SYSTEM,
            content = content,
            metadata = null,
            status = MessageStatus.SENT,
            timestamp = timestamp,
            replyToMessageId = null,
            isDeleted = false,
            editedAt = null
        )
    }
}