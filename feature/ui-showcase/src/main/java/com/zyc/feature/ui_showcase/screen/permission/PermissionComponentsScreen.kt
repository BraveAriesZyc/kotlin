package com.zyc.feature.ui_showcase.screen.permission


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.core.permission.manager.rememberPermissionManager
import com.zyc.core.ui.components.common.IconBackground
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.feedback.loading.Loading
import com.zyc.core.ui.components.layout.refreshview.BounceListView

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
    val viewModel = viewModel<PermissionViewModel>()
    val permissions by viewModel.permissions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            ZAppBar(
                title = "权限组件演示",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Loading.AnimatedBallLoader()
                }
            )
        } else {
            BounceListView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                contentPadding = PaddingValues(8.dp),
            ) {
                // 权限统计卡片
                items(permissions.size) { it ->
                    PermissionStatusCard(
                        permissionModel = permissions[it],
                    )
                }
            }
        }

    }
}


@Composable
fun PermissionStatusCard(
    permissionModel: PermissionModel,
) {
    val permissionManager = rememberPermissionManager(permissionModel.permission)
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        onClick = {
            permissionManager.permissionLauncher.invoke()
        },
        content = {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Row {
                        IconBackground(
                            icon = permissionModel.icon,
                            color = permissionModel.iconColor,
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start,
                        content = {
                            Text(text = "权限：${permissionModel.permission.description}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "状态：${permissionManager.permissionStatus.description}")
                        }
                    )
                }
            )
        }
    )
}

