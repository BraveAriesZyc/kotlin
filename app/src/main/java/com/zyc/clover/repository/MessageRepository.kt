package com.zyc.clover.repository


import com.zyc.data.models.MessageModel
import com.zyc.data.models.SessionMemberModel
import kotlinx.coroutines.flow.StateFlow

interface MessageRepository {
    val conversationList: StateFlow<List<SessionMemberModel>> // 会话列表
    val messages : StateFlow<List<MessageModel>> // 消息
    fun getMessages(sessionId: String)  // 获取消息
    fun sendMessage(message: MessageModel)
    fun initApp()
}
