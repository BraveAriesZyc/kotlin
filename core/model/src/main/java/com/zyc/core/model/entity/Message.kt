package com.zyc.core.model.entity

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

/**
 * 消息实体类
 */
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Message(
    val id: Long,
    val messageId: String,
    val chatId: Long,
    val sessionId: String,
    val senderId: Long,
    val senderUserId: String,
    val receiverId: Long? = null,
    val receiverUserId: String? = null,
    val content: String,
    val type: MessageType,
    val status: MessageStatus = MessageStatus.SENDING,
    val timestamp: Long,
    val replyToMessageId: Long? = null,
    val metadata: MessageMetadata? = null,
    val isDeleted: Boolean = false,
    val editedAt: Long? = null
) {
    val isEdited: Boolean
        get() = editedAt != null

    val isMedia: Boolean
        get() = type in listOf(MessageType.IMAGE, MessageType.VIDEO, MessageType.AUDIO, MessageType.FILE)
}

/**
 * 消息类型枚举
 */
@Serializable
enum class MessageType(val value: Int, val displayName: String) {
    TEXT(0, "文本"),
    IMAGE(1, "图片"),
    VIDEO(2, "视频"),
    AUDIO(3, "语音"),
    FILE(4, "文件"),
    LOCATION(5, "位置"),
    EMOJI(6, "表情"),
    STICKER(7, "贴纸"),
    SYSTEM(8, "系统消息")
}

/**
 * 消息状态枚举
 */
@Serializable
enum class MessageStatus(val value: Int, val displayName: String) {
    DRAFT(0, "草稿"),
    SENDING(1, "发送中"),
    SENT(2, "已发送"),
    DELIVERED(3, "已送达"),
    READ(4, "已读"),
    FAILED(5, "发送失败"),
    RECALLED(6, "已撤回")
}

/**
 * 消息元数据
 */
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MessageMetadata(
    val imageUrl: String? = null,
    val videoUrl: String? = null,
    val audioUrl: String? = null,
    val fileUrl: String? = null,
    val fileName: String? = null,
    val fileSize: Long? = null,
    val filePath: String? = null,
    val thumbnailPath: String? = null,
    val thumbnailUrl: String? = null, // 缩略图URL
    val duration: Long? = null, // 音频/视频时长（秒）
    val width: Int? = null, // 图片/视频宽度
    val height: Int? = null, // 图片/视频高度
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val mimeType: String? = null // MIME类型
) {
    val hasMedia: Boolean
        get() = imageUrl != null || videoUrl != null || audioUrl != null || fileUrl != null
}

/**
 * 聊天会话实体类
 */
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Chat(
    val id: Long,
    val chatId: String,
    val sessionId: String,
    val type: ChatType,
    val name: String?,
    val avatar: String? = null,
    val description: String? = null,
    val participantIds: List<Long>,
    val participantUserIds: List<String>,
    val lastMessageId: Long?,
    val lastMessageContent: String?,
    val lastMessageTime: Long?,
    val lastMessageSenderId: Long?,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val isArchived: Boolean = false,
    val createTime: Long,
    val updateTime: Long
) {
    val isGroup: Boolean
        get() = type == ChatType.GROUP

    val participantCount: Int
        get() = participantIds.size

    val hasUnread: Boolean
        get() = unreadCount > 0
}

/**
 * 聊天类型枚举
 */
@Serializable
enum class ChatType(val value: Int, val displayName: String) {
    PRIVATE(0, "私聊"),
    GROUP(1, "群聊"),
    CHANNEL(2, "频道"),
    SYSTEM(3, "系统消息")
}
