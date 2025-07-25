package com.zyc.core.data.repository.impl

import com.zyc.core.data.repository.MessageRepository
import com.zyc.core.model.entity.Message
import com.zyc.core.model.entity.MessageStatus
import com.zyc.core.model.entity.MessageType

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * 消息数据仓库实现类
 */
class MessageRepositoryImpl : MessageRepository {
    
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    private var nextMessageId = 1L
    
    override suspend fun initApp() {
        // 初始化一些模拟数据
         _messages.value = listOf(
             Message(
                 id = nextMessageId++,
                 messageId = "msg_${nextMessageId - 1}",
                 chatId = 1L,
                 sessionId = "1",
                 senderId = 1L,
                 senderUserId = "user1",
                 receiverId = 2L,
                 receiverUserId = "user2",
                 content = "Hello, how are you?",
                 type = MessageType.TEXT,
                 status = MessageStatus.SENT,
                 timestamp = System.currentTimeMillis() - 3600000
             ),
             Message(
                 id = nextMessageId++,
                 messageId = "msg_${nextMessageId - 1}",
                 chatId = 1L,
                 sessionId = "1",
                 senderId = 2L,
                 senderUserId = "user2",
                 receiverId = 1L,
                 receiverUserId = "user1",
                 content = "I'm doing great, thanks for asking!",
                 type = MessageType.TEXT,
                 status = MessageStatus.SENT,
                 timestamp = System.currentTimeMillis() - 3000000
             )
         )
    }
    
    override suspend fun sendMessage(message: Message): Long {
         val newMessage = message.copy(
             id = nextMessageId++,
             messageId = "msg_${nextMessageId - 1}",
             timestamp = System.currentTimeMillis(),
             status = MessageStatus.SENT
         )
         _messages.value = _messages.value + newMessage
         return newMessage.id
     }
    
    override suspend fun getMessages(chatId: Long, offset: Int, limit: Int): Flow<List<Message>> {
         return _messages.asStateFlow().map { messages ->
             messages.filter { it.chatId == chatId }
                 .sortedByDescending { it.timestamp }
                 .drop(offset)
                 .take(limit)
         }
     }
    
    override suspend fun getMessageById(messageId: Long): Message? {
        return _messages.value.find { it.id == messageId }
    }
    
    override suspend fun updateMessageStatus(messageId: Long, status: MessageStatus) {
        _messages.value = _messages.value.map { message ->
            if (message.id == messageId) {
                message.copy(status = status)
            } else {
                message
            }
        }
    }
    
    override suspend fun deleteMessage(messageId: Long) {
        _messages.value = _messages.value.filter { it.id != messageId }
    }
    
    override suspend fun recallMessage(messageId: Long) {
        _messages.value = _messages.value.map { message ->
            if (message.id == messageId) {
                message.copy(
                    content = "[消息已撤回]",
                    status = MessageStatus.RECALLED
                )
            } else {
                message
            }
        }
    }
    
    override suspend fun markMessageAsRead(messageId: Long) {
        updateMessageStatus(messageId, MessageStatus.READ)
    }
    
    override suspend fun searchMessages(chatId: Long, keyword: String): Flow<List<Message>> {
         return _messages.asStateFlow().map { messages ->
             messages.filter { message ->
                 message.chatId == chatId && 
                 message.content.contains(keyword, ignoreCase = true)
             }.sortedByDescending { it.timestamp }
         }
     }
    
    override suspend fun getUnreadMessageCount(chatId: Long): Int {
         return _messages.value.count { message ->
             message.chatId == chatId && 
             message.status != MessageStatus.READ
         }
     }
}