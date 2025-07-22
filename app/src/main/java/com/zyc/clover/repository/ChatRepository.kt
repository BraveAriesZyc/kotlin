package com.zyc.clover.repository


import com.zyc.data.models.MessageModel
import com.zyc.data.models.SessionMemberModel
import com.zyc.data.models.UserModel
import kotlinx.coroutines.flow.StateFlow

interface ChatRepository {
    val conversationList: StateFlow<List<SessionMemberModel>>
    val user : StateFlow<UserModel>
    val messages : StateFlow<List<MessageModel>>
    fun getMessages(sessionId: String)
    fun getUser(userId: String): UserModel?
    fun sendMessage(message: MessageModel)
    fun initApp()
}
