package com.zyc.feature.friend

import android.util.Log
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
import com.zyc.core.ui.components.navigation.menu.MenuAction
import com.zyc.core.ui.components.layout.refreshview.ZRefreshView
import com.zyc.core.ui.components.navigation.menu.contextMenu
import com.zyc.core.ui.components.navigation.menu.rememberContextMenuState

import com.zyc.core.router.Routes
import com.zyc.core.router.LocalNavController
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
                    navController.navigate(Routes.AddFriend)
                },
                onShowFriendRequests = { showFriendRequests = true }
            )
        }
    ) { paddingValues ->
        ZRefreshView(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(top = 8.dp)
                .padding(horizontal = 8.dp),
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

                            val menus = remember {
                                listOf(
                                    MenuAction(
                                        "取消顶置",
                                        "\uEB38"
                                    ) { viewModel.toggleStarFriend(friend.id, false) },
                                    MenuAction("更多操作", "\uEBD3") {
                                        selectedFriendId = friend.id
                                    },
                                )
                            }
                            LongPressFriend(
                                friend,
                                user,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                menus = menus,
                                onClick = {
                                    navController.navigate(Routes.SendMessage(user.userId))
                                }
                            )
                        }
                    }
                }
                items(
                    items = friendsWithUserInfo,
                    key = { (friend, _) -> friend.id }
                ) { (friend, user) ->

                    val menus = remember {
                        listOf(
                            MenuAction(
                                "顶置",
                                "\uEB42"
                            ) { viewModel.toggleStarFriend(friend.id, true) },
                            MenuAction("更多操作", "\uEBD3") {
                                println("更多操作")
                                selectedFriendId = friend.id
                            },
                        )
                    }


                    LongPressFriend(

                        friend,
                        user,
                        menus = menus,
                        onClick = {
                            navController.navigate(Routes.SendMessage(user.userId))
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
    menus: List<MenuAction> = emptyList(),
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.surfaceBright,
) {
    val contextMenuState = rememberContextMenuState { listIndex, menuIndex ->
        Log.d("你点击了第${listIndex + 1}项的", menus[menuIndex].title)
    }

    FriendItem(
        modifier = Modifier.contextMenu(
            onLongPress = { position ->
                contextMenuState.show(position, menus, 1)
            },
            onTap = { onClick() }
        ),
        friend = friend,
        user = user,
        color = color
    )
}
