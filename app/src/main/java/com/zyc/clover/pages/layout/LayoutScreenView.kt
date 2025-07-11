package com.zyc.clover.pages.layout


import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.zyc.clover.R
import com.zyc.clover.components.drawer.NavigationDrawerItemType
import com.zyc.clover.pages.layout.children.friend.FriendScreen
import com.zyc.clover.pages.layout.home.HomeScreen
import com.zyc.clover.pages.layout.children.me.MeScreen
import com.zyc.clover.pages.layout.children.message.MessageScreen
import com.zyc.clover.route.WebViewRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class LayoutScreenViewModel(navController: NavController) : ViewModel() {

    private val _drawerState = MutableStateFlow(false)
    val drawerState: StateFlow<Boolean> = _drawerState


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


    // 当前页面状态
    private val _currentPage = mutableIntStateOf(0)
    val currentPage: State<Int> = _currentPage


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
            screen = { FriendScreen(navController) }
        ),
        NavItem(
            title = "我的",
            icon = R.drawable.my,
            selectIcon = R.drawable.select_my,
            screen = { MeScreen() }
        )
    )


    // 处理页面切换
    fun setCurrentPage(page: Int) {
        if (page != _currentPage.intValue) {
            _currentPage.intValue = page
        }
    }

}


data class NavItem(
    val title: String = "",
    val screen: @Composable (() -> Unit) = {},
    val icon: Int,
    val selectIcon: Int
)
