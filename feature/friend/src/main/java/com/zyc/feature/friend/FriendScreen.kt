package com.zyc.feature.friend

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zyc.core.ui.components.refreshview.ZRefreshView
import com.zyc.feature.friend.components.FriendItem
import com.zyc.feature.friend.components.FriendRequestItem
import com.zyc.feature.friend.components.EmptyFriendsState
import com.zyc.feature.friend.components.FriendTopBar
import com.zyc.feature.friend.components.FriendRequestsDialog
import com.zyc.feature.friend.components.FriendActionDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendScreen(
    viewModel: FriendViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val friendsWithUserInfo by viewModel.filteredFriends.collectAsStateWithLifecycle()
    val friendRequests by viewModel.friendRequests.collectAsStateWithLifecycle()
    val searchKeyword by viewModel.searchKeyword.collectAsStateWithLifecycle()

    var showSearch by remember { mutableStateOf(false) }
    var showFriendRequests by remember { mutableStateOf(false) }
    var selectedFriendId by remember { mutableStateOf<Long?>(null) }

    // 错误提示
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // 这里可以显示 SnackBar 或其他错误提示
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            FriendTopBar(
                showSearch = showSearch,
                searchKeyword = searchKeyword,
                friendRequestCount = friendRequests.size,
                onSearchToggle = { showSearch = !showSearch },
                onSearchChange = viewModel::searchFriends,
                onClearSearch = viewModel::clearSearch,
                onShowFriendRequests = { showFriendRequests = true }
            )
        }
    ) { paddingValues ->
        ZRefreshView(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onRefresh = viewModel::refreshFriends,
            onLoadMore = viewModel::loadMoreFriends,
            isRefreshing = uiState.isRefreshing,
            isLoadingMore = uiState.isLoadingMore,
            enableLoadMore = true
        ) {
            if (uiState.isLoading && friendsWithUserInfo.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (friendsWithUserInfo.isEmpty()) {
                item {
                    EmptyFriendsState(
                        message = if (searchKeyword.isNotBlank()) "未找到相关朋友" else "暂无朋友"
                    )
                }
            } else {
                items(
                    items = friendsWithUserInfo,
                    key = { (friend, _) -> friend.id }
                ) { (friend, user) ->
                    FriendItem(
                        friend = friend,
                        user = user,
                        onItemClick = {
                            // 点击朋友项，可以导航到聊天页面或朋友详情页面
                        },
                        onStarClick = { isStarred ->
                            viewModel.toggleStarFriend(friend.id, isStarred)
                        },
                        onMoreClick = {
                            selectedFriendId = friend.id
                        }
                    )
                }
            }
        }
    }

    // 朋友请求弹窗
    if (showFriendRequests) {
        FriendRequestsDialog(
            requests = friendRequests,
            onDismiss = { showFriendRequests = false },
            onAcceptRequest = { requestId ->
                viewModel.handleFriendRequest(requestId, true)
            },
            onRejectRequest = { requestId ->
                viewModel.handleFriendRequest(requestId, false)
            }
        )
    }

    // 朋友操作菜单
    selectedFriendId?.let { friendId ->
        val friendPair = friendsWithUserInfo.find { it.first.id == friendId }
        if (friendPair != null) {
            FriendActionDialog(
                friend = friendPair.first,
                user = friendPair.second,
                onDismiss = { selectedFriendId = null },
                onUpdateNickname = { nickname ->
                    viewModel.updateFriendNickname(friendId, nickname)
                    selectedFriendId = null
                },
                onToggleBlock = { isBlocked ->
                    viewModel.toggleBlockFriend(friendId, isBlocked)
                    selectedFriendId = null
                },
                onRemoveFriend = {
                    viewModel.removeFriend(friendId)
                    selectedFriendId = null
                }
            )
        }
    }
}
