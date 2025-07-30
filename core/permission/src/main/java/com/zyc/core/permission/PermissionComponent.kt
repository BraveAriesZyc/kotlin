package com.zyc.core.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.core.permission.model.PermissionGroup
import com.zyc.core.permission.model.PermissionInfo

/**
 * 权限管理组件
 * 
 * 用于展示应用权限信息的可复用组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionComponent(
    modifier: Modifier = Modifier,
    viewModel: PermissionViewModel = viewModel(),
    showTitle: Boolean = true
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    // 初始化ViewModel
    LaunchedEffect(Unit) {
        viewModel.initialize(context)
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题（可选）
        if (showTitle) {
            Text(
                text = "权限管理",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        // 搜索框
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = viewModel::searchPermissions,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // 筛选选项
        FilterOptions(
            selectedGroup = uiState.selectedGroup,
            showOnlyDangerous = uiState.showOnlyDangerous,
            onGroupSelected = viewModel::filterByGroup,
            onDangerousFilterChanged = viewModel::showOnlyDangerousPermissions,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 权限统计
        PermissionStats(
            totalCount = uiState.allPermissions.size,
            dangerousCount = uiState.dangerousPermissions.size,
            grantedCount = uiState.allPermissions.count { it.isGranted },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 权限列表
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                ErrorMessage(
                    error = uiState.error,
                    onRetry = viewModel::refreshPermissions,
                    onDismiss = viewModel::clearError
                )
            }
            else -> {
                PermissionList(
                    permissions = uiState.filteredPermissions,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

/**
 * 搜索框组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("搜索权限...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索"
            )
        },
        modifier = modifier
    )
}

/**
 * 筛选选项组件
 */
@Composable
private fun FilterOptions(
    selectedGroup: PermissionGroup?,
    showOnlyDangerous: Boolean,
    onGroupSelected: (PermissionGroup?) -> Unit,
    onDangerousFilterChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // 危险权限筛选
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Checkbox(
                checked = showOnlyDangerous,
                onCheckedChange = onDangerousFilterChanged
            )
            Text(
                text = "只显示危险权限",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // 权限组筛选
        Text(
            text = "按权限组筛选:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyColumn(
            modifier = Modifier.height(120.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedGroup == null,
                    onClick = { onGroupSelected(null) },
                    label = { Text("全部") },
                    modifier = Modifier.padding(end = 8.dp, bottom = 4.dp)
                )
            }
            
            items(PermissionGroup.values()) { group ->
                FilterChip(
                    selected = selectedGroup == group,
                    onClick = { onGroupSelected(group) },
                    label = { Text(group.displayName) },
                    modifier = Modifier.padding(end = 8.dp, bottom = 4.dp)
                )
            }
        }
    }
}

/**
 * 权限统计组件
 */
@Composable
private fun PermissionStats(
    totalCount: Int,
    dangerousCount: Int,
    grantedCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("总计", totalCount.toString(), Color.Blue)
            StatItem("危险", dangerousCount.toString(), Color.Red)
            StatItem("已授权", grantedCount.toString(), Color.Green)
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
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * 权限列表组件
 */
@Composable
private fun PermissionList(
    permissions: List<PermissionInfo>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(permissions) { permission ->
            PermissionItem(
                permission = permission,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

/**
 * 权限项组件
 */
@Composable
private fun PermissionItem(
    permission: PermissionInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = permission.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = permission.permission,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row {
                    // 危险权限标识
                    if (permission.isDangerous) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "危险权限",
                            tint = Color.Orange,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    
                    // 权限状态图标
                    Icon(
                        imageVector = if (permission.isGranted) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = if (permission.isGranted) "已授权" else "未授权",
                        tint = if (permission.isGranted) Color.Green else Color.Red
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = permission.description,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 权限组标签
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = permission.group.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * 错误消息组件
 */
@Composable
private fun ErrorMessage(
    error: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "错误",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row {
                TextButton(onClick = onRetry) {
                    Text("重试")
                }
                TextButton(onClick = onDismiss) {
                    Text("关闭")
                }
            }
        }
    }
}