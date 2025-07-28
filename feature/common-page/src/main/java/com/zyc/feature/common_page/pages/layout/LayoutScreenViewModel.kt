package com.zyc.feature.common_page.pages.layout

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.zyc.core.ui.R
import com.zyc.core.ui.components.drawer.NavigationDrawerItemType
import com.zyc.core.ui.route.WebViewRoute
import com.zyc.feature.common_page.components.slidedrawer.DefaultDrawerItemType
import com.zyc.feature.common_page.model.NavItem
import com.zyc.feature.friend.FriendScreen
import com.zyc.feature.home.HomeScreen
import com.zyc.feature.message.MessageScreen
import com.zyc.feature.profile.MeScreen

class LayoutScreenViewModel(navController: NavController) : ViewModel() {
    // 当前页面状态
    private val _currentPage = mutableIntStateOf(0)
    val currentPage: State<Int> = _currentPage


    // 左侧抽屉状态
    var isLeftDrawerOpen by mutableStateOf(false)
        private set

    // 右侧抽屉状态
    var isRightDrawerOpen by mutableStateOf(false)
        private set

    // 抽屉拖拽偏移量
    private var _leftDrawerOffset by mutableStateOf(0f)
    val leftDrawerOffset: Float get() = _leftDrawerOffset

    private var _rightDrawerOffset by mutableStateOf(0f)
    val rightDrawerOffset: Float get() = _rightDrawerOffset

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
    // 左侧边栏数据
    val leftDrawerList = listOf(
        DefaultDrawerItemType(
            title = "华为应用市场",
            icon = "\uEA20",
            color = Color.Companion.Green,
            onClick = {
                navController.navigate(WebViewRoute("https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/myApp?menuId=97458334310914199"))
            }
        ),
        DefaultDrawerItemType(
            title = "百度",
            icon = "\uEE64",
            color = Color.Companion.Blue,
            onClick = {
                navController.navigate(WebViewRoute("https://www.iconfont.cn/home/index?spm=a313x.collections_index.1998910419.2.44b63a81zIc8sP"))
            }
        ),
        DefaultDrawerItemType(
            title = "春天",
            icon = "\uECB4",
            color = Color.Companion.Red,
            onClick = {
                navController.navigate(WebViewRoute("https://d3gbed2ley04jq.ldmnusfm.com/archives/136196"))
            }
        ),
    )

    // 右侧边栏数据
    val rightDrawerList = listOf(
        DefaultDrawerItemType(
            title = "个人设置",
            icon = "\uE8B8",
            color = Color.Companion.Gray,
            onClick = {
                // 个人设置逻辑
            }
        ),
        DefaultDrawerItemType(
            title = "隐私设置",
            icon = "\uE8A1",
            color = Color.Companion.Blue,
            onClick = {
                // 隐私设置逻辑
            }
        ),
        DefaultDrawerItemType(
            title = "帮助与反馈",
            icon = "\uE8FD",
            color = Color.Companion.Green,
            onClick = {
                // 帮助与反馈逻辑
            }
        ),
        DefaultDrawerItemType(
            title = "关于我们",
            icon = "\uE88E",
            color = Color.Companion.Red,
            onClick = {
                // 关于我们逻辑
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

    // 左侧抽屉控制
    fun openLeftDrawer() {
        isLeftDrawerOpen = true
        isRightDrawerOpen = false // 确保只有一个抽屉打开
    }

    fun closeLeftDrawer() {
        isLeftDrawerOpen = false
    }

    fun toggleLeftDrawer() {
        isLeftDrawerOpen = !isLeftDrawerOpen
        if (isLeftDrawerOpen) {
            isRightDrawerOpen = false
        }
    }

    // 右侧抽屉控制
    fun openRightDrawer() {
        isRightDrawerOpen = true
        isLeftDrawerOpen = false // 确保只有一个抽屉打开
    }

    fun closeRightDrawer() {
        isRightDrawerOpen = false
    }

    fun toggleRightDrawer() {
        isRightDrawerOpen = !isRightDrawerOpen
        if (isRightDrawerOpen) {
            isLeftDrawerOpen = false
        }
    }

    // 关闭所有抽屉
    fun closeAllDrawers() {
        isLeftDrawerOpen = false
        isRightDrawerOpen = false
    }

    // 设置左侧抽屉偏移量
    fun setLeftDrawerOffset(offset: Float) {
        _leftDrawerOffset = offset
    }

    // 设置右侧抽屉偏移量
    fun setRightDrawerOffset(offset: Float) {
        _rightDrawerOffset = offset
    }
}
