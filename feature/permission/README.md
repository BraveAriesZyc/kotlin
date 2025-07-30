# Permission Module

## 概述

权限管理模块用于管理和展示Android应用的权限清单，提供权限状态查看、分类筛选和搜索功能。

## 功能特性

### 🔍 权限展示
- 显示应用中所有已声明的权限
- 区分危险权限和普通权限
- 显示权限的授权状态
- 提供权限的详细描述

### 📊 权限分类
- 按权限组分类（相机、位置、存储等）
- 支持筛选显示特定类型权限
- 提供权限统计信息

### 🔎 搜索功能
- 支持按权限名称搜索
- 支持按权限描述搜索
- 实时搜索结果更新

### 📱 用户界面
- Material Design 3 设计风格
- 响应式布局
- 直观的权限状态指示
- 友好的错误处理

## 模块结构

```
feature/permission/
├── src/main/java/com/zyc/feature/permission/
│   ├── model/
│   │   └── PermissionInfo.kt          # 权限数据模型
│   ├── manager/
│   │   └── PermissionManager.kt       # 权限管理器
│   ├── PermissionViewModel.kt         # 视图模型
│   ├── PermissionScreen.kt           # 主界面
│   └── PermissionNavigation.kt       # 导航配置
├── build.gradle.kts                  # 模块构建配置（使用公共配置）
└── README.md                         # 模块文档
```

## 核心组件

### PermissionInfo
权限信息数据类，包含：
- 权限名称和描述
- 授权状态
- 危险权限标识
- 权限组分类

### PermissionManager
权限管理核心类，提供：
- 权限状态检查
- 权限信息获取
- 权限分类管理

### PermissionViewModel
视图模型，负责：
- UI状态管理
- 权限数据加载
- 筛选和搜索逻辑

### PermissionScreen
主界面组件，包含：
- 权限列表展示
- 搜索和筛选功能
- 权限统计信息

## 使用方法

### 1. 添加模块依赖

在需要使用权限管理功能的模块的 `build.gradle.kts` 中添加：

```kotlin
dependencies {
    implementation(project(":feature:permission"))
}
```

**注意：** 权限模块使用了项目的公共配置插件 `android-feature-convention`，自动包含了所有必要的依赖和配置。

### 2. 导航集成

在主导航图中添加权限管理路由：

```kotlin
import com.zyc.feature.permission.permissionScreen
import com.zyc.feature.permission.navigateToPermission

// 在NavGraphBuilder中添加
permissionScreen()

// 导航到权限管理页面
navController.navigateToPermission()
```

### 3. 界面使用

直接在Compose中使用权限管理界面：

```kotlin
import com.zyc.feature.permission.PermissionScreen

@Composable
fun MyScreen() {
    PermissionScreen()
}
```

## 权限分类

模块支持以下权限组分类：

- **相机** - 相机相关权限
- **麦克风** - 音频录制权限
- **位置** - 位置服务权限
- **存储** - 文件存储权限
- **通讯录** - 联系人权限
- **电话** - 电话相关权限
- **短信** - 短信权限
- **日历** - 日历权限
- **传感器** - 传感器权限
- **网络** - 网络权限
- **通知** - 通知权限
- **其他** - 其他系统权限

## 注意事项

1. 该模块主要用于权限信息的展示和管理，不包含权限请求功能
2. 权限状态检查基于当前应用的实际权限状态
3. 危险权限的识别基于Android系统的权限分类
4. 模块需要在Android环境中运行，依赖Context获取权限信息

## 扩展功能

未来可以考虑添加的功能：
- 权限请求功能
- 权限使用历史记录
- 权限风险评估
- 权限使用统计
- 导出权限报告