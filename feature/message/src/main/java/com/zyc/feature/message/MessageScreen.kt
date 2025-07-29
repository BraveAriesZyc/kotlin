package com.zyc.feature.message

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage


import com.zyc.core.router.Routes
import com.zyc.core.router.LocalNavController

import com.zyc.core.ui.utils.refresh.CustomOverscrollEffect
import com.zyc.core.model.entity.SessionMember
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick

import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    onNavigateToSendMessage: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val navController = LocalNavController.current
    val messageViewModel = koinViewModel<MessageViewModel>()
    val scrollState = rememberLazyListState()
    //
    val conversationList by messageViewModel.conversationList.collectAsState()
    val isRefreshing by messageViewModel.isRefreshing.collectAsState()
    val scope = rememberCoroutineScope()
    // Create the overscroll controller with the scope and desired orientation
    val verticalLazyOverscroll =
        remember(scope) { CustomOverscrollEffect(scope, orientation = Orientation.Vertical) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()




    Scaffold(
        topBar = {
            ZAppBar(
                title = "消息",
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "显示底部表单"
                )
            }
        },
        content = { paddingValues ->

            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .wrapContentHeight(),
                overscrollEffect = verticalLazyOverscroll,
                content = {
                    items(
                        items = conversationList,
                        itemContent = { it ->
                            UserItem(
                                modifier = Modifier,
                                user = it,
                                onClick = {
                                    navController.navigate(Routes.SendMessage(it.sessionId))
                                }
                            )
                        }
                    )
                },
            )

            // 显示底部表单的条件控制
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "底部表单内容",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "这里可以添加你需要的内容",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }

        }
    )


}

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: SessionMember,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f))
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .debounceClick { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            contentDescription = null,
            model = user.avatar,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8)),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = user.userName,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.avatar,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )
        }
    }
}
