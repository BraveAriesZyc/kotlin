package com.zyc.core.data.repository.impl

import com.zyc.core.data.repository.UserRepository
import com.zyc.core.model.entity.User
import com.zyc.core.model.entity.UserBrief
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * 用户数据仓库实现类
 */
class UserRepositoryImpl : UserRepository {
    
    private val _currentUser = MutableStateFlow<User?>(null)
    private val _users = MutableStateFlow<List<User>>(emptyList())
    private val _friendships = MutableStateFlow<Map<Long, Set<Long>>>(emptyMap())
    
    override suspend fun initApp() {
        // 初始化模拟数据
        val currentTime = System.currentTimeMillis()
        val mockUsers = listOf(
            User(
                id = 1L,
                userId = "user_1",
                username = "user1",
                nickname = "Alice",
                email = "alice@example.com",
                avatar = "https://example.com/avatar1.jpg",
                isOnline = true,
                lastLoginTime = currentTime,
                createTime = currentTime - 86400000,
                updateTime = currentTime
            ),
            User(
                id = 2L,
                userId = "user_2",
                username = "user2",
                nickname = "Bob",
                email = "bob@example.com",
                avatar = "https://example.com/avatar2.jpg",
                isOnline = false,
                lastLoginTime = currentTime - 3600000,
                createTime = currentTime - 172800000,
                updateTime = currentTime - 3600000
            ),
            User(
                id = 3L,
                userId = "user_3",
                username = "user3",
                nickname = "Charlie",
                email = "charlie@example.com",
                avatar = "https://example.com/avatar3.jpg",
                isOnline = true,
                lastLoginTime = currentTime - 1800000,
                createTime = currentTime - 259200000,
                updateTime = currentTime - 1800000
            )
        )
        
        _users.value = mockUsers
        _currentUser.value = mockUsers.first()
        
        // 初始化好友关系
        _friendships.value = mapOf(
            1L to setOf(2L, 3L),
            2L to setOf(1L),
            3L to setOf(1L)
        )
    }
    
    override suspend fun getUserById(userId: Long): User? {
        return _users.value.find { it.id == userId }
    }
    
    override suspend fun getUserByUsername(username: String): User? {
        return _users.value.find { it.username == username }
    }
    
    override suspend fun getCurrentUser(): Flow<User?> {
        return _currentUser.asStateFlow()
    }
    
    override suspend fun updateUser(user: User) {
        val updatedUser = user.copy(updateTime = System.currentTimeMillis())
        _users.value = _users.value.map { existingUser ->
            if (existingUser.id == updatedUser.id) updatedUser else existingUser
        }
        
        if (_currentUser.value?.id == updatedUser.id) {
            _currentUser.value = updatedUser
        }
    }
    
    override suspend fun updateUserAvatar(userId: Long, avatarUrl: String) {
        val user = getUserById(userId)
        user?.let {
            updateUser(it.copy(
                avatar = avatarUrl,
                updateTime = System.currentTimeMillis()
            ))
        }
    }
    
    override suspend fun updateUserOnlineStatus(userId: Long, isOnline: Boolean) {
        val user = getUserById(userId)
        user?.let {
            updateUser(it.copy(
                isOnline = isOnline,
                lastLoginTime = if (isOnline) System.currentTimeMillis() else it.lastLoginTime,
                updateTime = System.currentTimeMillis()
            ))
        }
    }
    
    override suspend fun searchUsers(keyword: String): Flow<List<UserBrief>> {
        return _users.asStateFlow().map { users ->
            users.filter { user ->
                user.username.contains(keyword, ignoreCase = true) ||
                user.nickname?.contains(keyword, ignoreCase = true) == true ||
                user.email?.contains(keyword, ignoreCase = true) == true
            }.map { user ->
                UserBrief(
                    id = user.id,
                    userId = user.userId,
                    username = user.username,
                    nickname = user.nickname,
                    avatar = user.avatar,
                    isOnline = user.isOnline
                )
            }
        }
    }
    
    override suspend fun getUserFriends(userId: Long): Flow<List<UserBrief>> {
        return _users.asStateFlow().map { users ->
            val friendIds = _friendships.value[userId] ?: emptySet()
            users.filter { it.id in friendIds }.map { user ->
                UserBrief(
                    id = user.id,
                    userId = user.userId,
                    username = user.username,
                    nickname = user.nickname,
                    avatar = user.avatar,
                    isOnline = user.isOnline
                )
            }
        }
    }
    
    override suspend fun addFriend(userId: Long, friendId: Long) {
        val currentFriendships = _friendships.value.toMutableMap()
        
        // 添加双向好友关系
        currentFriendships[userId] = (currentFriendships[userId] ?: emptySet()) + friendId
        currentFriendships[friendId] = (currentFriendships[friendId] ?: emptySet()) + userId
        
        _friendships.value = currentFriendships
    }
    
    override suspend fun removeFriend(userId: Long, friendId: Long) {
        val currentFriendships = _friendships.value.toMutableMap()
        
        // 移除双向好友关系
        currentFriendships[userId] = (currentFriendships[userId] ?: emptySet()) - friendId
        currentFriendships[friendId] = (currentFriendships[friendId] ?: emptySet()) - userId
        
        _friendships.value = currentFriendships
    }
    
    override suspend fun isFriend(userId: Long, friendId: Long): Boolean {
        return _friendships.value[userId]?.contains(friendId) == true
    }
}