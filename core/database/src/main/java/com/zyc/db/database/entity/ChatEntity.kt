package com.zyc.db.database.entity

import kotlinx.serialization.Serializable
import com.zyc.core.model.entity.Chat
import com.zyc.core.model.entity.ChatType

@Serializable
data class ChatEntity(
    val id: Long = 0,
    val chatId: String,
    val sessionId: String,
    val type: Int, // 对应 ChatType.value
    val name: String?,
    val avatar: String?,
    val description: String?,
    val participantIds: String, // JSON字符串存储List<Long>
    val participantUserIds: String, // JSON字符串存储List<String>
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
    fun toChat(): Chat {
        return Chat(
            id = id,
            chatId = chatId,
            sessionId = sessionId,
            type = ChatType.values().find { it.value == type } ?: ChatType.PRIVATE,
            name = name,
            avatar = avatar,
            description = description,
            participantIds = parseParticipantIds(participantIds),
            participantUserIds = parseParticipantUserIds(participantUserIds),
            lastMessageId = lastMessageId,
            lastMessageContent = lastMessageContent,
            lastMessageTime = lastMessageTime,
            lastMessageSenderId = lastMessageSenderId,
            unreadCount = unreadCount,
            isPinned = isPinned,
            isMuted = isMuted,
            isArchived = isArchived,
            createTime = createTime,
            updateTime = updateTime
        )
    }

    companion object {
        fun fromChat(chat: Chat): ChatEntity {
            return ChatEntity(
                id = chat.id,
                chatId = chat.chatId,
                sessionId = chat.sessionId,
                type = chat.type.value,
                name = chat.name,
                avatar = chat.avatar,
                description = chat.description,
                participantIds = serializeParticipantIds(chat.participantIds),
                participantUserIds = serializeParticipantUserIds(chat.participantUserIds),
                lastMessageId = chat.lastMessageId,
                lastMessageContent = chat.lastMessageContent,
                lastMessageTime = chat.lastMessageTime,
                lastMessageSenderId = chat.lastMessageSenderId,
                unreadCount = chat.unreadCount,
                isPinned = chat.isPinned,
                isMuted = chat.isMuted,
                isArchived = chat.isArchived,
                createTime = chat.createTime,
                updateTime = chat.updateTime
            )
        }

        private fun parseParticipantIds(json: String): List<Long> {
            return try {
                json.removeSurrounding("[", "]")
                    .split(",")
                    .map { it.trim().toLong() }
                    .filter { it > 0 }
            } catch (e: Exception) {
                emptyList()
            }
        }

        private fun parseParticipantUserIds(json: String): List<String> {
            return try {
                json.removeSurrounding("[", "]")
                    .split(",")
                    .map { it.trim().removeSurrounding("\"") }
                    .filter { it.isNotBlank() }
            } catch (e: Exception) {
                emptyList()
            }
        }

        private fun serializeParticipantIds(ids: List<Long>): String {
            return ids.joinToString(",", "[", "]")
        }

        private fun serializeParticipantUserIds(ids: List<String>): String {
            return ids.joinToString(",", "[", "]") { "\"$it\"" }
        }
    }
}