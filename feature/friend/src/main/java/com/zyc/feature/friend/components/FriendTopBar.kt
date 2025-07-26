package com.zyc.feature.friend.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.element.components.input.FormInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendTopBar(
    showSearch: Boolean,
    searchKeyword: String,
    friendRequestCount: Int,
    onSearchToggle: () -> Unit,
    onSearchChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onShowFriendRequests: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(showSearch) {
        if (showSearch) {
            focusRequester.requestFocus()
        } else {
            keyboardController?.hide()
        }
    }

    TopAppBar(
        title = {
            if (showSearch) {
                FormInput(
                    value = searchKeyword,
                    onValueChange = onSearchChange,
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
                            IconButton(onClick = onClearSearch) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "清除搜索"
                                )
                            }
                        }
                    }
                )
            } else {
                Text("朋友")
            }
        },
        actions = {
            if (!showSearch) {
                // 朋友请求按钮
                Box {
                    IconButton(onClick = onShowFriendRequests) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "朋友请求"
                        )
                    }
                    if (friendRequestCount > 0) {
                        Badge(
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Text(
                                text = if (friendRequestCount > 99) "99+" else friendRequestCount.toString(),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                // 搜索按钮
                IconButton(onClick = onSearchToggle) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索"
                    )
                }
            } else {
                // 取消搜索按钮
                TextButton(onClick = onSearchToggle) {
                    Text("取消")
                }
            }
        }
    )
}
