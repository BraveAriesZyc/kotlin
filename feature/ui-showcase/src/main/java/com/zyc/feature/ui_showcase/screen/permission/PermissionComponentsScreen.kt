package com.zyc.feature.ui_showcase.screen.permission


import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.permission.manager.PermissionManager
import com.zyc.core.permission.model.Permission
import com.zyc.core.ui.R
import com.zyc.core.ui.components.common.IconBackground
import com.zyc.core.ui.components.common.ZAppBar
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

    // 转换为权限字符串列表（用于兼容现有API）
    val commonPermissions = commonPermissionEnums.map { it.manifestPermission }

    var allPermissionsGranted by remember { mutableStateOf(false) }
    var dangerousPermissionsCount by remember { mutableStateOf(0) }

    // 初始化权限信息
    LaunchedEffect(Unit) {
        val permissionStatuses = permissionManager.checkPermissionsStatus(commonPermissions)
        allPermissionsGranted = permissionStatuses.values.all { it }

        // 计算危险权限数量（简化版本，实际应该根据权限类型判断）
        val dangerousPermissions = listOf(
            Permission.CAMERA.manifestPermission,
            Permission.MICROPHONE.manifestPermission,
            Permission.FINE_LOCATION.manifestPermission,
            Permission.COARSE_LOCATION.manifestPermission,
            Permission.READ_EXTERNAL_STORAGE.manifestPermission,
            Permission.WRITE_EXTERNAL_STORAGE.manifestPermission,
            Permission.READ_CONTACTS.manifestPermission,
            Permission.CALL_PHONE.manifestPermission,
            Permission.SEND_SMS.manifestPermission,
            Permission.READ_CALENDAR.manifestPermission
        )
        dangerousPermissionsCount = dangerousPermissions.size
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
                val grantedCount = commonPermissions.count { permission ->
                    permissionManager.checkPermissionStatus(permission)
                }

                PermissionStatsCard(
                    totalPermissions = commonPermissions.size,
                    grantedPermissions = grantedCount,
                    dangerousPermissions = dangerousPermissionsCount,
                    allGranted = grantedCount == commonPermissions.size
                )
                Spacer(modifier = Modifier.height(16.dp))
            }


            // 权限分类展示
            item {
                PermissionCategoryCard(
                    permissions = commonPermissionEnums,
                    permissionManager = permissionManager
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 权限详细检查工具
            item {
                PermissionDetailTool(
                    permissions = commonPermissionEnums,
                    permissionManager = permissionManager
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 权限使用场景演示
            item {
                PermissionUseCaseDemo(
                    permissionManager = permissionManager
                )
                Spacer(modifier = Modifier.height(16.dp))
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
) { Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // 标题行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "权限统计",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "应用权限状态概览",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconBackground(
                        icon = if (allGranted) "\uEB20" else "\uEA15",
                        color = if (allGranted)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 统计数据行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EnhancedStatItem(
                        label = "总权限",
                        value = totalPermissions.toString(),
                        color = MaterialTheme.colorScheme.secondary,
                        backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                    )
                    EnhancedStatItem(
                        label = "已授权",
                        value = grantedPermissions.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                    )
                    EnhancedStatItem(
                        label = "危险权限",
                        value = dangerousPermissions.toString(),
                        color = MaterialTheme.colorScheme.error,
                        backgroundColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 进度条
                val progress =
                    if (totalPermissions > 0) grantedPermissions.toFloat() / totalPermissions.toFloat() else 0f
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "授权进度",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )
                }
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
 * 增强版统计项组件
 */
@Composable
private fun EnhancedStatItem(
    label: String,
    value: String,
    color: Color,
    backgroundColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        modifier = Modifier
            .width(80.dp)
            .height(70.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


/**
 * 权限分类卡片
 */
@Composable
private fun PermissionCategoryCard(
    permissions: List<Permission>,
    permissionManager: PermissionManager
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "权限分类",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // 按类型分组权限
            val groupedPermissions = permissions.groupBy { getPermissionCategory(it) }

            groupedPermissions.forEach { (category, categoryPermissions) ->
                PermissionCategorySection(
                    category = category,
                    permissions = categoryPermissions,
                    permissionManager = permissionManager
                )
            }
        }
    }
}

/**
 * 权限分类部分
 */
@Composable
private fun PermissionCategorySection(
    category: String,
    permissions: List<Permission>,
    permissionManager: PermissionManager
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            val grantedCount = permissions.count {
                permissionManager.checkPermissionStatus(it.manifestPermission)
            }
            Text(
                text = "$grantedCount/${permissions.size}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        permissions.forEach { permission ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getPermissionDisplayName(permission),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )

                val isGranted = permissionManager.checkPermissionStatus(permission.manifestPermission)
                Icon(
                    imageVector = if (isGranted) Icons.Default.Check else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (isGranted) Color.Green else Color.Red,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * 权限详细检查工具 - 可点击的权限列表
 */
@Composable
private fun PermissionDetailTool(
    permissions: List<Permission>,
    permissionManager: PermissionManager
) {
    var selectedPermission by remember { mutableStateOf<Permission?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "权限详细信息",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "点击权限查看详细信息",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 权限列表
            permissions.forEach { permission ->
                ClickablePermissionItem(
                    permission = permission,
                    permissionManager = permissionManager,
                    onClick = {
                        selectedPermission = permission
                        showDialog = true
                    }
                )
            }
        }
    }

    // 权限详细信息弹窗
    if (showDialog && selectedPermission != null) {
        PermissionDetailDialog(
            permission = selectedPermission!!,
            permissionManager = permissionManager,
            onDismiss = { showDialog = false }
        )
    }
}

/**
 * 可点击的权限项
 */
@Composable
private fun ClickablePermissionItem(
    permission: Permission,
    permissionManager: PermissionManager,
    onClick: () -> Unit
) {
    val isGranted = permissionManager.checkPermissionStatus(permission.manifestPermission)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if(isGranted)  MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = getPermissionDisplayName(permission),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = getPermissionCategory(permission),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 状态指示器
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isGranted)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else
                        MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isGranted) Icons.Default.Check else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (isGranted)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // 箭头指示器
                // 箭头图标
                Text(
                    text = "\uEB3C",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = FontFamily(Font(R.font.icons)),
                )
            }
        }
    }
}

/**
 * 权限详细信息弹窗
 */
@Composable
private fun PermissionDetailDialog(
    permission: Permission,
    permissionManager: PermissionManager,
    onDismiss: () -> Unit
) {
    val isGranted = permissionManager.checkPermissionStatus(permission.manifestPermission)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (isGranted) Icons.Default.Check else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (isGranted) Color.Green else Color.Red
                )
                Text(
                    text = getPermissionDisplayName(permission),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 权限状态
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isGranted)
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else
                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "权限状态",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (isGranted) "已授权" else "未授权",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isGranted)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 权限信息
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DetailInfoRow("权限名称", getPermissionDisplayName(permission))
                        DetailInfoRow("权限分类", getPermissionCategory(permission))
                        DetailInfoRow("权限字符串", permission.manifestPermission)
                        DetailInfoRow("用途说明", getPermissionDescription(permission))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("确定")
            }
        }
    )
}

/**
 * 详细信息行
 */
@Composable
private fun DetailInfoRow(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 获取权限用途说明
 */
private fun getPermissionDescription(permission: Permission): String {
    return when (permission) {
        Permission.CAMERA -> "用于拍照、录制视频、扫描二维码等功能"
        Permission.MICROPHONE -> "用于录音、语音通话、语音识别等功能"
        Permission.FINE_LOCATION -> "用于获取精确位置信息，如导航、附近搜索等"
        Permission.COARSE_LOCATION -> "用于获取大致位置信息，如天气、地区服务等"
        Permission.BACKGROUND_LOCATION -> "用于在应用后台时获取位置信息"
        Permission.READ_EXTERNAL_STORAGE -> "用于读取设备存储中的文件、图片等"
        Permission.WRITE_EXTERNAL_STORAGE -> "用于保存文件、图片到设备存储"
        Permission.READ_PHONE_STATE -> "用于获取设备信息、通话状态等"
        Permission.CALL_PHONE -> "用于直接拨打电话"
        Permission.READ_CONTACTS -> "用于读取联系人信息"
        Permission.WRITE_CONTACTS -> "用于添加、修改联系人信息"
        Permission.SEND_SMS -> "用于发送短信"
        Permission.RECEIVE_SMS -> "用于接收短信"
        Permission.READ_CALENDAR -> "用于读取日历事件"
        Permission.WRITE_CALENDAR -> "用于添加、修改日历事件"
        Permission.BODY_SENSORS -> "用于访问健康传感器数据"
        Permission.POST_NOTIFICATIONS -> "用于显示通知消息"
        Permission.VIBRATE -> "用于设备震动反馈"
    }
}

/**
 * 权限使用场景演示
 */
@Composable
private fun PermissionUseCaseDemo(
    permissionManager: PermissionManager
) {
    var demoResult by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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

            // 场景按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val cameraGranted =
                            permissionManager.checkPermissionStatus(Permission.CAMERA.manifestPermission)
                        demoResult = "相机功能: ${if (cameraGranted) "可用" else "需要权限授权"}"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("拍照功能")
                }

                Button(
                    onClick = {
                        val locationPermissions = listOf(
                            Permission.FINE_LOCATION.manifestPermission,
                            Permission.COARSE_LOCATION.manifestPermission
                        )
                        val results = permissionManager.checkPermissionsStatus(locationPermissions)
                        val fineLocation = results[Permission.FINE_LOCATION.manifestPermission] == true
                        val coarseLocation = results[Permission.COARSE_LOCATION.manifestPermission] == true

                        val status = when {
                            fineLocation -> "精确位置可用"
                            coarseLocation -> "大致位置可用"
                            else -> "位置服务不可用"
                        }
                        demoResult = "位置服务: $status"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("定位功能")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val storagePermissions = listOf(
                            Permission.READ_EXTERNAL_STORAGE.manifestPermission,
                            Permission.WRITE_EXTERNAL_STORAGE.manifestPermission
                        )
                        val results = permissionManager.checkPermissionsStatus(storagePermissions)
                        val allGranted = results.values.all { it }
                        demoResult = "文件存储: ${if (allGranted) "完全访问" else "受限访问"}"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("文件访问")
                }

                Button(
                    onClick = {
                        val contactsGranted =
                            permissionManager.checkPermissionStatus(Permission.READ_CONTACTS.manifestPermission)
                        demoResult = "通讯录访问: ${if (contactsGranted) "可访问" else "无法访问"}"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("通讯录")
                }
            }

            // 结果显示
            demoResult?.let { result ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = result,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * 获取权限显示名称
 */
private fun getPermissionDisplayName(permission: Permission): String {
    return when (permission) {
        Permission.CAMERA -> "相机"
        Permission.MICROPHONE -> "麦克风"
        Permission.FINE_LOCATION -> "精确位置"
        Permission.COARSE_LOCATION -> "大致位置"
        Permission.BACKGROUND_LOCATION -> "后台位置"
        Permission.READ_EXTERNAL_STORAGE -> "读取存储"
        Permission.WRITE_EXTERNAL_STORAGE -> "写入存储"
        Permission.READ_PHONE_STATE -> "读取电话状态"
        Permission.CALL_PHONE -> "拨打电话"
        Permission.READ_CONTACTS -> "读取联系人"
        Permission.WRITE_CONTACTS -> "写入联系人"
        Permission.SEND_SMS -> "发送短信"
        Permission.RECEIVE_SMS -> "接收短信"
        Permission.READ_CALENDAR -> "读取日历"
        Permission.WRITE_CALENDAR -> "写入日历"
        Permission.BODY_SENSORS -> "身体传感器"
        Permission.POST_NOTIFICATIONS -> "发送通知"
        Permission.VIBRATE -> "震动"
    }
}

/**
 * 获取权限分类
 */
private fun getPermissionCategory(permission: Permission): String {
    return when (permission) {
        Permission.CAMERA, Permission.MICROPHONE -> "媒体权限"
        Permission.FINE_LOCATION, Permission.COARSE_LOCATION, Permission.BACKGROUND_LOCATION -> "位置权限"
        Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE -> "存储权限"
        Permission.READ_PHONE_STATE, Permission.CALL_PHONE -> "电话权限"
        Permission.READ_CONTACTS, Permission.WRITE_CONTACTS -> "联系人权限"
        Permission.SEND_SMS, Permission.RECEIVE_SMS -> "短信权限"
        Permission.READ_CALENDAR, Permission.WRITE_CALENDAR -> "日历权限"
        Permission.BODY_SENSORS -> "传感器权限"
        Permission.POST_NOTIFICATIONS -> "通知权限"
        Permission.VIBRATE -> "系统权限"
    }
}
