package com.zyc.feature.friend

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zyc.core.model.entity.Friend
import com.zyc.core.model.entity.User
import com.zyc.core.ui.components.menu.LongPressMenuContainer
import com.zyc.core.ui.components.menu.MenuAction
import com.zyc.core.ui.components.refreshview.ZRefreshView
import com.zyc.core.ui.route.AddFriendRoute
import com.zyc.core.ui.route.LocalNavController
import com.zyc.core.ui.route.SendMessageRoute
import com.zyc.feature.friend.components.FriendActionDialog
import com.zyc.feature.friend.components.FriendItem
import com.zyc.feature.friend.components.FriendRequestsDialog
import com.zyc.feature.friend.components.FriendTopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendScreen(
    viewModel: FriendViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val friendsWithUserInfo by viewModel.friendsWithUserInfo.collectAsStateWithLifecycle()
    val friendRequests by viewModel.friendRequests.collectAsStateWithLifecycle()
    val topFriends by viewModel.topFriends.collectAsStateWithLifecycle()
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
                            LongPressFriend(
                                friend,
                                user,
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                                onMoreClick = { id ->
                                    selectedFriendId = id
                                },
                                onTopClick = { id, isStarred ->
                                    viewModel.toggleStarFriend(id, isStarred)
                                },
                                onClick = {
                                    navController.navigate(SendMessageRoute(user.userId))
                                }
                            )
                        }
                    }
                }
                items(
                    items = friendsWithUserInfo,
                    key = { (friend, _) -> friend.id }
                ) { (friend, user) ->
                    LongPressFriend(
                        friend,
                        user,
                        onMoreClick = { id ->
                            selectedFriendId = id
                        },
                        onTopClick = { id, isStarred ->
                            viewModel.toggleStarFriend(id, isStarred)
                        },
                        onClick = {
                            navController.navigate(SendMessageRoute(user.userId))
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
        val list = listOf(friendsWithUserInfo, topFriends).flatten()
        val friendPair = list.find { it.first.id == friendId }

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


@Composable
fun LongPressFriend(
    friend: Friend,
    user: User,
    onMoreClick: (Long) -> Unit,
    onTopClick: (Long, Boolean) -> Unit,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.surface,
) {
    // 创建菜单项
    val menuItems = remember {
        listOf(
            MenuAction(
                if (friend.isStarred) "取消顶置" else "顶置",
                if (friend.isStarred) "\uEC15" else "\uEC14"
            ) { onTopClick(friend.id, !friend.isStarred) },
            MenuAction("更多操作", "\uEBD3") {
                println("更多操作")
                onMoreClick(friend.id)
            },
        )
    }

    LongPressMenuContainer(
        menuItems = menuItems,
        onTap = onClick,
        content = {
            FriendItem(
                friend = friend,
                user = user,
                color = color
            )
        }
    )
}
