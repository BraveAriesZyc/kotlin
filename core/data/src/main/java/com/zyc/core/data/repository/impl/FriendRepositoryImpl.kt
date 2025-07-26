package com.zyc.core.data.repository.impl

import com.zyc.core.data.repository.FriendRepository
import com.zyc.core.model.entity.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * 朋友数据仓库实现类
 */
class FriendRepositoryImpl : FriendRepository {

    // 模拟数据存储
    private val _friends = MutableStateFlow(generateMockFriends())
    private val _friendRequests = MutableStateFlow(generateMockFriendRequests())
    private val _users = MutableStateFlow(generateMockUsers())

    override suspend fun getFriends(): Flow<List<Friend>> {
        delay(500) // 模拟网络延迟
        return _friends.asStateFlow()
    }

    override suspend fun getFriendsByGroup(groupId: Long?): Flow<List<Friend>> {
        delay(300)
        return _friends.map { friends ->
            friends.filter { it.groupId == groupId }
        }
    }

    override suspend fun searchFriends(keyword: String): Flow<List<Friend>> {
        delay(300)
        return _friends.map { friends ->
            friends.filter { friend ->
                friend.nickname?.contains(keyword, ignoreCase = true) == true ||
                friend.friendUserIdStr.contains(keyword, ignoreCase = true)
            }
        }
    }

    override suspend fun addFriend(friendUserId: Long, nickname: String?, groupId: Long?): Boolean {
        delay(500)
        val newFriend = Friend(
            id = System.currentTimeMillis(),
            userId = 1L, // 当前用户ID
            friendUserId = friendUserId,
            friendUserIdStr = "user_$friendUserId",
            nickname = nickname,
            groupId = groupId,
            groupName = when(groupId) {
                1L -> "同事"
                2L -> "朋友"
                3L -> "家人"
                else -> null
            },
            status = FriendStatus.NORMAL,
            addTime = System.currentTimeMillis(),
            updateTime = System.currentTimeMillis()
        )

        val currentFriends = _friends.value.toMutableList()
        currentFriends.add(newFriend)
        _friends.value = currentFriends
        return true
    }

    override suspend fun removeFriend(friendId: Long): Boolean {
        delay(300)
        val currentFriends = _friends.value.toMutableList()
        val removed = currentFriends.removeAll { it.id == friendId }
        if (removed) {
            _friends.value = currentFriends
        }
        return removed
    }

    override suspend fun updateFriendNickname(friendId: Long, nickname: String): Boolean {
        delay(300)
        val currentFriends = _friends.value.toMutableList()
        val index = currentFriends.indexOfFirst { it.id == friendId }
        if (index != -1) {
            currentFriends[index] = currentFriends[index].copy(
                nickname = nickname,
                updateTime = System.currentTimeMillis()
            )
            _friends.value = currentFriends
            return true
        }
        return false
    }

    override suspend fun updateFriendGroup(friendId: Long, groupId: Long?): Boolean {
        delay(300)
        val currentFriends = _friends.value.toMutableList()
        val index = currentFriends.indexOfFirst { it.id == friendId }
        if (index != -1) {
            currentFriends[index] = currentFriends[index].copy(
                groupId = groupId,
                groupName = when(groupId) {
                    1L -> "同事"
                    2L -> "朋友"
                    3L -> "家人"
                    else -> null
                },
                updateTime = System.currentTimeMillis()
            )
            _friends.value = currentFriends
            return true
        }
        return false
    }

    override suspend fun blockFriend(friendId: Long, isBlocked: Boolean): Boolean {
        delay(300)
        val currentFriends = _friends.value.toMutableList()
        val index = currentFriends.indexOfFirst { it.id == friendId }
        if (index != -1) {
            currentFriends[index] = currentFriends[index].copy(
                isBlocked = isBlocked,
                updateTime = System.currentTimeMillis()
            )
            _friends.value = currentFriends
            return true
        }
        return false
    }

    override suspend fun starFriend(friendId: Long, isStarred: Boolean): Boolean {
        delay(300)
        val currentFriends = _friends.value.toMutableList()
        val index = currentFriends.indexOfFirst { it.id == friendId }
        if (index != -1) {
            currentFriends[index] = currentFriends[index].copy(
                isStarred = isStarred,
                updateTime = System.currentTimeMillis()
            )
            _friends.value = currentFriends
            return true
        }
        return false
    }

    override suspend fun getFriendRequests(): Flow<List<FriendRequest>> {
        delay(300)
        return _friendRequests.asStateFlow()
    }

    override suspend fun sendFriendRequest(toUserId: Long, message: String?): Boolean {
        delay(500)
        val newRequest = FriendRequest(
            id = System.currentTimeMillis(),
            fromUserId = 1L, // 当前用户ID
            fromUserIdStr = "user_1",
            toUserId = toUserId,
            toUserIdStr = "user_$toUserId",
            message = message ?: "",
            status = FriendRequestStatus.PENDING,
            createTime = System.currentTimeMillis(),
            updateTime = System.currentTimeMillis(),
            fromUserName = ""
        )

        val currentRequests = _friendRequests.value.toMutableList()
        currentRequests.add(newRequest)
        _friendRequests.value = currentRequests
        return true
    }

    override suspend fun handleFriendRequest(requestId: Long, accept: Boolean): Boolean {
        delay(500)
        val currentRequests = _friendRequests.value.toMutableList()
        val index = currentRequests.indexOfFirst { it.id == requestId }
        if (index != -1) {
            val request = currentRequests[index]
            currentRequests[index] = request.copy(
                status = if (accept) FriendRequestStatus.ACCEPTED else FriendRequestStatus.REJECTED,
                updateTime = System.currentTimeMillis()
            )
            _friendRequests.value = currentRequests

            // 如果接受请求，自动添加为朋友
            if (accept) {
                addFriend(request.fromUserId)
            }
            return true
        }
        return false
    }

    override suspend fun getFriendWithUserInfo(friendId: Long): Pair<Friend, User>? {
        delay(300)
        val friend = _friends.value.find { it.id == friendId } ?: return null
        val user = _users.value.find { it.id == friend.friendUserId } ?: return null
        return Pair(friend, user)
    }

    override suspend fun getFriendsWithUserInfo(): Flow<List<Pair<Friend, User>>> {
        delay(500)
        return _friends.map { friends ->
            friends.mapNotNull { friend ->
                val user = _users.value.find { it.id == friend.friendUserId }
                if (user != null) Pair(friend, user) else null
            }
        }
    }

    // 生成模拟朋友数据
    private fun generateMockFriends(): List<Friend> {
        return listOf(
            Friend(
                id = 1L,
                userId = 1L,
                friendUserId = 2L,
                friendUserIdStr = "user_2",
                nickname = "小明",
                groupId = 2L,
                groupName = "朋友",
                status = FriendStatus.NORMAL,
                isStarred = true,
                addTime = System.currentTimeMillis() - 86400000,
                updateTime = System.currentTimeMillis()
            ),
            Friend(
                id = 2L,
                userId = 1L,
                friendUserId = 3L,
                friendUserIdStr = "user_3",
                nickname = "小红",
                groupId = 1L,
                groupName = "同事",
                status = FriendStatus.NORMAL,
                addTime = System.currentTimeMillis() - 172800000,
                updateTime = System.currentTimeMillis()
            ),
            Friend(
                id = 3L,
                userId = 1L,
                friendUserId = 4L,
                friendUserIdStr = "user_4",
                nickname = "老王",
                groupId = 3L,
                groupName = "家人",
                status = FriendStatus.NORMAL,
                addTime = System.currentTimeMillis() - 259200000,
                updateTime = System.currentTimeMillis()
            ),
            Friend(
                id = 4L,
                userId = 1L,
                friendUserId = 5L,
                friendUserIdStr = "user_5",
                nickname = "小李",
                groupId = 2L,
                groupName = "朋友",
                status = FriendStatus.NORMAL,
                addTime = System.currentTimeMillis() - 345600000,
                updateTime = System.currentTimeMillis()
            ),
            Friend(
                id = 5L,
                userId = 1L,
                friendUserId = 6L,
                friendUserIdStr = "user_6",
                nickname = "张三",
                groupId = 1L,
                groupName = "同事",
                status = FriendStatus.NORMAL,
                addTime = System.currentTimeMillis() - 432000000,
                updateTime = System.currentTimeMillis()
            )
        )
    }

    // 生成模拟朋友请求数据
    private fun generateMockFriendRequests(): List<FriendRequest> {
        return listOf(
            FriendRequest(
                id = 1L,
                fromUserId = 7L,
                fromUserIdStr = "user_7",
                toUserId = 1L,
                toUserIdStr = "user_1",
                message = "你好，我想加你为好友",
                status = FriendRequestStatus.PENDING,
                createTime = System.currentTimeMillis() - 3600000,
                updateTime = System.currentTimeMillis() - 3600000,
                fromUserName = "TODO()"
            ),
            FriendRequest(
                id = 2L,
                fromUserId = 8L,
                fromUserIdStr = "user_8",
                toUserId = 1L,
                toUserIdStr = "user_1",
                message = "我们是同事，加个好友吧",
                status = FriendRequestStatus.PENDING,
                createTime = System.currentTimeMillis() - 7200000,
                updateTime = System.currentTimeMillis() - 7200000,
                fromUserName = "TODO()"
            )
        )
    }

    // 生成模拟用户数据
    private fun generateMockUsers(): List<User> {
        return listOf(
            User(
                id = 2L,
                userId = "user_2",
                username = "xiaoming",
                nickname = "小明同学",
                avatar = "https://picsum.photos/200/200?random=2",
                gender = Gender.MALE,
                signature = "努力工作，快乐生活",
                isOnline = true,
                createTime = System.currentTimeMillis() - 86400000,
                updateTime = System.currentTimeMillis()
            ),
            User(
                id = 3L,
                userId = "user_3",
                username = "xiaohong",
                nickname = "小红",
                avatar = "https://picsum.photos/200/200?random=3",
                gender = Gender.FEMALE,
                signature = "生活很美好",
                isOnline = false,
                createTime = System.currentTimeMillis() - 172800000,
                updateTime = System.currentTimeMillis()
            ),
            User(
                id = 4L,
                userId = "user_4",
                username = "laowang",
                nickname = "老王",
                avatar = "https://picsum.photos/200/200?random=4",
                gender = Gender.MALE,
                signature = "家庭第一",
                isOnline = true,
                createTime = System.currentTimeMillis() - 259200000,
                updateTime = System.currentTimeMillis()
            ),
            User(
                id = 5L,
                userId = "user_5",
                username = "xiaoli",
                nickname = "小李子",
                avatar = "https://picsum.photos/200/200?random=5",
                gender = Gender.FEMALE,
                signature = "热爱旅行",
                isOnline = true,
                createTime = System.currentTimeMillis() - 345600000,
                updateTime = System.currentTimeMillis()
            ),
            User(
                id = 6L,
                userId = "user_6",
                username = "zhangsan",
                nickname = "张三丰",
                avatar = "https://picsum.photos/200/200?random=6",
                gender = Gender.MALE,
                signature = "技术改变世界",
                isOnline = false,
                createTime = System.currentTimeMillis() - 432000000,
                updateTime = System.currentTimeMillis()
            ),
            User(
                id = 7L,
                userId = "user_7",
                username = "newuser1",
                nickname = "新用户1",
                avatar = "https://picsum.photos/200/200?random=7",
                gender = Gender.UNKNOWN,
                signature = "刚刚注册",
                isOnline = true,
                createTime = System.currentTimeMillis() - 3600000,
                updateTime = System.currentTimeMillis()
            ),
            User(
                id = 8L,
                userId = "user_8",
                username = "newuser2",
                nickname = "新用户2",
                avatar = "https://picsum.photos/200/200?random=8",
                gender = Gender.FEMALE,
                signature = "Hello World",
                isOnline = false,
                createTime = System.currentTimeMillis() - 7200000,
                updateTime = System.currentTimeMillis()
            )
        )
    }
}
