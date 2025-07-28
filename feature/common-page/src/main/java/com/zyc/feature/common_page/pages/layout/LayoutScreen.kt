package com.zyc.feature.common_page.pages.layout

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zyc.core.ui.components.drawer.MenuDrawer
import com.zyc.core.ui.components.page.PageScreen
import com.zyc.core.ui.components.page.PageScreenData
import com.zyc.core.ui.route.LocalNavController
import com.zyc.feature.common_page.components.LeftDrawer
import com.zyc.feature.common_page.components.RightDrawer
import com.zyc.feature.common_page.model.NavItem
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LayoutScreen() {
    val navController = LocalNavController.current
    val layoutViewModel by remember { mutableStateOf(LayoutScreenViewModel(navController)) }
    val pagerState = rememberPagerState(pageCount = { layoutViewModel.navItems.size })

    // 屏幕配置
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current
    
    // 侧边栏宽度（与LeftDrawer和RightDrawer保持一致）
    val drawerWidth = screenWidth * 0.5f

    // 滑动阈值（屏幕宽度的25%）
    val swipeThreshold = with(density) { (screenWidth * 0.25f).toPx() }
    
    // 根据侧边栏状态计算主屏幕偏移量
    val mainContentOffset by animateFloatAsState(
        targetValue = when {
            layoutViewModel.isLeftDrawerOpen -> drawerWidth.value
            layoutViewModel.isRightDrawerOpen -> -drawerWidth.value
            else -> 0f
        },
        animationSpec = tween(durationMillis = 300),
        label = "mainContentOffset"
    )

    // 同步ViewModel与PagerState的状态
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                layoutViewModel.setCurrentPage(page)
            }
    }

    // 最后一页的索引
    val lastPageIndex = layoutViewModel.navItems.size - 1

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 主内容区域
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = mainContentOffset.dp) // 根据侧边栏状态动态偏移
                .zIndex(0f)
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                content = { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding())
                    ) {
                        // 创建NestedScrollConnection来处理滚动事件
                        val nestedScrollConnection = remember(pagerState.currentPage) {
                            object : NestedScrollConnection {
                                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                                    // 让HorizontalPager处理页面切换，不拦截滑动事件
                                    return Offset.Zero
                                }

                                override fun onPostScroll(
                                    consumed: Offset,
                                    available: Offset,
                                    source: NestedScrollSource
                                ): Offset {
                                    return Offset.Zero
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(nestedScrollConnection)
                        ) {
                            PageScreen(
                                data = PageScreenData(
                                    pagerState = pagerState,
                                    pageContents = layoutViewModel.navItems.map { it.screen }
                                )
                            )
                        }
                    }
                },
                bottomBar = {
                    BottomNavigationBar(layoutViewModel.navItems, pagerState)
                }
            )
        }

        // 左边缘手势检测区域（仅在第一页生效）
        if (pagerState.currentPage == 0) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp) // 增大到100dp
                    .align(Alignment.CenterStart)
                    .zIndex(10f) // 进一步提高zIndex
                    // .background(Color.Red.copy(alpha = 0.1f)) // 调试用边界已移除
                    .pointerInput(Unit) { // 使用Unit作为key，避免重复创建
                        detectDragGestures(
                            onDragStart = { offset ->
                                // 左边缘拖拽开始
                            },
                            onDragEnd = {
                                // 左边缘拖拽结束
                                if (layoutViewModel.leftDrawerOffset > swipeThreshold) {
                                    layoutViewModel.openLeftDrawer()
                                } else {
                                    layoutViewModel.closeLeftDrawer()
                                }
                                layoutViewModel.setLeftDrawerOffset(0f)
                            },
                            onDrag = { change, dragAmount ->
                                val horizontalDrag = dragAmount.x

                                // 简化逻辑：只要是向右滑动就处理
                                if (horizontalDrag > 0) {
                                    val newOffset = (layoutViewModel.leftDrawerOffset + horizontalDrag).coerceAtLeast(0f)
                                    layoutViewModel.setLeftDrawerOffset(newOffset)
                                    change.consume()
                                }
                            }
                        )
                    }
            )
        }

        // 右边缘手势检测区域（仅在最后一页生效）
        if (pagerState.currentPage == lastPageIndex) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp) // 增大到100dp
                    .align(Alignment.CenterEnd)
                    .zIndex(10f) // 进一步提高zIndex
                    // .background(Color.Blue.copy(alpha = 0.1f)) // 调试用边界已移除
                    .pointerInput(Unit) { // 使用Unit作为key，避免重复创建
                        detectDragGestures(
                            onDragStart = { offset ->
                                // 右边缘拖拽开始
                            },
                            onDragEnd = {
                                // 右边缘拖拽结束
                                if (abs(layoutViewModel.rightDrawerOffset) > swipeThreshold) {
                                    layoutViewModel.openRightDrawer()
                                } else {
                                    layoutViewModel.closeRightDrawer()
                                }
                                layoutViewModel.setRightDrawerOffset(0f)
                            },
                            onDrag = { change, dragAmount ->
                                val horizontalDrag = dragAmount.x

                                // 简化逻辑：只要是向左滑动就处理
                                if (horizontalDrag < 0) {
                                    val newOffset = (layoutViewModel.rightDrawerOffset + horizontalDrag).coerceAtMost(0f)
                                    layoutViewModel.setRightDrawerOffset(newOffset)
                                    change.consume()
                                }
                            }
                        )
                    }
            )
        }

        // 左侧边栏
        LeftDrawer(
            isOpen = layoutViewModel.isLeftDrawerOpen,
            drawerList = layoutViewModel.leftDrawerList,
            onClose = { layoutViewModel.closeLeftDrawer() },
            modifier = Modifier.zIndex(3f)
        )

        // 右侧边栏
        RightDrawer(
            isOpen = layoutViewModel.isRightDrawerOpen,
            drawerList = layoutViewModel.rightDrawerList,
            onClose = { layoutViewModel.closeRightDrawer() },
            modifier = Modifier.zIndex(3f)
        )
    }
}

@Composable
private fun BottomNavigationBar(
    navItems: List<NavItem>,
    pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()
    val insets = WindowInsets.systemBars
    // 计算底部安全距离（导航栏高度）
    val bottomInset = with(LocalDensity.current) {
        insets.getBottom(
            this
        ).toDp()
    }
    Row(
        Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                         // 底部导航栏拖拽开始
                     },
                     onDragEnd = {
                         // 底部导航栏拖拽结束
                     },
                     onDrag = { change, dragAmount ->
                         val horizontalDrag = dragAmount.x
                         
                         // 在第一页向右滑动打开左侧边栏
                         if (pagerState.currentPage == 0 && horizontalDrag > 0) {
                             change.consume()
                         }
                         // 在最后一页向左滑动打开右侧边栏
                         else if (pagerState.currentPage == navItems.size - 1 && horizontalDrag < 0) {
                             change.consume()
                         }
                     }
                )
            },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        content = {
            navItems.forEachIndexed { index, item ->
                BottomNavigationItem(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.requestScrollToPage(index)
                                }
                            }
                        )
                        .padding(bottom = bottomInset)
                        .padding(top = 15.dp, bottom = 15.dp)
                        .background(color = MaterialTheme.colorScheme.surface),
                    icon = if (pagerState.currentPage == index) item.selectIcon else item.icon,

                    )
            }
        }

    )
}

@Composable
fun BottomNavigationItem(
    modifier: Modifier = Modifier,
    icon: Int,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = icon,
            ),
            contentDescription = "item.title",
            modifier = Modifier.size(24.dp)
        )
    }
}
