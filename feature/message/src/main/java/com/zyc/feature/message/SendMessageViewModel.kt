package com.zyc.feature.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyc.core.data.repository.impl.ChatRepositoryImpl
import com.zyc.core.model.entity.Message
import com.zyc.core.model.entity.SessionMember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
class SendMessageViewModel(
    private val chatRepository: ChatRepositoryImpl
) : ViewModel() {

    private val _messageList = MutableStateFlow<List<Message>>(emptyList())
    val messageList: StateFlow<List<Message>> = _messageList.asStateFlow()

    val user = chatRepository.currentUser
    fun getUser(uid: String): SessionMember? {
        return chatRepository.getUser(uid)
    }

    fun getMessages(sessionId: String) {
        viewModelScope.launch {
            // 将sessionId转换为chatId (Long类型)
            val chatId = sessionId.toLongOrNull() ?: 1L
            chatRepository.getMessagesForChat(chatId, 100).collect { messages ->
                _messageList.value = messages
            }
        }
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            // 将sessionId转换为chatId (Long类型)
            val chatId = message.sessionId?.toLongOrNull() ?: 1L
            chatRepository.sendMessage(chatId, message)
            // 重新获取消息列表
            getMessages(message.sessionId ?: "1")
        }
    }
}