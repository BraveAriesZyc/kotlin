package com.zyc.clover.repository.impl


import com.zyc.clover.repository.MessageRepository
import com.zyc.data.models.FriedModel
import com.zyc.data.models.MessageModel
import com.zyc.data.models.SessionMemberModel
import com.zyc.data.models.UserModel
import com.zyc.data.models.enums.Role
import com.zyc.db.database.DatabaseRepository
import com.zyc.db.database.entity.MessageEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.emptyList

// 消息仓库实现类
class MessageRepositoryImpl(
    private val databaseRepository: DatabaseRepository
) : MessageRepository {


    // 初始化一些示例消息数据
    private val _conversationList = MutableStateFlow(
        listOf(
            SessionMemberModel(
                uid = "1",
                sessionId = "1",
                role = Role.USER.value,
                userName = "⭐残乡碎梦🍀",
                avatar = "https://zhoudaxian.oss-rg-china-mainland.aliyuncs.com/avatar1.jpg"
            ),
            SessionMemberModel(
                uid = "2",
                sessionId = "2",
                role = Role.ASSISTANT.value,
                userName = "🍀不思梦❀",
                avatar = "https://zhoudaxian.oss-rg-china-mainland.aliyuncs.com/avatar1.jpg"
            ),
        )
    )
    private val _messageList = MutableStateFlow<List<MessageModel>>(emptyList())
    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())


    override val conversationList: StateFlow<List<SessionMemberModel>> = _conversationList
    override val messages: StateFlow<List<MessageModel>> = _messages


    // 发送新消息
    override fun sendMessage(message: MessageModel) {
        val i = databaseRepository.messageDao.insertMessage(
            MessageEntity(
                userId = message.userId,
                type = message.type,
                content = message.content,
                role = message.role,
                imageUrl = message.imageUrl,
                videoUrl = message.videoUrl,
                audioUrl = message.audioUrl,
                fileUrl = message.fileUrl,
                sessionId = message.sessionId,
                timestamp = message.timestamp
            )
        )
        val newList = mutableListOf<MessageModel>()
        newList.add(message)
        _messages.update { currentList ->
            mutableListOf(message).apply {
                addAll(currentList)
            }
        }
    }

    override fun getMessages(sessionId: String) {
        _messages.value = _messageList.value.filter { it.sessionId == sessionId }
    }

    override fun initApp() {
        _messageList.value = databaseRepository.messageDao.selectMessage().map {
            MessageModel(
                id = it.id,
                userId = it.userId,
                type = it.type,
                content = it.content,
                role = it.role,
                imageUrl = it.imageUrl,
                videoUrl = it.videoUrl,
                audioUrl = it.audioUrl,
                fileUrl = it.fileUrl,
                sessionId = it.sessionId,
                timestamp = it.timestamp
            )
        }

    }

}
