package com.zyc.db.database.entity

import kotlinx.serialization.Serializable
import com.zyc.core.model.entity.Message
import com.zyc.core.model.entity.MessageType
import com.zyc.core.model.entity.MessageStatus
import com.zyc.core.model.entity.MessageMetadata

@Serializable
data class MessageEntity(
    val id: Long = 0,
    val messageId: String,
    val senderId: Long,
    val senderUserId: String,
    val receiverId: Long?,
    val receiverUserId: String?,
    val chatId: Long,
    val sessionId: String,
    val type: Int, // 对应 MessageType.value
    val content: String?,
    val imageUrl: String?,
    val audioUrl: String?,
    val fileUrl: String?,
    val fileName: String?,
    val fileSize: Long?,
    val duration: Long?,
    val thumbnailUrl: String?,
    val width: Int?,
    val height: Int?,
    val mimeType: String?,
    val status: Int = 2, // 对应 MessageStatus.value，默认已发送
    val timestamp: Long,
    val replyToId: Long?,
    val isDeleted: Boolean = false,
    val editedAt: Long?
) {
    fun toMessage(): Message {
        return Message(
            id = id,
            messageId = messageId,
            senderId = senderId,
            senderUserId = senderUserId,
            receiverId = receiverId,
            receiverUserId = receiverUserId,
            chatId = chatId,
            sessionId = sessionId,
            type = MessageType.entries.find { it.value == type } ?: MessageType.TEXT,
            content = content ?: "",
            metadata = MessageMetadata(
                imageUrl = imageUrl,
                audioUrl = audioUrl,
                fileUrl = fileUrl,
                fileName = fileName,
                fileSize = fileSize,
                duration = duration,
                thumbnailUrl = thumbnailUrl,
                width = width,
                height = height,
                mimeType = mimeType
            ),
            status = MessageStatus.entries.find { it.value == status } ?: MessageStatus.SENT,
            timestamp = timestamp,
            isDeleted = isDeleted,
            editedAt = editedAt
        )
    }

    companion object {
        fun fromMessage(message: Message): MessageEntity {
            return MessageEntity(
                id = message.id,
                messageId = message.messageId,
                senderId = message.senderId,
                senderUserId = message.senderUserId,
                receiverId = message.receiverId,
                receiverUserId = message.receiverUserId,
                chatId = message.chatId,
                sessionId = message.sessionId,
                type = message.type.value,
                content = message.content,
                imageUrl = message.metadata?.imageUrl,
                audioUrl = message.metadata?.audioUrl,
                fileUrl = message.metadata?.fileUrl,
                fileName = message.metadata?.fileName,
                fileSize = message.metadata?.fileSize,
                duration = message.metadata?.duration,
                thumbnailUrl = message.metadata?.thumbnailUrl,
                width = message.metadata?.width,
                height = message.metadata?.height,
                mimeType = message.metadata?.mimeType,
                status = message.status.value,
                timestamp = message.timestamp,

                isDeleted = message.isDeleted,
                editedAt = message.editedAt,
                replyToId =  message.replyToMessageId
            )
        }
    }
}
