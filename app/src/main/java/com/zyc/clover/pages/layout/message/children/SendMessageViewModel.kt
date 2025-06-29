package com.zyc.clover.pages.layout.children.message.children

import android.util.Log
import androidx.lifecycle.ViewModel

import com.zyc.clover.models.MessageModel
import com.zyc.clover.models.UserModel
import com.zyc.clover.models.enums.MessageType
import com.zyc.clover.models.enums.Role
import com.zyc.clover.repository.ChatRepository
class SendMessageViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    val  messageList = chatRepository.messages

    val user = chatRepository.user
    fun getUser(uid: String): UserModel {
        return  chatRepository.getUser(uid)
    }
    fun getMessages(sessionId: String)  {
         chatRepository.getMessages(sessionId)
    }
    fun sendMessage(message: MessageModel) {


        chatRepository.sendMessage(message)
    }
}
