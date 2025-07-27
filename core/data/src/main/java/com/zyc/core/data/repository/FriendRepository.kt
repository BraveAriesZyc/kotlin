package com.zyc.core.data.repository

import com.zyc.core.model.entity.Friend
import com.zyc.core.model.entity.FriendRequest
import com.zyc.core.model.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * 朋友数据仓库接口
 */
interface FriendRepository {

    /**
     * 获取朋友列表
     */
    suspend fun getFriends(): Flow<List<Friend>>

    /**
     * 根据分组获取朋友列表
     */
    suspend fun getFriendsByGroup(groupId: Long?): Flow<List<Friend>>

    /**
     * 搜索朋友
     */
    suspend fun searchFriends(keyword: String): Flow<List<Friend>>

    /**
     * 添加朋友
     */
    suspend fun addFriend(friendUserId: Long, nickname: String? = null, groupId: Long? = null): Boolean

    /**
     * 删除朋友
     */
    suspend fun removeFriend(friendId: Long): Boolean

    /**
     * 更新朋友备注
     */
    suspend fun updateFriendNickname(friendId: Long, nickname: String): Boolean

    /**
     * 更新朋友分组
     */
    suspend fun updateFriendGroup(friendId: Long, groupId: Long?): Boolean

    /**
     * 屏蔽/取消屏蔽朋友
     */
    suspend fun blockFriend(friendId: Long, isBlocked: Boolean): Boolean

    /**
     * 特别关注/取消特别关注
     */
    suspend fun starFriend(friendId: Long, isStarred: Boolean): Boolean

    /**
     * 获取朋友请求列表
     */
    suspend fun getFriendRequests(): Flow<List<FriendRequest>>

    /**
     * 发送朋友请求
     */
    suspend fun sendFriendRequest(toUserId: Long, message: String? = null): Boolean

    /**
     * 处理朋友请求
     */
    suspend fun handleFriendRequest(requestId: Long, accept: Boolean): Boolean

    /**
     * 获取朋友详细信息（包含用户信息）
     */
    suspend fun getFriendWithUserInfo(friendId: Long): Pair<Friend, User>?

    /**
     * 获取所有朋友的详细信息
     */
    suspend fun getFriendsWithUserInfo(): Flow<List<Pair<Friend, User>>>

    /**
     * 获取顶置详细信息
     */
    suspend fun getTopFriends(): Flow<List<Pair<Friend, User>>>
}
