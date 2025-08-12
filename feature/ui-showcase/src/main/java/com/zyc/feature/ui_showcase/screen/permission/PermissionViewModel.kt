package com.zyc.feature.ui_showcase.screen.permission

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyc.core.permission.model.Permission
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PermissionViewModel : ViewModel() {
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _permissions = MutableStateFlow(
        listOf(
            PermissionModel(
                permission = Permission.READ_PHONE_STATE,
                icon = "\uEADA",
                iconColor = Color(0xFFFF5E5E),
            ),
            PermissionModel(
                permission = Permission.CAMERA,
                icon = "\uEADD",
                iconColor = Color(0xFF5E9FFF)
            ),
            PermissionModel(
                permission = Permission.MICROPHONE,
                icon = "\uED5D",
                iconColor = Color(0xFF66D2A0)
            ),
            PermissionModel(
                permission = Permission.FINE_LOCATION,
                icon = "\uED6F",
                iconColor = Color(0xFFB886FF)
            ),
            PermissionModel(
                permission = Permission.COARSE_LOCATION,
                icon = "\uED71",
                iconColor = Color(0xFFFFD700)
            ),
            PermissionModel(
                permission = Permission.READ_EXTERNAL_STORAGE,
                icon = "\uEBDF",
                iconColor = Color(0xFF8C42FF)
            ),
            PermissionModel(
                permission = Permission.WRITE_EXTERNAL_STORAGE,
                icon = "\uEBBB",
                iconColor = Color(0xFF98FB98)
            ),
            PermissionModel(
                permission = Permission.READ_CONTACTS,
                icon = "\uEDAB",
                iconColor = Color(0xFFDDA0DD)
            ),
            PermissionModel(
                permission = Permission.SEND_SMS,
                icon = "\uEB0E",
                iconColor = Color(0xFF20B2AA)
            ),
            PermissionModel(
                permission = Permission.READ_CALENDAR,
                icon = "\uEAD5",
                iconColor = Color(0xFFF0E68C)
            ),
            PermissionModel(
                permission = Permission.POST_NOTIFICATIONS,
                icon = "\uEE67",
                iconColor = Color(0xFF87CEFA)
            ),
            PermissionModel(
                permission = Permission.VIBRATE,
                icon = "\uED80",
                iconColor = Color(0xFF8A2BE2)
            ),
            PermissionModel(
                permission = Permission.NETWORK,
                icon = "\uEB05",
                iconColor = Color(0xFF5E9FFF)
            ),
        )
    )
    val permissions: StateFlow<List<PermissionModel>> = _permissions


}

data class PermissionModel(
    val permission: Permission,
    val icon: String,
    val iconColor: Color,
)