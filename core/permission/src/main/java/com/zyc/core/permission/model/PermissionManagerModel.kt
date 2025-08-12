package com.zyc.core.permission.model

class PermissionManagerModel(
    val permissionStatus: PermissionStatus,
    val permissionLauncher: () -> Unit
)