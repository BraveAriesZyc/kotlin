package com.zyc.core.data.repository

import com.zyc.core.model.entity.User
import com.zyc.core.model.entity.UserBrief
import kotlinx.coroutines.flow.Flow

/**
 * 用户数据仓库接口
 */
interface UserRepository {
    
    /**
     * 初始化应用数据
     */
    suspend fun initApp()
    
    /**
     * 根据ID获取用户信息
     */
    suspend fun getUserById(userId: Long): User?
    
    /**
     * 根据用户名获取用户信息
     */
    suspend fun getUserByUsername(username: String): User?
    
    /**
     * 获取当前登录用户信息
     */
    suspend fun getCurrentUser(): Flow<User?>
    
    /**
     * 更新用户信息
     */
    suspend fun updateUser(user: User)
    
    /**
     * 更新用户头像
     */
    suspend fun updateUserAvatar(userId: Long, avatarUrl: String)
    
    /**
     * 更新用户在线状态
     */
    suspend fun updateUserOnlineStatus(userId: Long, isOnline: Boolean)
    
    /**
     * 搜索用户
     */
    suspend fun searchUsers(keyword: String): Flow<List<UserBrief>>
    
    /**
     * 获取用户好友列表
     */
    suspend fun getUserFriends(userId: Long): Flow<List<UserBrief>>
    
    /**
     * 添加好友
     */
    suspend fun addFriend(userId: Long, friendId: Long)
    
    /**
     * 删除好友
     */
    suspend fun removeFriend(userId: Long, friendId: Long)
    
    /**
     * 检查是否为好友关系
     */
    suspend fun isFriend(userId: Long, friendId: Long): Boolean
}