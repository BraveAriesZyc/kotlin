package com.zyc.core.ui.components.navigation.menu

/**
 * 菜单操作数据类
 * 用于定义上下文菜单中的操作项
 */
data class MenuAction(
    val title: String,
    val icon: String,
    val onClickMenu: () -> Unit
)
