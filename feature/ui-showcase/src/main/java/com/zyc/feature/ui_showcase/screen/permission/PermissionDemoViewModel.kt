package com.zyc.feature.ui_showcase.screen.permission

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyc.core.permission.manager.PermissionManager
import com.zyc.core.permission.model.PermissionInfo
import com.zyc.core.permission.model.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 权限演示ViewModel
 * 
 * 管理权限演示页面的状态和业务逻辑
 */
class PermissionDemoViewModel(context: Context) : ViewModel() {
    
    private val permissionManager = PermissionManager(context)
    
    private val _uiState = MutableStateFlow(PermissionDemoUiState())
    val uiState: StateFlow<PermissionDemoUiState> = _uiState.asStateFlow()
    
    /**
     * 检查单个权限状态
     */
    fun checkSinglePermission(permission: String): Boolean {
        return permissionManager.isPermissionGranted(permission)
    }
    
    /**
     * 检查多个权限状态
     */
    fun checkMultiplePermissions(permissions: List<String>): Map<String, Boolean> {
        val statusMap = permissionManager.checkMultiplePermissions(permissions.toTypedArray())
        return statusMap.mapValues { it.value == PermissionStatus.GRANTED }
    }
    
    /**
     * 检查所有权限是否都已授权
     */
    fun areAllPermissionsGranted(permissions: List<String>): Boolean {
        return permissionManager.areAllPermissionsGranted(permissions.toTypedArray())
    }
    
    /**
     * 判断是否为危险权限
     */
    fun isDangerousPermission(permission: String): Boolean {
        return permissionManager.isDangerousPermission(permission)
    }
    
    /**
     * 获取权限信息
     */
    fun getPermissionInfo(permission: String): PermissionInfo {
        return permissionManager.getPermissionInfo(permission)
    }
    
    /**
     * 获取多个权限信息
     */
    fun getMultiplePermissionInfo(permissions: List<String>): List<PermissionInfo> {
        return permissionManager.getMultiplePermissionInfo(permissions.toTypedArray())
    }
    
    /**
     * 加载权限信息
     */
    fun loadPermissionInfo(permissions: List<String>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val permissionInfoList = getMultiplePermissionInfo(permissions)
                val allGranted = areAllPermissionsGranted(permissions)
                val dangerousCount = permissions.count { isDangerousPermission(it) }
                val grantedCount = permissionInfoList.count { it.isGranted }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    permissionInfoList = permissionInfoList,
                    totalPermissions = permissions.size,
                    grantedPermissions = grantedCount,
                    dangerousPermissions = dangerousCount,
                    allPermissionsGranted = allGranted,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "加载权限信息失败"
                )
            }
        }
    }
    
    /**
     * 清除错误状态
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * 更新演示结果
     */
    fun updateDemoResult(result: String) {
        _uiState.value = _uiState.value.copy(demoResult = result)
    }
    
    /**
     * 清除演示结果
     */
    fun clearDemoResult() {
        _uiState.value = _uiState.value.copy(demoResult = "")
    }
}

/**
 * 权限演示UI状态
 */
data class PermissionDemoUiState(
    val isLoading: Boolean = false,
    val permissionInfoList: List<PermissionInfo> = emptyList(),
    val totalPermissions: Int = 0,
    val grantedPermissions: Int = 0,
    val dangerousPermissions: Int = 0,
    val allPermissionsGranted: Boolean = false,
    val demoResult: String = "",
    val error: String? = null
)