package com.zyc.feature.common_page.pages.layout

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.zyc.core.router.Routes
import com.zyc.core.ui.R
import com.zyc.feature.common_page.components.slidedrawer.DefaultDrawerItemType
import com.zyc.feature.common_page.model.NavItem
import com.zyc.feature.friend.FriendScreen
import com.zyc.feature.home.HomeScreen
import com.zyc.feature.message.MessageScreen
import com.zyc.feature.profile.ProfileScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LayoutScreenViewModel(navController: NavController) : ViewModel() {
    // 当前页面状态
    private val _currentPage = mutableIntStateOf(0)
    val currentPage: State<Int> = _currentPage


    // 左侧抽屉状态
    private var _isLeftDrawerOpen = MutableStateFlow<Boolean>(false)
    var isLeftDrawerOpen: StateFlow<Boolean> = _isLeftDrawerOpen

    private var _isRightDrawerOpen = MutableStateFlow<Boolean>(false)
    var isRightDrawerOpen: StateFlow<Boolean> = _isRightDrawerOpen

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
            screen = {
                HomeScreen(
                    openDrawer = {
                        openLeftDrawer()
                    }
                )
            }
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
            screen = {
                ProfileScreen(
                    openDrawer = {
                        openRightDrawer()
                    }
                )
            }
        )
    )

    // 左侧边栏数据
    val leftDrawerList = listOf(
        DefaultDrawerItemType(
            title = "华为应用市场",
            icon = "\uEA20",
            color = Color.Companion.Red,
            onClick = {
                navController.navigate(Routes.Common.WebView("https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/myApp?menuId=97458334310914199"))
            }
        ),
        DefaultDrawerItemType(
            title = "百度",
            icon = "\uEE64",
            color = Color.Companion.Blue,
            onClick = {
                navController.navigate(Routes.Common.WebView("https://www.iconfont.cn/home/index?spm=a313x.collections_index.1998910419.2.44b63a81zIc8sP"))
            }
        ),
        DefaultDrawerItemType(
            title = "网站列表",
            icon = "\uEBC4",
            color = Color.Companion.Green,
            onClick = {
                viewModelScope.launch {
                    delay(3)
                    navController.navigate(Routes.Common.WebList)
                }
            }
        ),
//        DefaultDrawerItemType(
//            title = "夏天",
//            icon = "\uEA71",
//            color = Color.Companion.Yellow,
//            onClick = {
//                navController.navigate(Routes.Common.WebView("https://yandex.com"))
//            }
//        ),
//        DefaultDrawerItemType(
//            title = "秋天",
//            icon = "\uEB7C",
//            color = Color(0xe6d2b100),
//            onClick = {
//                navController.navigate(Routes.Common.WebView("https://d3op4betz9vht.cloudfront.net/"))
//            }
//        ),
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
                try {
                    closeRightDrawer()
                    // UI展示模块
                    viewModelScope.launch {
                        delay(3)
                        navController.navigate(Routes.UIShowcase.UIShowcase)
                    }

                } catch (e: Exception) {
                    Log.e("NavigationManager", "installUiShowcaseGraph error: ${e.message}")
                    e.printStackTrace()
                }

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
        _isLeftDrawerOpen.value = true
        _isRightDrawerOpen.value = false // 确保只有一个抽屉打开
    }

    fun closeLeftDrawer() {
        _isLeftDrawerOpen.value = false
    }

    fun toggleLeftDrawer() {
        _isLeftDrawerOpen.value = !_isLeftDrawerOpen.value
        if (_isLeftDrawerOpen.value) {
            _isRightDrawerOpen.value = false
        }
    }

    // 右侧抽屉控制
    fun openRightDrawer() {
        _isRightDrawerOpen.value = true
        _isLeftDrawerOpen.value = false // 确保只有一个抽屉打开
    }

    fun closeRightDrawer() {
        _isRightDrawerOpen.value = false
    }

    fun toggleRightDrawer() {
        _isRightDrawerOpen.value = !_isRightDrawerOpen.value
        if (_isRightDrawerOpen.value) {
            _isLeftDrawerOpen.value = false
        }
    }

    // 关闭所有抽屉
    fun closeAllDrawers() {
        _isLeftDrawerOpen.value = false
        _isRightDrawerOpen.value = false
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
