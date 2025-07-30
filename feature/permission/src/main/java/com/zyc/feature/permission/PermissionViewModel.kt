package com.zyc.feature.permission

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyc.feature.permission.manager.PermissionManager
import com.zyc.feature.permission.model.PermissionGroup
import com.zyc.feature.permission.model.PermissionInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 权限管理ViewModel
 */
class PermissionViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(PermissionUiState())
    val uiState: StateFlow<PermissionUiState> = _uiState.asStateFlow()
    
    private lateinit var permissionManager: PermissionManager
    
    /**
     * 初始化权限管理器
     */
    fun initialize(context: Context) {
        permissionManager = PermissionManager(context)
        loadPermissions()
    }
    
    /**
     * 加载权限信息
     */
    private fun loadPermissions() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val allPermissions = permissionManager.getAllDeclaredPermissions()
                val dangerousPermissions = permissionManager.getDangerousPermissions()
                val permissionsByGroup = permissionManager.getPermissionsByGroup()
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    allPermissions = allPermissions,
                    dangerousPermissions = dangerousPermissions,
                    permissionsByGroup = permissionsByGroup,
                    filteredPermissions = allPermissions
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
     * 刷新权限状态
     */
    fun refreshPermissions() {
        loadPermissions()
    }
    
    /**
     * 按组筛选权限
     */
    fun filterByGroup(group: PermissionGroup?) {
        val filtered = if (group == null) {
            _uiState.value.allPermissions
        } else {
            _uiState.value.allPermissions.filter { it.group == group }
        }
        
        _uiState.value = _uiState.value.copy(
            filteredPermissions = filtered,
            selectedGroup = group
        )
    }
    
    /**
     * 只显示危险权限
     */
    fun showOnlyDangerousPermissions(showOnly: Boolean) {
        val filtered = if (showOnly) {
            _uiState.value.dangerousPermissions
        } else {
            _uiState.value.allPermissions
        }
        
        _uiState.value = _uiState.value.copy(
            filteredPermissions = filtered,
            showOnlyDangerous = showOnly
        )
    }
    
    /**
     * 搜索权限
     */
    fun searchPermissions(query: String) {
        val filtered = if (query.isBlank()) {
            _uiState.value.allPermissions
        } else {
            _uiState.value.allPermissions.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.permission.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
            }
        }
        
        _uiState.value = _uiState.value.copy(
            filteredPermissions = filtered,
            searchQuery = query
        )
    }
    
    /**
     * 清除错误状态
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * 权限管理UI状态
 */
data class PermissionUiState(
    val isLoading: Boolean = false,
    val allPermissions: List<PermissionInfo> = emptyList(),
    val dangerousPermissions: List<PermissionInfo> = emptyList(),
    val permissionsByGroup: Map<PermissionGroup, List<PermissionInfo>> = emptyMap(),
    val filteredPermissions: List<PermissionInfo> = emptyList(),
    val selectedGroup: PermissionGroup? = null,
    val showOnlyDangerous: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null
)