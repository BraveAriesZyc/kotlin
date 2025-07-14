package com.zyc.clover.repository.impl

import com.zyc.clover.models.MessageModel
import com.zyc.clover.models.SessionMemberModel
import com.zyc.clover.models.UserModel
import com.zyc.clover.repository.ChatRepository
import com.zyc.clover.repository.MessageRepository
import com.zyc.clover.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow

// 假设这是你的 ChatRepository 实现类
class ChatRepositoryImpl(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
) : ChatRepository {
    override val user: StateFlow<UserModel> = userRepository.user
    override val conversationList: StateFlow<List<SessionMemberModel>> = messageRepository.conversationList
    override val messages: StateFlow<List<MessageModel>> = messageRepository.messages
    override fun getMessages(sessionId: String) {
        messageRepository.getMessages(sessionId)
    }

    override fun getUser(userId: String): UserModel {

        return userRepository.getFriend(userId)
    }

    override fun sendMessage(message: MessageModel) {
        messageRepository.sendMessage(message)
    }


    override fun initApp() {
        messageRepository.initApp()
        userRepository.initApp()
    }

}
