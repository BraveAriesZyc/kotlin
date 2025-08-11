package com.zyc.feature.ui_showcase.screen.permission


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.zyc.core.permission.manager.PermissionManager
import com.zyc.core.permission.model.Permission
import com.zyc.core.permission.model.PermissionStatus
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick

/**
 * 权限组件演示屏幕
 *
 * 展示权限管理器的各种功能：
 * - 权限状态检查
 * - 多权限检查
 * - 危险权限识别
 * - 权限信息获取
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionComponentsScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val permissionManager = remember { PermissionManager(context) }

    // 常用权限列表 - 使用权限枚举
    val commonPermissionEnums = listOf(
        Permission.CAMERA,
        Permission.MICROPHONE,
        Permission.FINE_LOCATION,
        Permission.COARSE_LOCATION,
        Permission.READ_EXTERNAL_STORAGE,
        Permission.WRITE_EXTERNAL_STORAGE,
        Permission.READ_CONTACTS,
        Permission.CALL_PHONE,
        Permission.SEND_SMS,
        Permission.READ_CALENDAR,
        Permission.POST_NOTIFICATIONS,
        Permission.VIBRATE
    )


    Scaffold(
        topBar = {
            ZAppBar(
                title = "权限组件演示",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        BounceListView(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(8.dp),
        ) {
            // 权限统计卡片
            items(commonPermissionEnums.size) { it ->
                PermissionStatusCard(
                    permission = commonPermissionEnums[it],
                    permissionStatus = permissionManager.checkPermissionStatus(commonPermissionEnums[it]),
                    onClick = {
                        permissionManager.requestPermission(commonPermissionEnums[it])
                    }
                )
            }
        }
    }
}


@Composable
fun PermissionStatusCard(
    permission: Permission,
    permissionStatus: PermissionStatus,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
            .clickable {
                onClick.invoke()
            }.padding(bottom = 8.dp),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Text(text = "权限：${permission.description}")
                    Text(text = "状态：${permissionStatus.description}")
                }
            )
        }
    )

}