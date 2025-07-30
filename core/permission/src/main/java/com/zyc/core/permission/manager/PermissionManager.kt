package com.zyc.core.permission.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.zyc.core.permission.model.PermissionInfo
import com.zyc.core.permission.model.PermissionStatus
import com.zyc.core.permission.model.PermissionCategory
import com.zyc.core.permission.model.PermissionCategoryInfo
import com.zyc.core.permission.model.PermissionCategoryHelper

/**
 * 权限管理器
 * 负责权限的检查和状态管理
 */
class PermissionManager(private val context: Context) {
    
    /**
     * 检查单个权限状态
     */
    fun checkPermissionStatus(permission: String): PermissionStatus {
        return when (ContextCompat.checkSelfPermission(context, permission)) {
            PackageManager.PERMISSION_GRANTED -> PermissionStatus.GRANTED
            PackageManager.PERMISSION_DENIED -> PermissionStatus.DENIED
            else -> PermissionStatus.NOT_REQUESTED
        }
    }
    
    /**
     * 检查权限是否已授权
     */
    fun isPermissionGranted(permission: String): Boolean {
        return checkPermissionStatus(permission) == PermissionStatus.GRANTED
    }
    
    /**
     * 检查多个权限状态
     */
    fun checkMultiplePermissions(permissions: Array<String>): Map<String, PermissionStatus> {
        return permissions.associateWith { checkPermissionStatus(it) }
    }
    
    /**
     * 检查多个权限是否全部已授权
     */
    fun areAllPermissionsGranted(permissions: Array<String>): Boolean {
        return permissions.all { isPermissionGranted(it) }
    }
    
    /**
     * 获取权限信息
     */
    fun getPermissionInfo(permission: String): PermissionInfo {
        return PermissionInfo(
            permission = permission,
            isGranted = isPermissionGranted(permission),
            isDangerous = isDangerousPermission(permission)
        )
    }
    
    /**
     * 获取多个权限信息
     */
    fun getMultiplePermissionInfo(permissions: Array<String>): List<PermissionInfo> {
        return permissions.map { getPermissionInfo(it) }
    }
    
    /**
     * 判断是否为危险权限
     */
    fun isDangerousPermission(permission: String): Boolean {
        return when (permission) {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR -> true
            else -> false
        }
    }
    
    /**
     * 获取权限分类
     */
    fun getPermissionCategory(permission: String): PermissionCategory {
        return PermissionCategoryHelper.getPermissionCategory(permission)
    }
    
    /**
     * 根据分类获取权限信息
     */
    fun getPermissionsByCategory(category: PermissionCategory): List<PermissionInfo> {
        val permissions = PermissionCategoryHelper.getPermissionsByCategory(category)
        return getMultiplePermissionInfo(permissions.toTypedArray())
    }
    
    /**
     * 获取所有分类的权限信息
     */
    fun getAllCategorizedPermissions(): Map<PermissionCategory, List<PermissionInfo>> {
        return PermissionCategory.values().associateWith { category ->
            getPermissionsByCategory(category)
        }.filterValues { it.isNotEmpty() }
    }
    
    /**
     * 获取分类信息
     */
    fun getCategoryInfo(category: PermissionCategory): PermissionCategoryInfo {
        return PermissionCategoryInfo(
            category = category,
            displayName = PermissionCategoryHelper.getCategoryDisplayName(category),
            description = PermissionCategoryHelper.getCategoryDescription(category),
            permissions = PermissionCategoryHelper.getPermissionsByCategory(category)
        )
    }
    
    /**
     * 获取所有分类信息
     */
    fun getAllCategoryInfo(): List<PermissionCategoryInfo> {
        return PermissionCategoryHelper.getAllCategories()
    }
    
    /**
     * 检查分类中的权限状态
     */
    fun checkCategoryPermissions(category: PermissionCategory): Map<String, PermissionStatus> {
        val permissions = PermissionCategoryHelper.getPermissionsByCategory(category)
        return checkMultiplePermissions(permissions.toTypedArray())
    }
    
    /**
     * 检查分类中的权限是否全部已授权
     */
    fun areCategoryPermissionsGranted(category: PermissionCategory): Boolean {
        val permissions = PermissionCategoryHelper.getPermissionsByCategory(category)
        return areAllPermissionsGranted(permissions.toTypedArray())
    }
    
}