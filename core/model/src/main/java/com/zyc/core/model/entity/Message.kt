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
    val chatId: Long,
    val senderId: Long,
    val content: String,
    val type: MessageType,
    val status: MessageStatus = MessageStatus.SENDING,
    val timestamp: Long,
    val replyToMessageId: Long? = null,
    val metadata: MessageMetadata? = null
)

/**
 * 消息类型
 */
@Serializable
enum class MessageType {
    TEXT,           // 文本消息
    IMAGE,          // 图片消息
    VIDEO,          // 视频消息
    AUDIO,          // 音频消息
    FILE,           // 文件消息
    LOCATION,       // 位置消息
    SYSTEM          // 系统消息
}

/**
 * 消息状态
 */
@Serializable
enum class MessageStatus {
    SENDING,        // 发送中
    SENT,           // 已发送
    DELIVERED,      // 已送达
    READ,           // 已读
    FAILED          // 发送失败
}

/**
 * 消息元数据
 */
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MessageMetadata(
    val fileName: String? = null,
    val fileSize: Long? = null,
    val filePath: String? = null,
    val thumbnailPath: String? = null,
    val duration: Long? = null,
    val width: Int? = null,
    val height: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null
)

/**
 * 聊天会话实体类
 */
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Chat(
    val id: Long,
    val name: String,
    val type: ChatType,
    val avatar: String? = null,
    val description: String? = null,
    val memberCount: Int = 0,
    val lastMessage: Message? = null,
    val unreadCount: Int = 0,
    val isTop: Boolean = false,
    val isMuted: Boolean = false,
    val createTime: Long,
    val updateTime: Long
)

/**
 * 聊天类型
 */
@Serializable
enum class ChatType {
    PRIVATE,        // 私聊
    GROUP,          // 群聊
    SYSTEM          // 系统会话
}
