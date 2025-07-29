package com.zyc.feature.friend.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zyc.core.model.entity.Friend
import com.zyc.core.model.entity.User
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.form.input.FormInput
import com.zyc.core.ui.components.layout.refreshview.BounceListView

import com.zyc.core.router.LocalNavController
import com.zyc.core.router.Routes
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick
import com.zyc.feature.friend.FriendViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddFriendScreen(
    viewModel: FriendViewModel = koinViewModel(),
    onNavigateToSendMessage: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val searchKeyword by viewModel.searchKeyword.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val addFriendState by viewModel.addFriendState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }
    Scaffold(
        topBar = {

            ZAppBar(
                backgroundColor = MaterialTheme.colorScheme.background,
                customContent = {
                    FormInput(
                        value = searchKeyword,
                        onValueChange = {
                            viewModel.searchFriends(
                                it
                            )
                        },
                        shape  = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        placeholder = "搜索朋友",
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                            }
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,  // 获得焦点时的背景色
                            unfocusedContainerColor = Color.White,  // 未获得焦点时的背景色
                            disabledContainerColor = Color.Gray,  // 禁用状态的背景色
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        trailingIcon = {
                            if (searchKeyword.isNotBlank()) {
                                IconButton(onClick = {
                                    viewModel.clearSearch()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "清除搜索"
                                    )
                                }
                            }
                        }
                    )

                },
                customIconButton =  {
                    Box(
                        modifier = Modifier.debounceClick { navController.popBackStack() },
                        content = {
                            Text("取消", fontSize = 16.sp)
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            BounceListView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                content = {
                    if (addFriendState.isEmpty()) {
                        item {
                            EmptyFriendsState(
                                message = if (searchKeyword.isNotBlank()) "未找到相关朋友" else "暂无朋友"
                            )
                        }
                    } else {
                        items(
                            items = addFriendState,
                            key = { (friend, _) -> friend.id }
                        ) { (friend, user) ->
                            OnClickFriend(friend, user, onClick = {
                                navController.navigate(Routes.Message.SendMessage(user.userId))
                            })
                        }
                    }
                }
            )
        }
    )

}


@Composable
fun OnClickFriend(
    friend: Friend,
    user: User,
    onClick: () -> Unit,
) {
    FriendItem(
        friend = friend,
        user = user,
        color = MaterialTheme.colorScheme.surfaceBright,
        onClick = {
            onClick()
        }
    )
}
