package com.zyc.feature.ui_showcase.screen.permission

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.permission.manager.PermissionManager
import com.zyc.core.permission.model.PermissionCategoryHelper
import com.zyc.core.permission.model.PermissionEnum
import com.zyc.core.permission.model.PermissionGroupInfo
import com.zyc.core.permission.model.PermissionInfo
import com.zyc.core.permission.model.PermissionStatus
import com.zyc.core.ui.components.common.IconBackground

/**
 * 权限使用场景演示组件
 */
@Composable
fun PermissionUseCaseDemo(
    permissionManager: PermissionManager,
    onResultUpdate: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "使用场景演示",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // 相机功能场景
            UseCaseItem(
                title = "相机功能",
                description = "检查相机权限是否可用",
                icon = "\uEADD",
                onClick = {
                    val cameraGranted = permissionManager.isPermissionGranted(PermissionEnum.CAMERA.manifestPermission)
                    onResultUpdate("相机功能: ${if (cameraGranted) "可用" else "需要权限授权"}")
                }
            )

            // 位置服务场景
            UseCaseItem(
                title = "位置服务",
                description = "检查位置权限状态",
                icon = "\uED71",
                onClick = {
                    val locationPermissions = listOf(
                        PermissionEnum.ACCESS_FINE_LOCATION.manifestPermission,
                        PermissionEnum.ACCESS_COARSE_LOCATION.manifestPermission
                    )
                    val results = permissionManager.checkMultiplePermissions(locationPermissions)
                    val fineLocation = results[PermissionEnum.ACCESS_FINE_LOCATION.manifestPermission] == PermissionStatus.GRANTED
                    val coarseLocation = results[PermissionEnum.ACCESS_COARSE_LOCATION.manifestPermission] == PermissionStatus.GRANTED

                    val status = when {
                        fineLocation -> "精确位置可用"
                        coarseLocation -> "大致位置可用"
                        else -> "位置服务不可用"
                    }
                    onResultUpdate("位置服务: $status")
                }
            )

            // 存储访问场景
            UseCaseItem(
                title = "文件存储",
                description = "检查存储权限",
                icon = "\uEE6A",
                onClick = {
                    val storagePermissions = listOf(
                        PermissionEnum.READ_EXTERNAL_STORAGE.manifestPermission,
                        PermissionEnum.WRITE_EXTERNAL_STORAGE.manifestPermission
                    )
                    val allGranted = permissionManager.areAllPermissionsGranted(storagePermissions)
                    onResultUpdate("文件存储: ${if (allGranted) "完全访问" else "受限访问"}")
                }
            )

            // 通讯录访问场景
            UseCaseItem(
                title = "通讯录访问",
                description = "检查联系人权限",
                icon = "\uEADA",
                onClick = {
                    val contactsGranted = permissionManager.isPermissionGranted(PermissionEnum.READ_CONTACTS.manifestPermission)
                    onResultUpdate("通讯录访问: ${if (contactsGranted) "可访问" else "无法访问"}")
                }
            )

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // 权限分类演示部分
            PermissionCategorySection(
                permissionManager = permissionManager,
                onResultUpdate = onResultUpdate
            )
        }
    }
}

/**
 * 使用场景项组件
 */
@Composable
private fun UseCaseItem(
    title: String,
    description: String,
    icon: String,
    onClick: () -> Unit
) {
    Card(
        onClick = { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBackground(
                icon = icon,
                color = Color.Blue
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconBackground(
                icon = "\uEB3C",
                color = Color.Blue
            )
        }
    }
}



/**
 * 权限分类演示区域组件
 */
@Composable
private fun PermissionCategorySection(
    permissionManager: PermissionManager,
    onResultUpdate: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategoryInfo by remember { mutableStateOf<PermissionGroupInfo?>(null) }
    var categoryPermissions by remember { mutableStateOf<List<PermissionInfo>>(emptyList()) }
    val allCategories = remember { permissionManager.getAllPermissionGroups() }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "权限分类演示",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "点击分类查看详细权限信息",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 分类按钮列表
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(allCategories.filter { it.permissions.isNotEmpty() }.size) { index ->
                val categoryInfo = allCategories.filter { it.permissions.isNotEmpty() }[index]

                FilterChip(
                    onClick = {
                        selectedCategoryInfo = categoryInfo
                        categoryPermissions = categoryInfo.permissions
                        val grantedCount = categoryPermissions.count { it.isGranted }
                        val totalCount = categoryPermissions.size
                        onResultUpdate("${categoryInfo.displayName}: $grantedCount/$totalCount 权限已授权")
                        showDialog = true
                    },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconBackground(
                                icon = categoryInfo.icon,
                                color = Color(categoryInfo.color),
                            )
                            Text(
                                text = categoryInfo.displayName,
                                fontSize = 12.sp
                            )
                        }
                    },
                    selected = false
                )
            }
        }
    }

    // 权限详情弹出框
    if (showDialog && selectedCategoryInfo != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "${selectedCategoryInfo!!.displayName}权限详情",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = selectedCategoryInfo!!.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(categoryPermissions) { permissionInfo ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = permissionInfo.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = permissionInfo.permission,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (permissionInfo.isDangerous) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "危险权限",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Icon(
                                    imageVector = if (permissionInfo.isGranted) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = if (permissionInfo.isGranted) "已授权" else "未授权",
                                    tint = if (permissionInfo.isGranted) Color.Green else Color.Red,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("确定")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

/**
 * 权限检查工具组件
 */
@Composable
fun PermissionCheckTool(
    permissionManager: PermissionManager,
    onResultUpdate: (String) -> Unit
) {
    // 常用权限列表 - 使用权限枚举
    val commonPermissionEnums = listOf(
        PermissionEnum.CAMERA,
        PermissionEnum.RECORD_AUDIO,
        PermissionEnum.ACCESS_FINE_LOCATION,
        PermissionEnum.ACCESS_COARSE_LOCATION,
        PermissionEnum.READ_EXTERNAL_STORAGE,
        PermissionEnum.WRITE_EXTERNAL_STORAGE,
        PermissionEnum.READ_CONTACTS,
        PermissionEnum.CALL_PHONE,
        PermissionEnum.SEND_SMS,
        PermissionEnum.ACCESS_NETWORK_STATE,
        PermissionEnum.INTERNET,
        PermissionEnum.VIBRATE
    )
    
    // 转换为显示名称和权限字符串的配对列表
    val commonPermissions = commonPermissionEnums.map { permissionEnum ->
        permissionEnum.getDisplayName() to permissionEnum.manifestPermission
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "权限检查工具",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "点击按钮直接检查对应权限状态:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 权限检查按钮水平滚动列表
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(commonPermissions.size) { index ->
                    val (label, permission) = commonPermissions[index]
                                            Button(
                            onClick = {
                                val permissionInfo = permissionManager.getPermissionInfo(permission)
                                onResultUpdate("权限: $label\n完整名称: $permission\n状态: ${if (permissionInfo.isGranted) "已授权" else "未授权"}\n类型: ${if (permissionInfo.isDangerous) "危险权限" else "普通权限"}")
                            },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
