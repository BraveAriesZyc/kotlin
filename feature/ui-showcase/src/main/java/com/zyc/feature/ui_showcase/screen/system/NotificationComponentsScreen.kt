package com.zyc.feature.ui_showcase.screen.system

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.zyc.core.permission.manager.PermissionManager
import com.zyc.core.permission.model.Permission
import com.zyc.core.permission.model.PermissionStatus
import com.zyc.core.permission.notification.NotificationUtil
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView

@Composable
fun NotificationScreen(
    onBack: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            ZAppBar(
                title = "权限组件演示",
                onBack = onBack
            )
        },
        content = {

            val context = LocalContext.current
            val permissionState = PermissionManager(context)


            val channelId = "test_channel_id"
            val channelName = "Test Channel Name"
            BounceListView(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
                content = {
                    item {
                        Button(
                            onClick = {
                                if (
                                    permissionState.checkPermissionStatus(Permission.POST_NOTIFICATIONS) == PermissionStatus.GRANTED
                                    ) {
                                    NotificationUtil.sendNotification(
                                        context,
                                        channelId,
                                        channelName,
                                        "好友2",
                                        "你好吗？"
                                    )
                                } else {
                                    permissionState.requestPermission(Permission.POST_NOTIFICATIONS)
                                }
                            },
                            content = {
                                Text(text = "发送通知")
                            }
                        )
                    }
                }
            )
        }
    )

}

