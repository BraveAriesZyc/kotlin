package com.zyc.feature.ui_showcase.screen.permission

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.permission.manager.PermissionManager
import com.zyc.core.permission.model.PermissionInfo
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView
import com.zyc.core.ui.utils.event.GlobalAntiShake
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

    // 常用权限列表
    val commonPermissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.VIBRATE
    )

    var permissionInfoList by remember { mutableStateOf<List<PermissionInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var allPermissionsGranted by remember { mutableStateOf(false) }
    var dangerousPermissionsCount by remember { mutableStateOf(0) }

    // 加载权限信息
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            permissionInfoList = permissionManager.getMultiplePermissionInfo(commonPermissions.toTypedArray())
            allPermissionsGranted = permissionManager.areAllPermissionsGranted(commonPermissions.toTypedArray())
            dangerousPermissionsCount = commonPermissions.count { permissionManager.isDangerousPermission(it) }
        } finally {
            isLoading = false
        }
    }

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
            item {
                PermissionStatsCard(
                    totalPermissions = commonPermissions.size,
                    grantedPermissions = permissionInfoList.count { it.isGranted },
                    dangerousPermissions = dangerousPermissionsCount,
                    allGranted = allPermissionsGranted
                )
            }

            // 使用场景演示
            item {
                PermissionUseCaseDemo(
                    permissionManager = permissionManager,
                    onResultUpdate = { result ->
                        // 可以在这里处理结果显示
                    }
                )
            }

            // 权限分类展示
            item {
                PermissionCategoryDemo(
                    permissionManager = permissionManager
                )
            }

            // 权限检查工具
            item {
                PermissionCheckTool(
                    permissionManager = permissionManager,
                    onResultUpdate = { result ->
                        // 可以在这里处理结果显示
                    }
                )
            }

            // 功能演示按钮
            item {
                FunctionDemoButtons(
                    permissionManager = permissionManager,
                    permissions = commonPermissions,
                    onRefresh = {
                        isLoading = true
                        permissionInfoList = permissionManager.getMultiplePermissionInfo(commonPermissions.toTypedArray())
                        allPermissionsGranted = permissionManager.areAllPermissionsGranted(commonPermissions.toTypedArray())
                        isLoading = false
                    }
                )
            }

            // 权限列表标题
            item {
                Text(
                    text = "权限详情列表",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // 权限列表
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(permissionInfoList) { permissionInfo ->
                    PermissionInfoCard(permissionInfo = permissionInfo)
                }
            }
        }
    }
}

/**
 * 权限统计卡片
 */
@Composable
private fun PermissionStatsCard(
    totalPermissions: Int,
    grantedPermissions: Int,
    dangerousPermissions: Int,
    allGranted: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (allGranted)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "权限统计",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (allGranted) Icons.Default.Check else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (allGranted)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "总权限",
                    value = totalPermissions.toString(),
                    color = MaterialTheme.colorScheme.onSurface
                )
                StatItem(
                    label = "已授权",
                    value = grantedPermissions.toString(),
                    color = MaterialTheme.colorScheme.primary
                )
                StatItem(
                    label = "危险权限",
                    value = dangerousPermissions.toString(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * 统计项组件
 */
@Composable
private fun StatItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 功能演示按钮组
 */
@Composable
private fun FunctionDemoButtons(
    permissionManager: PermissionManager,
    permissions: List<String>,
    onRefresh: () -> Unit
) {
    var demoResult by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "功能演示",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // 检查相机权限
            Button(
                onClick = {
                    GlobalAntiShake.runWithDebounce {
                        val isGranted = permissionManager.isPermissionGranted(Manifest.permission.CAMERA)
                        demoResult = "相机权限状态: ${if (isGranted) "已授权" else "未授权"}"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("检查相机权限")
            }

            // 检查所有权限
            Button(
                onClick = {
                    GlobalAntiShake.runWithDebounce {
                        val allGranted = permissionManager.areAllPermissionsGranted(permissions.toTypedArray())
                        demoResult = "所有权限状态: ${if (allGranted) "全部已授权" else "存在未授权权限"}"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("检查所有权限")
            }

            // 检查危险权限
            Button(
                onClick = {
                    GlobalAntiShake.runWithDebounce {
                        val dangerousCount = permissions.count { permissionManager.isDangerousPermission(it) }
                        demoResult = "危险权限数量: $dangerousCount 个"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("统计危险权限")
            }

            // 刷新权限信息
            Button(
                onClick = {
                    GlobalAntiShake.runWithDebounce {
                        onRefresh()
                        demoResult = "权限信息已刷新"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("刷新权限信息")
            }

            // 显示演示结果
            if (demoResult.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = demoResult,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/**
 * 权限信息卡片
 */
@Composable
private fun PermissionInfoCard(
    permissionInfo: PermissionInfo
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (permissionInfo.isGranted)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 权限名称（简化显示）
                Text(
                    text = permissionInfo.permission.substringAfterLast("."),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )

                // 完整权限名称
                Text(
                    text = permissionInfo.permission,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // 权限标签
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    if (permissionInfo.isDangerous) {
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = "危险权限",
                                    fontSize = 10.sp
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
            }

            // 权限状态图标
            Icon(
                imageVector = if (permissionInfo.isGranted) Icons.Default.Check else Icons.Default.Close,
                contentDescription = if (permissionInfo.isGranted) "已授权" else "未授权",
                tint = if (permissionInfo.isGranted)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
