package com.zyc.feature.layout.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.zyc.feature.home.HomeScreen
import com.zyc.feature.message.MessageScreen
import com.zyc.feature.friend.FriendScreen
import com.zyc.feature.profile.MeScreen
import com.zyc.core.ui.R

data class NavItem(
    val title: String,
    val icon: Int,
    val screen: @Composable () -> Unit
)

class LayoutScreenViewModel : ViewModel() {
    // 抽屉状态
    var isDrawerOpen by mutableStateOf(false)
        private set

    // 导航项列表
    val navItems = listOf(
        NavItem(
            title = "首页",
            icon =  R.drawable.home,
            screen = { HomeScreen() }
        ),
        NavItem(
            title = "消息",
            icon = android.R.drawable.ic_menu_send,
            screen = { MessageScreen() }
        ),
        NavItem(
            title = "好友",
            icon = android.R.drawable.ic_menu_my_calendar,
            screen = { FriendScreen() }
        ),
        NavItem(
            title = "我的",
            icon = android.R.drawable.ic_menu_myplaces,
            screen = { MeScreen() }
        )
    )

    // 当前选中的导航项索引
    var selectedIndex by mutableIntStateOf(0)
        private set

    // 更新选中的导航项
    fun updateSelectedIndex(index: Int) {
        selectedIndex = index
    }

    // 切换抽屉状态
    fun toggleDrawer() {
        isDrawerOpen = !isDrawerOpen
    }

    // 关闭抽屉
    fun closeDrawer() {
        isDrawerOpen = false
    }
}
