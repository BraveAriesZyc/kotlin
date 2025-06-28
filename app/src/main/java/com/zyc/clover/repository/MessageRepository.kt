package com.zyc.clover.repository

import com.zyc.clover.models.MessageModel
import com.zyc.clover.models.SessionMemberModel
import com.zyc.clover.models.UserModel

import kotlinx.coroutines.flow.StateFlow

interface MessageRepository {
    val conversationList: StateFlow<List<SessionMemberModel>> // 会话列表
    val messages : StateFlow<List<MessageModel>> // 消息
    fun getMessages(sessionId: String)  // 获取消息
    fun sendMessage(message: MessageModel)
    fun initApp()
}
