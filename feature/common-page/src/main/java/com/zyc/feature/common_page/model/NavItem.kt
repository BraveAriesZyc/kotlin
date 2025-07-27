package com.zyc.feature.common_page.model

import androidx.compose.runtime.Composable

data class NavItem(
    val title: String,
    val icon: Int,
    val selectIcon: Int,
    val screen: @Composable () -> Unit
)