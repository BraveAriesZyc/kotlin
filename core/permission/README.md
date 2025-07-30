# Permission Core Module

## 概述

权限核心模块专注于Android权限管理的核心功能，不包含任何UI组件。此模块提供简洁的API来检查和管理应用权限。

## 功能特性

- ✅ 权限状态检查
- ✅ 单个/多个权限检查
- ✅ 危险权限识别
- ✅ 权限信息获取
- ❌ 不包含UI组件
- ❌ 不包含权限请求逻辑

## 使用方法

### 基本用法

```kotlin
val permissionManager = PermissionManager(context)

// 检查单个权限
val isGranted = permissionManager.isPermissionGranted(Manifest.permission.CAMERA)

// 检查权限状态
val status = permissionManager.checkPermissionStatus(Manifest.permission.CAMERA)

// 检查多个权限
val permissions = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)
val allGranted = permissionManager.areAllPermissionsGranted(permissions)

// 获取权限信息
val permissionInfo = permissionManager.getPermissionInfo(Manifest.permission.CAMERA)
val multipleInfo = permissionManager.getMultiplePermissionInfo(permissions)

// 检查是否为危险权限
val isDangerous = permissionManager.isDangerousPermission(Manifest.permission.CAMERA)
```

### 权限状态

```kotlin
enum class PermissionStatus {
    GRANTED,        // 已授权
    DENIED,         // 被拒绝
    PERMANENTLY_DENIED, // 永久拒绝
    NOT_REQUESTED   // 未请求
}
```

### 权限信息

```kotlin
data class PermissionInfo(
    val permission: String,     // 权限名称
    val isGranted: Boolean,     // 是否已授权
    val isDangerous: Boolean    // 是否为危险权限
)
```

## 设计原则

1. **单一职责**: 只处理权限检查和状态管理
2. **无UI依赖**: 不包含任何UI组件或Compose依赖
3. **简洁API**: 提供简单易用的权限管理接口
4. **轻量级**: 最小化依赖，只依赖核心Android库

## 注意事项

- 此模块不处理权限请求，权限请求应在具体的feature模块中实现
- 如需UI展示权限信息，请在相应的feature模块中创建UI组件
- 权限请求的逻辑应使用Android官方的权限请求API或第三方库如Accompanist Permissions