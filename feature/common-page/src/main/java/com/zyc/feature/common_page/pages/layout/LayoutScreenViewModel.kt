package com.zyc.feature.common_page.pages.layout

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.zyc.core.router.Routes
import com.zyc.core.ui.R
import com.zyc.feature.common_page.components.slidedrawer.DefaultDrawerItemType
import com.zyc.feature.common_page.model.NavItem
import com.zyc.feature.friend.FriendScreen
import com.zyc.feature.home.HomeScreen
import com.zyc.feature.message.MessageScreen
import com.zyc.feature.profile.ProfileScreen

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
    private var _leftDrawerOffset by mutableFloatStateOf(0f)
    val leftDrawerOffset: Float get() = _leftDrawerOffset

    private var _rightDrawerOffset by mutableFloatStateOf(0f)
    val rightDrawerOffset: Float get() = _rightDrawerOffset

    // 导航项列表
    val navItems = listOf(
        NavItem(
            title = "首页",
            icon = R.drawable.home,
            selectIcon = R.drawable.select_home,
            screen = { HomeScreen(
                openDrawer = {
                    openLeftDrawer()
                }
            ) }
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
            screen = { ProfileScreen(
                openDrawer = {
                    openRightDrawer()
                }
            ) }
        )
    )
    // 左侧边栏数据
    val leftDrawerList = listOf(
        DefaultDrawerItemType(
            title = "华为应用市场",
            icon = "\uEA20",
            color = Color.Companion.Green,
            onClick = {
                navController.navigate(Routes.WebView("https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/myApp?menuId=97458334310914199"))
            }
        ),
        DefaultDrawerItemType(
            title = "百度",
            icon = "\uEE64",
            color = Color.Companion.Blue,
            onClick = {
                navController.navigate(Routes.WebView("https://www.iconfont.cn/home/index?spm=a313x.collections_index.1998910419.2.44b63a81zIc8sP"))
            }
        ),
        DefaultDrawerItemType(
            title = "春天",
            icon = "\uECB4",
            color = Color.Companion.Red,
            onClick = {
                navController.navigate(Routes.WebView("https://d3gbed2ley04jq.ldmnusfm.com/archives/136196"))
            }
        ),
    )

    // 右侧边栏数据
    val rightDrawerList = listOf(
        DefaultDrawerItemType(
            title = "个人设置",
            icon = "\uEE6D",
            color = Color.Companion.Gray,
            onClick = {
                // 个人设置逻辑
            }
        ),
        DefaultDrawerItemType(
            title = "隐私设置",
            icon = "\uED8c",
            color = Color.Companion.Blue,
            onClick = {
                // 隐私设置逻辑
            }
        ),
        DefaultDrawerItemType(
            title = "帮助与反馈",
            icon = "\uEC7F",
            color = Color.Companion.Green,
            onClick = {
                // 帮助与反馈逻辑
            }
        ),
        DefaultDrawerItemType(
            title = "关于我们",
            icon = "\uEC9A",
            color = Color.Companion.Red,
            onClick = {
                // 关于我们逻辑
            }
        ),
        DefaultDrawerItemType(
            title = "ui模块",
            icon = "\uEDC5",
            color = Color.Companion.Yellow,
            onClick = {
              navController.navigate(Routes.UIShowcase)
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
