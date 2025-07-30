package com.zyc.feature.ui_showcase.screen.permission

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.permission.manager.PermissionManager
import com.zyc.core.permission.model.PermissionInfo
import com.zyc.core.permission.model.PermissionStatus
import com.zyc.core.ui.components.common.IconBackground
import com.zyc.core.ui.utils.event.GlobalAntiShake
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick

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
                    val cameraGranted = permissionManager.isPermissionGranted(Manifest.permission.CAMERA)
                    onResultUpdate("相机功能: ${if (cameraGranted) "可用" else "需要权限授权"}")
                }
            )

            // 位置服务场景
            UseCaseItem(
                title = "位置服务",
                description = "检查位置权限状态",
                icon = "\uED71",
                onClick = {
                    val locationPermissions = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    val results = permissionManager.checkMultiplePermissions(locationPermissions)
                    val fineLocation = results[Manifest.permission.ACCESS_FINE_LOCATION] == PermissionStatus.GRANTED
                    val coarseLocation = results[Manifest.permission.ACCESS_COARSE_LOCATION] == PermissionStatus.GRANTED

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
                    val storagePermissions = arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                    val contactsGranted = permissionManager.isPermissionGranted(Manifest.permission.READ_CONTACTS)
                    onResultUpdate("通讯录访问: ${if (contactsGranted) "可访问" else "无法访问"}")
                }
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
 * 权限分类展示组件
 */
@Composable
fun PermissionCategoryDemo(
    permissionManager: PermissionManager
) {
    val allPermissions = listOf(
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
        Manifest.permission.VIBRATE,
        Manifest.permission.WAKE_LOCK,
        Manifest.permission.ACCESS_WIFI_STATE
    )

    val dangerousPermissions = allPermissions.filter { permissionManager.isDangerousPermission(it) }
    val normalPermissions = allPermissions.filter { !permissionManager.isDangerousPermission(it) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "权限分类展示",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // 危险权限
            PermissionCategorySection(
                title = "危险权限 (${dangerousPermissions.size})",
                permissions = dangerousPermissions,
                permissionManager = permissionManager,
                isDangerous = true
            )

            Divider()

            // 普通权限
            PermissionCategorySection(
                title = "普通权限 (${normalPermissions.size})",
                permissions = normalPermissions,
                permissionManager = permissionManager,
                isDangerous = false
            )
        }
    }
}

/**
 * 权限分类区域组件
 */
@Composable
private fun PermissionCategorySection(
    title: String,
    permissions: List<String>,
    permissionManager: PermissionManager,
    isDangerous: Boolean
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isDangerous) Icons.Default.Warning else Icons.Default.Share,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (isDangerous) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.heightIn(max = 200.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(permissions) { permission ->
                val isGranted = permissionManager.isPermissionGranted(permission)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isGranted)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            else
                                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = permission.substringAfterLast("."),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = if (isGranted) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (isGranted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
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
    var customPermission by remember { mutableStateOf("") }

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

            // 自定义权限输入
            OutlinedTextField(
                value = customPermission,
                onValueChange = { customPermission = it },
                label = { Text("输入权限名称") },
                placeholder = { Text("例如: android.permission.CAMERA") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 检查权限状态
                Button(
                    onClick = {
                        GlobalAntiShake.runWithDebounce {
                            if (customPermission.isNotEmpty()) {
                                val isGranted = permissionManager.isPermissionGranted(customPermission)
                                val isDangerous = permissionManager.isDangerousPermission(customPermission)
                                onResultUpdate("权限: $customPermission\n状态: ${if (isGranted) "已授权" else "未授权"}\n类型: ${if (isDangerous) "危险权限" else "普通权限"}")
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = customPermission.isNotEmpty()
                ) {
                    Text("检查状态")
                }

                // 获取权限信息
                Button(
                    onClick = {
                        GlobalAntiShake.runWithDebounce {
                            if (customPermission.isNotEmpty()) {
                                try {
                                    val permissionInfo = permissionManager.getPermissionInfo(customPermission)
                                    onResultUpdate("权限信息:\n权限: ${permissionInfo.permission}\n已授权: ${permissionInfo.isGranted}\n危险权限: ${permissionInfo.isDangerous}")
                                } catch (e: Exception) {
                                    onResultUpdate("获取权限信息失败: ${e.message}")
                                }
                            }

                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = customPermission.isNotEmpty()
                ) {
                    Text("获取信息")
                }
            }

            // 快速填充按钮
            Text(
                text = "快速填充:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "CAMERA" to Manifest.permission.CAMERA,
                    "位置" to Manifest.permission.ACCESS_FINE_LOCATION,
                    "存储" to Manifest.permission.READ_EXTERNAL_STORAGE
                ).forEach { (label, permission) ->
                    AssistChip(
                        onClick = { customPermission = permission },
                        label = { Text(label, fontSize = 12.sp) }
                    )
                }
            }
        }
    }
}
