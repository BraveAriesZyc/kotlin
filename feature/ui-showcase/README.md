# UI Showcase Module

## 概述

UI Showcase 模块是一个专门用于展示项目中所有UI组件使用方法的模块。它提供了一个完整的组件库展示界面，帮助开发者了解和学习各种UI组件的用法。

## 功能特性

### 🎨 组件分类展示
- **通用组件**: ZAppBar等基础通用组件
- **表单组件**: 按钮、输入框等表单相关组件
- **反馈组件**: 加载动画、进度条等反馈组件
- **布局组件**: 页面布局、刷新视图等容器组件
- **导航组件**: 菜单等导航相关组件
- **交互组件**: 键盘输入等交互组件
- **动画组件**: Lottie动画等动效组件

### 📱 实时预览
- 每个组件都有实际的使用示例
- 支持交互式操作和参数调整
- 提供详细的使用说明和参数文档

### 🔧 开发工具
- 统一的展示工具函数
- 可复用的示例数据生成器
- 标准化的组件文档格式

## 模块结构

```
feature/ui-showcase/
├── src/main/java/com/zyc/feature/ui_showcase/
│   ├── UIShowcaseScreen.kt              # 主入口页面
│   ├── UIShowcaseRootNavigation.kt      # 导航配置
│   ├── ComponentShowcaseUtils.kt        # 展示工具函数
│   ├── CommonComponentsScreen.kt        # 通用组件展示
│   ├── FormComponentsScreen.kt          # 表单组件展示
│   ├── FeedbackComponentsScreen.kt      # 反馈组件展示
│   ├── LayoutComponentsScreen.kt        # 布局组件展示
│   ├── NavigationComponentsScreen.kt    # 导航组件展示
│   ├── InteractionComponentsScreen.kt   # 交互组件展示
│   └── AnimationComponentsScreen.kt     # 动画组件展示
├── build.gradle.kts                     # 模块构建配置
├── src/main/AndroidManifest.xml         # Android清单文件
└── README.md                            # 模块说明文档
```

## 使用方法

### 1. 在主应用中集成

```kotlin
// 在主应用的导航图中添加
NavHost(
    navController = navController,
    startDestination = "home"
) {
    // 其他路由...
    
    // 添加UI展示模块
    uiShowcaseGraph(
        navController = navController,
        onBack = { navController.popBackStack() }
    )
}
```

### 2. 导航到UI展示页面

```kotlin
// 在需要的地方调用
navController.navigateToUIShowcase()
```

### 3. 添加新的组件展示

如果需要添加新的组件展示，可以按照以下步骤：

1. 在对应的组件展示页面中添加新的 `ComponentDemo`
2. 使用 `ComponentShowcaseUtils.kt` 中的工具函数
3. 提供详细的使用说明和参数文档

示例：
```kotlin
ComponentDemo(
    title = "新组件示例",
    description = "这是一个新组件的使用示例"
) {
    // 组件使用示例
    NewComponent(
        parameter1 = "value1",
        parameter2 = "value2",
        onAction = {
            // 处理交互
        }
    )
}
```

## 工具函数说明

### ComponentSection
用于创建组件展示区域的容器，包含标题、描述和内容。

### ComponentDemo
用于展示单个组件的使用示例，包含演示标题、描述和实际组件。

### CodeShowcase
用于展示代码示例的卡片组件。

### ParameterCard
用于展示组件参数说明的卡片。

### SuggestionCard
用于展示使用建议的卡片。

### ShowcaseDataGenerator
提供示例数据生成功能，包括列表数据、用户数据、消息数据等。

## 依赖关系

本模块依赖以下核心模块：
- `core:common` - 通用工具和基础功能
- `core:ui` - UI组件库
- `core:model` - 数据模型

## 开发建议

1. **保持示例简洁**: 每个组件示例应该简洁明了，突出核心功能
2. **提供完整文档**: 为每个组件提供详细的参数说明和使用建议
3. **支持交互测试**: 尽可能提供交互式的示例，让开发者能够实际操作
4. **统一展示风格**: 使用统一的展示工具函数，保持界面风格一致
5. **及时更新**: 当UI组件库有更新时，及时更新对应的展示示例

## 注意事项

- 本模块仅用于开发和测试阶段，不应包含在生产版本中
- 所有示例数据都是模拟数据，不要用于实际业务逻辑
- 保持模块的独立性，避免与业务逻辑耦合

## 版本历史

- v1.0.0 - 初始版本，包含基础的组件展示功能
  - 支持7大类UI组件的展示
  - 提供完整的导航和交互功能
  - 包含统一的展示工具函数