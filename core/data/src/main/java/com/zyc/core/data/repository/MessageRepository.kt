package com.zyc.core.data.repository

import com.zyc.core.model.entity.Message
import com.zyc.core.model.entity.MessageStatus
import kotlinx.coroutines.flow.Flow

/**
 * 消息数据仓库接口
 */
interface MessageRepository {
    
    /**
     * 发送消息
     */
    suspend fun sendMessage(message: Message): Long
    
    /**
     * 获取消息列表
     */
    suspend fun getMessages(chatId: Long, offset: Int = 0, limit: Int = 50): Flow<List<Message>>
    
    /**
     * 根据ID获取消息
     */
    suspend fun getMessageById(messageId: Long): Message?
    
    /**
     * 更新消息状态
     */
    suspend fun updateMessageStatus(messageId: Long, status: MessageStatus)
    
    /**
     * 删除消息
     */
    suspend fun deleteMessage(messageId: Long)
    
    /**
     * 撤回消息
     */
    suspend fun recallMessage(messageId: Long)
    
    /**
     * 标记消息为已读
     */
    suspend fun markMessageAsRead(messageId: Long)
    
    /**
     * 搜索消息
     */
    suspend fun searchMessages(chatId: Long, keyword: String): Flow<List<Message>>
    
    /**
     * 获取未读消息数量
     */
    suspend fun getUnreadMessageCount(chatId: Long): Int
}