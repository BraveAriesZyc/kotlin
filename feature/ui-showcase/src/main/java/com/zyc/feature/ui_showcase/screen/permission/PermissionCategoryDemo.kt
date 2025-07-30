package com.zyc.feature.ui_showcase.screen.permission

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.permission.manager.PermissionManager
import com.zyc.core.permission.model.PermissionCategory
import com.zyc.core.permission.model.PermissionCategoryInfo
import com.zyc.core.permission.model.PermissionInfo
import com.zyc.core.permission.model.PermissionCategoryHelper
import com.zyc.core.ui.components.common.IconBackground

/**
 * 权限分类演示组件
 */
@Composable
fun PermissionCategoryDemo(
    permissionManager: PermissionManager
) {
    var selectedCategory by remember { mutableStateOf<PermissionCategory?>(null) }
    var categoryPermissions by remember { mutableStateOf<List<PermissionInfo>>(emptyList()) }
    val allCategories = remember { permissionManager.getAllCategoryInfo() }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "权限分类演示",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "选择一个权限分类查看详细信息",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 分类选择器
            LazyColumn(
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allCategories.filter { it.permissions.isNotEmpty() }) { categoryInfo ->
                    CategoryCard(
                        categoryInfo = categoryInfo,
                        isSelected = selectedCategory == categoryInfo.category,
                        onClick = {
                            selectedCategory = categoryInfo.category
                            categoryPermissions = permissionManager.getPermissionsByCategory(categoryInfo.category)
                        }
                    )
                }
            }

            // 选中分类的权限详情
            selectedCategory?.let { category ->
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                Text(
                    text = "${permissionManager.getCategoryInfo(category).displayName}详情",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )

                if (categoryPermissions.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.height(150.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(categoryPermissions) { permissionInfo ->
                            PermissionDetailItem(permissionInfo = permissionInfo)
                        }
                    }
                } else {
                    Text(
                        text = "该分类下暂无权限",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 分类卡片
 */
@Composable
private fun CategoryCard(
    categoryInfo: PermissionCategoryInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected)
            CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary)
            )
        else
            CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconBackground(
                    color = Color(PermissionCategoryHelper.getCategoryColorHex(categoryInfo.category)),
                    icon = PermissionCategoryHelper.getCategoryIcon(categoryInfo.category),
                )

                Column {
                    Text(
                        text = categoryInfo.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                    Text(
                        text = "${categoryInfo.permissions.size} 个权限",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "已选中",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 权限详情项
 */
@Composable
private fun PermissionDetailItem(
    permissionInfo: PermissionInfo
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBackground(
                color = if (permissionInfo.isGranted) Color(0xFF4CAF50) else Color(0xFFF44336),
                icon = if (permissionInfo.isGranted) "\uEE5C" else "\uEE5D",
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = PermissionCategoryHelper.getPermissionSimpleName(permissionInfo.permission),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = permissionInfo.permission,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (permissionInfo.isDangerous) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "危险权限",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
