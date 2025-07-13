package com.zyc.clover.pages.layout.children.message

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

import com.zyc.clover.models.SessionMemberModel
import com.zyc.clover.route.SendMessageRoute
import com.zyc.clover.components.ZAppBar
import com.zyc.clover.pages.layout.message.MessageViewModel
import com.zyc.clover.route.LocalNavController

import com.zyc.clover.utils.event.GlobalAntiShake.debounceClick
import com.zyc.clover.utils.refresh.CustomOverscrollEffect

import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen() {
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




    Scaffold(
        topBar = {
            ZAppBar(
                title = "消息",
            )
        },
        content = { paddingValues ->
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .wrapContentHeight(),
                overscrollEffect = verticalLazyOverscroll,
                content = {

//                    repeat(40){
//                        item {
//                            UserItem(
//                                modifier = Modifier,
//                                user = UserModel(
//                                    userName = "用户$it",
//                                    uid = "uid$it",
//                                    avatar = "https://picsum.photos/200/300?random=$it",
//                                    age = it,
//                                    sex = "男",
//                                ),
//                                onClick = {
//                                    navController.navigate("${RouterName.SendMessageScreen.route}/$it")
//                                }
//                            )
//                        }
//                    }
                    items(
                        items = conversationList,
                        itemContent = { it ->
                            UserItem(
                                modifier = Modifier,
                                user = it,
                                onClick = {
                                    navController.navigate(SendMessageRoute(it.sessionId))
                                }
                            )
                        }
                    )
                },
            )
        }
    )


}

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: SessionMemberModel,
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
                    color =MaterialTheme.colorScheme.onSurface
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
