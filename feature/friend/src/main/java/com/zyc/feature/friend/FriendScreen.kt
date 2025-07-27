package com.zyc.feature.friend

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zyc.core.ui.components.refreshview.ZRefreshView
import com.zyc.core.ui.route.AddFriendRoute
import com.zyc.core.ui.route.LocalNavController
import com.zyc.feature.friend.components.*
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendScreen(
    viewModel: FriendViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val friendsWithUserInfo by viewModel.friendsWithUserInfo.collectAsStateWithLifecycle()
    val friendRequests by viewModel.friendRequests.collectAsStateWithLifecycle()
    val topFriends by viewModel.topFriends.collectAsStateWithLifecycle()
    val searchKeyword by viewModel.searchKeyword.collectAsStateWithLifecycle()
    var showFriendRequests by remember { mutableStateOf(false) }
    var selectedFriendId by remember { mutableStateOf<Long?>(null) }

    val navController = LocalNavController.current

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
                friendRequestCount = friendRequests.size,
                onAdd = {
                    navController.navigate(AddFriendRoute)
                },
                onShowFriendRequests = { showFriendRequests = true }
            )
        }
    ) { paddingValues ->
        ZRefreshView(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(vertical = 8.dp, horizontal = 8.dp),
            onRefresh = viewModel::refreshFriends,
            onLoadMore = viewModel::loadMoreFriends,
            isRefreshing = uiState.isRefreshing,
            isLoadingMore = uiState.isLoadingMore,
            enableLoadMore = true,
            content = {
                // 添加顶置
                if (topFriends.isNotEmpty()) {
                    topFriends.forEach { (friend, user) ->
                        item {
                            FriendItem(
                                friend = friend,
                                user = user,
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
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
        )
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
