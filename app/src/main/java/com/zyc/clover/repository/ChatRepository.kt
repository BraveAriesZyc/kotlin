package com.zyc.clover.repository

import com.zyc.clover.models.MessageModel
import com.zyc.clover.models.SessionMemberModel
import com.zyc.clover.models.UserModel
import kotlinx.coroutines.flow.StateFlow

interface ChatRepository {
    val conversationList: StateFlow<List<SessionMemberModel>>
    val user : StateFlow<UserModel>
    val messages : StateFlow<List<MessageModel>>
    fun getMessages(sessionId: String)
    fun getUser(uid: String): UserModel
    fun sendMessage(message: MessageModel)
    fun initApp()
}
