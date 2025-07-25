package com.zyc.feature.message

import androidx.lifecycle.ViewModel


import com.zyc.data.repository.ChatRepository
import com.zyc.data.models.MessageModel
import com.zyc.data.models.UserModel

class SendMessageViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    val messageList = chatRepository.messages

    val user = chatRepository.user
    fun getUser(uid: String): UserModel {
        return chatRepository.getUser(uid) ?: UserModel()
    }

    fun getMessages(sessionId: String) {
        chatRepository.getMessages(sessionId)
    }

    fun sendMessage(message: MessageModel) {
        chatRepository.sendMessage(message)
    }
}