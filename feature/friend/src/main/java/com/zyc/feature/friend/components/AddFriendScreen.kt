package com.zyc.feature.friend.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zyc.core.ui.components.ZAppBar
import com.zyc.core.ui.components.element.components.input.FormInput
import com.zyc.core.ui.components.input.Input
import com.zyc.core.ui.components.keyboard.ZAInput
import com.zyc.core.ui.components.refreshview.ZRefreshView
import com.zyc.core.ui.route.LocalNavController
import com.zyc.feature.friend.FriendViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddFriendScreen(
    viewModel: FriendViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchKeyword by viewModel.searchKeyword.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    Scaffold(
        topBar = {
            ZAppBar(
                actions = {
                    FormInput(
                        value = searchKeyword,
                        onValueChange = { viewModel.searchFriends(
                             it
                        ) },
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
                onBack = {
                    navController.popBackStack()
                }
            )
        },
        content = { paddingValues ->
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
                    item {
                        EmptyFriendsState(
                            message = if (searchKeyword.isNotBlank()) "未找到相关朋友" else "暂无朋友"
                        )
                    }
                }
            )
        }
    )

}
