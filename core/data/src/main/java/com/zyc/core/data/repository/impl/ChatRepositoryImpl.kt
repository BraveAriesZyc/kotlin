package com.zyc.core.data.repository.impl

import com.zyc.core.data.repository.ChatRepository
import com.zyc.core.model.entity.Chat
import com.zyc.core.model.entity.ChatType
import com.zyc.core.model.entity.Message
import com.zyc.core.model.entity.SessionMember
import com.zyc.core.model.entity.SessionMemberRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

/**
 * 聊天数据仓库实现类
 */
class ChatRepositoryImpl : ChatRepository {
    
    // 会话成员列表
    private val _conversationList = MutableStateFlow<List<SessionMember>>(emptyList())
    val conversationList: StateFlow<List<SessionMember>> = _conversationList.asStateFlow()
    
    // 当前用户
    private val _currentUser = MutableStateFlow<SessionMember?>(null)
    val currentUser: StateFlow<SessionMember?> = _currentUser.asStateFlow()
    
    // 用户缓存
    private val userCache = mutableMapOf<String, SessionMember>()
    
    // 聊天会话缓存
    private val chatCache = mutableMapOf<Long, Chat>()
    
    // 消息缓存
    private val messageCache = mutableMapOf<Long, MutableList<Message>>()
    
    override suspend fun initApp() {
        // 初始化会话成员
        val mockMembers = listOf(
            SessionMember(
                id = "1",
                userId = "user1",
                userName = "张三",
                nickname = "小张",
                avatar = "https://example.com/avatar1.jpg",
                isOnline = true,
                sessionId = "session1",
                role = SessionMemberRole.OWNER
            ),
            SessionMember(
                id = "2",
                userId = "user2",
                userName = "李四",
                avatar = "https://example.com/avatar2.jpg",
                isOnline = false,
                sessionId = "session1",
                role = SessionMemberRole.MEMBER
            ),
            SessionMember(
                id = "3",
                userId = "user3",
                userName = "王五",
                nickname = "小王",
                avatar = "https://example.com/avatar3.jpg",
                isOnline = true,
                sessionId = "session1",
                role = SessionMemberRole.ADMIN
            )
        )
        
        _conversationList.value = mockMembers
        mockMembers.forEach { addUserToCache(it) }
        setCurrentUser(mockMembers.first())
        
        // 初始化聊天会话
        val currentTime = System.currentTimeMillis()
        val mockChat = Chat(
            id = 1L,
            chatId = "chat_1",
            sessionId = "session1",
            type = ChatType.GROUP,
            name = "测试群聊",
            avatar = "https://example.com/group_avatar.jpg",
            participantIds = listOf(1L, 2L, 3L),
            participantUserIds = listOf("user1", "user2", "user3"),
            lastMessageId = null,
            lastMessageContent = "大家好！",
            lastMessageTime = currentTime,
            lastMessageSenderId = 1L,
            unreadCount = 0,
            isPinned = false,
            isMuted = false,
            isArchived = false,
            createTime = currentTime,
            updateTime = currentTime
        )
        chatCache[1L] = mockChat
    }
    
    override suspend fun getAllChats(): Flow<List<Chat>> {
        return flowOf(chatCache.values.toList())
    }
    
    override suspend fun getChatById(chatId: Long): Chat? {
        return chatCache[chatId]
    }
    
    override suspend fun createChat(chat: Chat): Long {
        val newId = (chatCache.keys.maxOrNull() ?: 0) + 1
        val newChat = chat.copy(id = newId)
        chatCache[newId] = newChat
        return newId
    }
    
    override suspend fun updateChat(chat: Chat) {
        chatCache[chat.id] = chat
    }
    
    override suspend fun deleteChat(chatId: Long) {
        chatCache.remove(chatId)
        messageCache.remove(chatId)
    }
    
    override suspend fun getMessagesForChat(chatId: Long, limit: Int): Flow<List<Message>> {
        val messages = messageCache[chatId]?.takeLast(limit) ?: emptyList()
        return flowOf(messages)
    }
    
    override suspend fun markChatAsRead(chatId: Long) {
        chatCache[chatId]?.let { chat ->
            updateChat(chat.copy(unreadCount = 0))
        }
    }
    
    override suspend fun toggleChatTop(chatId: Long, isTop: Boolean) {
        chatCache[chatId]?.let { chat ->
            updateChat(chat.copy(isPinned = isTop))
        }
    }
    
    override suspend fun toggleChatMute(chatId: Long, isMuted: Boolean) {
        chatCache[chatId]?.let { chat ->
            updateChat(chat.copy(isMuted = isMuted))
        }
    }
    

    
    /**
     * 获取用户信息
     */
    fun getUser(userId: String): SessionMember? {
        return userCache[userId]
    }
    
    /**
     * 更新会话成员列表
     */
    fun updateConversationList(members: List<SessionMember>) {
        _conversationList.value = members
    }
    
    /**
     * 添加用户到缓存
     */
    fun addUserToCache(user: SessionMember) {
        userCache[user.userId] = user
    }
    
    /**
     * 设置当前用户
     */
    fun setCurrentUser(user: SessionMember) {
        _currentUser.value = user
        addUserToCache(user)
    }
    
    /**
     * 发送消息
     */
    suspend fun sendMessage(chatId: Long, message: Message) {
        val messages = messageCache.getOrPut(chatId) { mutableListOf() }
        messages.add(message)
        
        // 更新聊天会话的最后消息
        chatCache[chatId]?.let { chat ->
            updateChat(chat.copy(
                lastMessageId = message.id,
                lastMessageContent = message.content,
                lastMessageTime = message.timestamp,
                lastMessageSenderId = message.senderId,
                unreadCount = if (message.senderUserId != _currentUser.value?.userId) chat.unreadCount + 1 else chat.unreadCount
            ))
        }
    }
    

}