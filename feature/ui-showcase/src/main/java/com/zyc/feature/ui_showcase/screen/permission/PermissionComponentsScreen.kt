package com.zyc.feature.ui_showcase.screen.permission


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zyc.core.permission.manager.PermissionManager
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

    // 常用权限列表
    val commonPermissions = listOf(
        "android.permission.CAMERA",
        "android.permission.RECORD_AUDIO",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.READ_CONTACTS",
        "android.permission.CALL_PHONE",
        "android.permission.SEND_SMS",
        "android.permission.ACCESS_NETWORK_STATE",
        "android.permission.INTERNET",
        "android.permission.VIBRATE"
    )

    var allPermissionsGranted by remember { mutableStateOf(false) }
    var dangerousPermissionsCount by remember { mutableStateOf(0) }

    // 初始化权限信息
    LaunchedEffect(Unit) {
        allPermissionsGranted = permissionManager.areAllPermissionsGranted(commonPermissions.toTypedArray())
        dangerousPermissionsCount = commonPermissions.count { permissionManager.isDangerousPermission(it) }
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
                    permissionManager.isPermissionGranted(permission)
                }

                PermissionStatsCard(
                    totalPermissions = commonPermissions.size,
                    grantedPermissions = grantedCount,
                    dangerousPermissions = dangerousPermissionsCount,
                    allGranted = grantedCount == commonPermissions.size
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

            

            // 权限检查工具
            item {
                PermissionCheckTool(
                    permissionManager = permissionManager,
                    onResultUpdate = { result ->
                        // 可以在这里处理结果显示
                    }
                )
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
    val gradientColors = if (allGranted) {
        listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors
                    )
                )
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
                        Text(
                            text = "应用权限状态概览",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (allGranted)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else
                            MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (allGranted) Icons.Default.Check else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (allGranted)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
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
                        color = MaterialTheme.colorScheme.onSurface,
                        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                    )
                    EnhancedStatItem(
                        label = "已授权",
                        value = grantedPermissions.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                    EnhancedStatItem(
                        label = "危险权限",
                        value = dangerousPermissions.toString(),
                        color = MaterialTheme.colorScheme.error,
                        backgroundColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
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

// FunctionDemoButtons 函数已删除

// PermissionInfoCard 函数已删除
