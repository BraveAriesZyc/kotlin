package com.zyc.feature.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyc.core.data.repository.FriendRepository
import com.zyc.core.model.entity.Friend
import com.zyc.core.model.entity.FriendRequest
import com.zyc.core.model.entity.FriendStatus
import com.zyc.core.model.entity.Gender
import com.zyc.core.model.entity.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 朋友页面 ViewModel
 */
class FriendViewModel(
    private val friendRepository: FriendRepository
) : ViewModel() {

    // UI 状态
    private val _uiState = MutableStateFlow(FriendUiState())
    val uiState: StateFlow<FriendUiState> = _uiState.asStateFlow()

    // 添加好友
    private val _addFriendState = MutableStateFlow<List<Pair<Friend, User>>>(emptyList())
    val addFriendState: StateFlow<List<Pair<Friend, User>>> = _addFriendState.asStateFlow()

    // 朋友列表（包含用户信息）
    private val _friendsWithUserInfo = MutableStateFlow<List<Pair<Friend, User>>>(emptyList())
    val friendsWithUserInfo: StateFlow<List<Pair<Friend, User>>> = _friendsWithUserInfo.asStateFlow()

    // 朋友请求列表
    private val _friendRequests = MutableStateFlow<List<FriendRequest>>(emptyList())
    val friendRequests: StateFlow<List<FriendRequest>> = _friendRequests.asStateFlow()

    // 搜索关键词
    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword.asStateFlow()


    init {
        loadFriends()
        loadFriendRequests()
    }

    /**
     * 加载朋友列表
     */
    fun loadFriends() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                friendRepository.getFriendsWithUserInfo().collect { friends ->
                    _friendsWithUserInfo.value = friends
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "加载朋友列表失败"
                )
            }
        }
    }

    /**
     * 刷新朋友列表
     */
    fun refreshFriends() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            try {
                friendRepository.getFriendsWithUserInfo().collect { friends ->
                    _friendsWithUserInfo.value = friends
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    error = e.message ?: "刷新失败"
                )
            }
        }
    }

    /**
     * 加载更多朋友
     */
    fun loadMoreFriends() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMore = true)
            try {
                // 模拟加载更多数据
                kotlinx.coroutines.delay(1000)
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    error = e.message ?: "加载更多失败"
                )
            }
        }
    }

    /**
     * 搜索朋友
     */
    fun searchFriends(keyword: String) {
        _searchKeyword.value = keyword
        _addFriendState.value = listOf(
            Pair(
                Friend(
                    id = 11L,
                    userId = 1L,
                    friendUserId = 6L,
                    friendUserIdStr = "user_11",
                    nickname = "张三",
                    groupId = 1L,
                    groupName = "同事",
                    status = FriendStatus.NORMAL,
                    addTime = System.currentTimeMillis() - 432000000,
                    updateTime = System.currentTimeMillis()
                ),
                User(
                    id = 11L,
                    userId = "user_11",
                    username = "newuser1",
                    nickname = "新用户1",
                    avatar = "https://picsum.photos/200/200?random=23",
                    gender = Gender.UNKNOWN,
                    signature = "刚刚注册",
                    isOnline = true,
                    createTime = System.currentTimeMillis() - 3600000,
                    updateTime = System.currentTimeMillis()
                ),
            )
        )
    }

    /**
     * 清除搜索
     */
    fun clearSearch() {
        _searchKeyword.value = ""
        _addFriendState.value = emptyList()
    }

    /**
     * 删除朋友
     */
    fun removeFriend(friendId: Long) {
        viewModelScope.launch {
            try {
                val success = friendRepository.removeFriend(friendId)
                if (success) {
                    // 刷新列表
                    loadFriends()
                } else {
                    _uiState.value = _uiState.value.copy(error = "删除朋友失败")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "删除朋友失败"
                )
            }
        }
    }

    /**
     * 更新朋友备注
     */
    fun updateFriendNickname(friendId: Long, nickname: String) {
        viewModelScope.launch {
            try {
                val success = friendRepository.updateFriendNickname(friendId, nickname)
                if (success) {
                    loadFriends()
                } else {
                    _uiState.value = _uiState.value.copy(error = "更新备注失败")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "更新备注失败"
                )
            }
        }
    }

    /**
     * 特别关注/取消特别关注
     */
    fun toggleStarFriend(friendId: Long, isStarred: Boolean) {
        viewModelScope.launch {
            try {
                val success = friendRepository.starFriend(friendId, isStarred)
                if (success) {
                    loadFriends()
                } else {
                    _uiState.value = _uiState.value.copy(error = "操作失败")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "操作失败"
                )
            }
        }
    }

    /**
     * 屏蔽/取消屏蔽朋友
     */
    fun toggleBlockFriend(friendId: Long, isBlocked: Boolean) {
        viewModelScope.launch {
            try {
                val success = friendRepository.blockFriend(friendId, isBlocked)
                if (success) {
                    loadFriends()
                } else {
                    _uiState.value = _uiState.value.copy(error = "操作失败")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "操作失败"
                )
            }
        }
    }

    /**
     * 加载朋友请求
     */
    private fun loadFriendRequests() {
        viewModelScope.launch {
            try {
                friendRepository.getFriendRequests().collect { requests ->
                    _friendRequests.value = requests
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "加载朋友请求失败"
                )
            }
        }
    }

    /**
     * 处理朋友请求
     */
    fun handleFriendRequest(requestId: Long, accept: Boolean) {
        viewModelScope.launch {
            try {
                val success = friendRepository.handleFriendRequest(requestId, accept)
                if (success) {
                    loadFriendRequests()
                    if (accept) {
                        loadFriends() // 如果接受请求，刷新朋友列表
                    }
                } else {
                    _uiState.value = _uiState.value.copy(error = "处理请求失败")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "处理请求失败"
                )
            }
        }
    }

    /**
     * 清除错误信息
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * 朋友页面 UI 状态
 */
data class FriendUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null
)
