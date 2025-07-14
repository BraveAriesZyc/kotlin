package com.zyc.clover.pages.layout.message.children

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

import com.zyc.clover.components.keyboard.InputArea

import com.zyc.clover.models.MessageModel

import com.zyc.clover.models.enums.Role
import com.zyc.clover.models.UserModel
import com.zyc.clover.models.enums.MessageType
import com.zyc.clover.components.ZAppBar
import com.zyc.clover.pages.layout.children.message.children.SendMessageViewModel
import com.zyc.clover.route.LocalNavController

import com.zyc.clover.utils.event.GlobalAntiShake.debounceClick
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SendMessageScreen(conversationId: String) {
    val navController = LocalNavController.current
    val scrollState = rememberLazyListState()
    val sendMessageViewModel = koinViewModel<SendMessageViewModel>()
    val messageList by sendMessageViewModel.messageList.collectAsState()
    val user by sendMessageViewModel.user.collectAsState()
    val friend = sendMessageViewModel.getUser(conversationId)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit){
        sendMessageViewModel.getMessages(conversationId)
    }
    Scaffold(
        topBar = {
            ZAppBar(
                title = "${friend.nickname}",
                onBack = {
                    navController.popBackStack()
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .debounceClick {
                            // 点击消息区域时隐藏键盘
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                        .padding(top = it.calculateTopPadding()),
                    reverseLayout = true,
                    state = scrollState,
                    verticalArrangement = Arrangement.Top,
                ) {
                    items(messageList.size, key = { it -> it }) { it ->
                        MessageItem(messageList[it], friend, user)
                    }
                }
                InputArea(onSend = { it ->
                    if (it.isNotEmpty()){
                        // 在协程作用域中调用挂起函数

                        sendMessageViewModel.sendMessage(
                            MessageModel(
                                userId = "${user.userId}",
                                type = MessageType.TEXT.value,
                                content = it,
                                sessionId = conversationId,
                                role = Role.USER.value,
                            )
                        )
                    }
                })
            }
        }
    )

}

@Composable
fun MessageItem(item: MessageModel, friend: UserModel, user: UserModel) {
    val isSelf = item.role == Role.USER.value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 5.dp),
        horizontalArrangement = if (isSelf) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top,
        content = {
            if (!isSelf) {
                BuildAvatar(friend)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = if (isSelf) Alignment.End else Alignment.Start,
                content = {
                    Text(
                        text =  "${user.nickname}", style = TextStyle(
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Column(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .padding(start = if (isSelf) 60.dp else 0.dp, end = if (isSelf) 0.dp else 60.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                (if (isSelf) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                                    .copy(alpha = 0.2f)
                            )
                            .padding(horizontal = 10.dp, vertical = 10.dp),

                        content = {

                            when (item.type) {
                                MessageType.TEXT.value -> MessageItemText(item)
                                MessageType.IMAGE.value -> MessageItemImage(item)
                                MessageType.VIDEO.value -> MessageItemVideo(item)
                                MessageType.AUDIO.value -> MessageItemAudio(item)
                                MessageType.FILE.value -> MessageItemFile(item)
                                else -> MessageItemText(item)
                            }
                        }
                    )
                }
            )
            if (isSelf) {
                BuildAvatar(user)
            }
        }
    )


}

@Composable
fun MessageItemText(item: MessageModel) {
    Text(
        text = item.content, style = TextStyle(
            fontSize = 12.sp
        )
    )
}

@Composable
fun MessageItemImage(item: MessageModel) {
    val context = LocalContext.current
    Text(item.imageUrl)
}

@Composable
fun MessageItemVideo(item: MessageModel) {
    val context = LocalContext.current
    Text(item.videoUrl)
}

@Composable
fun MessageItemAudio(item: MessageModel) {
    val context = LocalContext.current
    Text(item.audioUrl)
}

@Composable
fun MessageItemFile(item: MessageModel) {
    val context = LocalContext.current
    Text(item.fileUrl)
}


@Composable
fun BuildAvatar(user: UserModel) {
    Column {
        Spacer(modifier = Modifier.height(10.dp))
        AsyncImage(
            modifier = Modifier.size(45.dp).clip(RoundedCornerShape(10.dp)),
            model = user.avatar, contentDescription = null
        )
    }
}
