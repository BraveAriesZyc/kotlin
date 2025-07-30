package com.zyc.feature.ui_showcase.home.model

/**
 * 组件分类数据模型
 * @param title 分类标题
 * @param description 分类描述
 * @param route 导航路由
 * @param icon 图标字符
 * @param items 组件列表
 */
data class ComponentCategory(
    val title: String,
    val description: String,
    val route: Any,
    val icon: String,
    val items: List<String>
)


/**
 * UI状态数据类
 */
data class UIShowcaseHomeUiState(
    val categories: List<ComponentCategory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
