package com.zyc.core.data.repository

import com.zyc.core.model.entity.Chat
import com.zyc.core.model.entity.Message
import kotlinx.coroutines.flow.Flow

/**
 * 聊天数据仓库接口
 */
interface ChatRepository {
    
    /**
     * 获取所有聊天会话
     */
    suspend fun getAllChats(): Flow<List<Chat>>
    
    /**
     * 根据ID获取聊天会话
     */
    suspend fun getChatById(chatId: Long): Chat?
    
    /**
     * 创建新的聊天会话
     */
    suspend fun createChat(chat: Chat): Long
    
    /**
     * 更新聊天会话
     */
    suspend fun updateChat(chat: Chat)
    
    /**
     * 删除聊天会话
     */
    suspend fun deleteChat(chatId: Long)
    
    /**
     * 获取聊天会话的消息列表
     */
    suspend fun getMessagesForChat(chatId: Long, limit: Int = 50): Flow<List<Message>>
    
    /**
     * 标记聊天为已读
     */
    suspend fun markChatAsRead(chatId: Long)
    
    /**
     * 置顶/取消置顶聊天
     */
    suspend fun toggleChatTop(chatId: Long, isTop: Boolean)
    
    /**
     * 静音/取消静音聊天
     */
    suspend fun toggleChatMute(chatId: Long, isMuted: Boolean)
}