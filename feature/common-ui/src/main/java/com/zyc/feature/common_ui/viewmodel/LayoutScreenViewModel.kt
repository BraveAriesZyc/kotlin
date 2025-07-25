package com.zyc.feature.common_ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.zyc.feature.home.HomeScreen
import com.zyc.feature.message.MessageScreen
import com.zyc.feature.friend.FriendScreen
import com.zyc.feature.profile.MeScreen
import com.zyc.core.ui.R
import com.zyc.core.ui.components.drawer.NavigationDrawerItemType
import com.zyc.core.ui.route.WebViewRoute
import com.zyc.feature.common_ui.model.NavItem


class LayoutScreenViewModel(navController: NavController) : ViewModel() {
    // 当前页面状态
    private val _currentPage = mutableIntStateOf(0)
    val currentPage: State<Int> = _currentPage


    // 抽屉状态
    var isDrawerOpen by mutableStateOf(false)
        private set

    // 导航项列表
    val navItems = listOf(
        NavItem(
            title = "首页",
            icon = R.drawable.home,
            selectIcon = R.drawable.select_home,
            screen = { HomeScreen() }
        ),

        NavItem(
            title = "信息",
            icon = R.drawable.message,
            selectIcon = R.drawable.select_message,
            screen = { MessageScreen() }
        ),
        NavItem(
            title = "联系人",
            icon = R.drawable.friends,
            selectIcon = R.drawable.select_friends,
            screen = { FriendScreen() }
        ),
        NavItem(
            title = "我的",
            icon = R.drawable.my,
            selectIcon = R.drawable.select_my,
            screen = { MeScreen() }
        )
    )
    val drawerList = listOf(
        NavigationDrawerItemType(
            title = "华为应用市场",
            icon = "\uEA20",
            onClick = {
                navController.navigate(WebViewRoute("https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/myApp?menuId=97458334310914199"))
            }
        ),
        NavigationDrawerItemType(
            title = "百度",
            icon = "\uEE64",
            onClick = {
                navController.navigate(WebViewRoute("https://www.iconfont.cn/home/index?spm=a313x.collections_index.1998910419.2.44b63a81zIc8sP"))
            }
        ),
    )
    // 处理页面切换
    fun setCurrentPage(page: Int) {
        if (page != _currentPage.intValue) {
            _currentPage.intValue = page
        }
    }

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
