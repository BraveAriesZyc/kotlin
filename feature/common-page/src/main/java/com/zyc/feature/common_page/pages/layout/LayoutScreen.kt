package com.zyc.feature.common_page.pages.layout

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.zyc.core.router.LocalNavController
import com.zyc.core.ui.components.layout.page.PageScreen
import com.zyc.core.ui.components.layout.page.PageScreenData
import com.zyc.feature.common_page.components.bottombar.BottomNavigationBar
import com.zyc.feature.common_page.components.slidedrawer.BaseDrawerConfig
import com.zyc.feature.common_page.components.slidedrawer.BaseDrawerViewModel
import com.zyc.feature.common_page.components.slidedrawer.LeftDrawer
import com.zyc.feature.common_page.components.slidedrawer.RightDrawer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs

// 使用BaseDrawerConfig和BaseDrawerViewModel替代本地配置

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LayoutScreen(
    isOverlayMode: Boolean = false // 默认为重叠模式
) {
    val navController = LocalNavController.current
    val drawerViewModel by remember { mutableStateOf(BaseDrawerViewModel()) }
    val layoutViewModel by remember {
        mutableStateOf(
            LayoutScreenViewModel(
                navController = navController,
                onOpenLeftDrawer = { drawerViewModel.openLeftDrawer() },
                onOpenRightDrawer = { drawerViewModel.openRightDrawer() }
            )
        )
    }
    val pagerState = rememberPagerState(pageCount = { layoutViewModel.navItems.size })
    val isLeftDrawerOpen by drawerViewModel.isLeftDrawerOpen.collectAsState()
    val isRightDrawerOpen by drawerViewModel.isRightDrawerOpen.collectAsState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current

    val drawerWidth = screenWidth * BaseDrawerConfig.DRAWER_WIDTH_RATIO

    val swipeThreshold = remember(screenWidth) {
        with(density) { (screenWidth * BaseDrawerConfig.SWIPE_THRESHOLD_RATIO).toPx() }
    }


    LaunchedEffect(isRightDrawerOpen){
        Log.d("LayoutScreen", "isOverlayMode: $isRightDrawerOpen")
    }



    val maxDragDistance = remember(drawerWidth) { drawerWidth * BaseDrawerConfig.MAX_DRAG_RATIO }
    val openThreshold = remember(drawerWidth) { drawerWidth * BaseDrawerConfig.OPEN_THRESHOLD_RATIO }
    // 根据isOverlayMode参数决定是否应用主内容偏移
    val mainContentOffset by animateFloatAsState(
        targetValue = if (isOverlayMode) {
            0f // 重叠模式：主内容不偏移
        } else {
            when {
                // 拖拽时实时偏移主内容
                drawerViewModel.isLeftDragging -> {
                    val dragProgress = (drawerViewModel.leftDrawerOffset + drawerWidth.value) / drawerWidth.value
                    (dragProgress * drawerWidth.value).coerceAtLeast(0f)
                }
                drawerViewModel.isRightDragging -> {
                    val dragProgress = (drawerWidth.value - drawerViewModel.rightDrawerOffset) / drawerWidth.value
                    -(dragProgress * drawerWidth.value).coerceAtLeast(0f)
                }
                // 正常开关状态
                isLeftDrawerOpen -> drawerWidth.value
                isRightDrawerOpen -> -drawerWidth.value
                else -> 0f
            }
        },
        animationSpec = if (drawerViewModel.isLeftDragging || drawerViewModel.isRightDragging) {
            tween(durationMillis = 0) // 拖拽时不使用动画
        } else {
            // 根据侧边栏状态调整动画时长
            val duration = when {
                isLeftDrawerOpen || isRightDrawerOpen -> 300 // 打开时稍快
                else -> 400 // 关闭时稍慢，更平滑
            }
            tween(
                durationMillis = duration,
                easing = FastOutSlowInEasing
            ) // 使用平滑缓动动画，避免弹跳
        },
        label = "mainContentOffset"
    )


    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                layoutViewModel.setCurrentPage(page)
            }
    }


    val lastPageIndex = layoutViewModel.navItems.size - 1

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = mainContentOffset.dp)
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                content = { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding()),
                        content = {
                            val nestedScrollConnection = remember(pagerState.currentPage, lastPageIndex) {
                                object : NestedScrollConnection {
                                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                                        val horizontalDrag = available.x

                                        // 左侧拖拽：在第一页且向右拖拽
                                        if (pagerState.currentPage == 0 && horizontalDrag > 0) {
                                            // 如果已经在拖拽中，继续更新偏移量
                                            if (drawerViewModel.isLeftDragging || drawerViewModel.leftDrawerOffset > 0f) {
                                                // 限制拖拽速度，防止闪烁
                                                val limitedDrag = BaseDrawerConfig.limitDragSpeed(horizontalDrag)
                                                // 应用阻力系数，提供更自然的手感
                                                val resistedDrag = limitedDrag * BaseDrawerConfig.DRAG_RESISTANCE_FACTOR
                                                // 计算目标偏移量
                                                val targetOffset = (drawerViewModel.leftDrawerOffset + resistedDrag)
                                                    .coerceAtLeast(-drawerWidth.value) // 最小值：完全隐藏
                                                    .coerceAtMost(-drawerWidth.value + maxDragDistance.value) // 最大值：限制拖拽距离
                                                // 应用平滑处理
                                                val smoothOffset = BaseDrawerConfig.smoothDrag(drawerViewModel.leftDrawerOffset, targetOffset)
                                                drawerViewModel.setLeftDrawerOffset(smoothOffset)
                                                drawerViewModel.setLeftDragging(true)
                                                return available // 完全消费事件，防止内容滚动
                                            }
                                        }

                                        // 左侧拖拽：反向拖拽时也要拦截事件
                                        if (pagerState.currentPage == 0 && horizontalDrag < 0 && drawerViewModel.isLeftDragging) {
                                            // 反向拖拽时继续更新偏移量
                                            val limitedDrag = BaseDrawerConfig.limitDragSpeed(horizontalDrag)
                                            val resistedDrag = limitedDrag * BaseDrawerConfig.DRAG_RESISTANCE_FACTOR
                                            val targetOffset = (drawerViewModel.leftDrawerOffset + resistedDrag)
                                                .coerceAtLeast(-drawerWidth.value)
                                                .coerceAtMost(-drawerWidth.value + maxDragDistance.value)
                                            val smoothOffset = BaseDrawerConfig.smoothDrag(drawerViewModel.leftDrawerOffset, targetOffset)
                                            drawerViewModel.setLeftDrawerOffset(smoothOffset)
                                            return available // 完全消费事件，防止内容滚动
                                        }

                                        // 右侧拖拽：在最后一页且向左拖拽
                                        if (pagerState.currentPage == lastPageIndex && horizontalDrag < 0) {
                                            // 如果已经在拖拽中，继续更新偏移量
                                            if (drawerViewModel.isRightDragging || drawerViewModel.rightDrawerOffset < 0f) {
                                                // 限制拖拽速度，防止闪烁
                                                val limitedDrag = BaseDrawerConfig.limitDragSpeed(horizontalDrag)
                                                // 应用阻力系数，提供更自然的手感
                                                val resistedDrag = limitedDrag * BaseDrawerConfig.DRAG_RESISTANCE_FACTOR
                                                // 计算目标偏移量
                                                val targetOffset = (drawerViewModel.rightDrawerOffset + resistedDrag)
                                                    .coerceAtMost(drawerWidth.value) // 最大值：完全隐藏
                                                    .coerceAtLeast(drawerWidth.value - maxDragDistance.value) // 最小值：限制拖拽距离
                                                // 应用平滑处理
                                                val smoothOffset = BaseDrawerConfig.smoothDrag(drawerViewModel.rightDrawerOffset, targetOffset)
                                                drawerViewModel.setRightDrawerOffset(smoothOffset)
                                                drawerViewModel.setRightDragging(true)
                                                return available // 完全消费事件，防止内容滚动
                                            }
                                        }

                                        // 右侧拖拽：反向拖拽时也要拦截事件
                                        if (pagerState.currentPage == lastPageIndex && horizontalDrag > 0 && drawerViewModel.isRightDragging) {
                                            // 反向拖拽时继续更新偏移量
                                            val limitedDrag = BaseDrawerConfig.limitDragSpeed(horizontalDrag)
                                            val resistedDrag = limitedDrag * BaseDrawerConfig.DRAG_RESISTANCE_FACTOR
                                            val targetOffset = (drawerViewModel.rightDrawerOffset + resistedDrag)
                                                .coerceAtMost(drawerWidth.value)
                                                .coerceAtLeast(drawerWidth.value - maxDragDistance.value)
                                            val smoothOffset = BaseDrawerConfig.smoothDrag(drawerViewModel.rightDrawerOffset, targetOffset)
                                            drawerViewModel.setRightDrawerOffset(smoothOffset)
                                            return available // 完全消费事件，防止内容滚动
                                        }

                                        return Offset.Zero
                                    }

                                    override fun onPostScroll(
                                        consumed: Offset,
                                        available: Offset,
                                        source: NestedScrollSource
                                    ): Offset {
                                        val horizontalDrag = available.x

                                        // 左侧拖拽开始：在第一页且向右拖拽，且还未开始拖拽
                                        if (pagerState.currentPage == 0 && horizontalDrag > BaseDrawerConfig.MIN_DRAG_THRESHOLD &&
                                            !drawerViewModel.isLeftDragging && drawerViewModel.leftDrawerOffset == 0f) {
                                            // 开始拖拽，设置初始偏移量为从完全隐藏开始，但限制最大拖拽距离
                                            val limitedDrag = BaseDrawerConfig.limitDragSpeed(horizontalDrag)
                                            val resistedDrag = limitedDrag * BaseDrawerConfig.DRAG_RESISTANCE_FACTOR
                                            val initialOffset = (-drawerWidth.value + resistedDrag)
                                                .coerceAtMost(-drawerWidth.value + maxDragDistance.value)
                                            drawerViewModel.setLeftDrawerOffset(initialOffset)
                                            drawerViewModel.setLeftDragging(true)
                                            return available // 完全消费事件，防止内容滚动
                                        }

                                        // 右侧拖拽开始：在最后一页且向左拖拽，且还未开始拖拽
                                        if (pagerState.currentPage == lastPageIndex && horizontalDrag < -BaseDrawerConfig.MIN_DRAG_THRESHOLD &&
                                            !drawerViewModel.isRightDragging && drawerViewModel.rightDrawerOffset == 0f) {
                                            // 开始拖拽，设置初始偏移量为从完全隐藏开始，但限制最大拖拽距离
                                            val limitedDrag = BaseDrawerConfig.limitDragSpeed(horizontalDrag)
                                            val resistedDrag = limitedDrag * BaseDrawerConfig.DRAG_RESISTANCE_FACTOR
                                            val initialOffset = (drawerWidth.value + resistedDrag)
                                                .coerceAtLeast(drawerWidth.value - maxDragDistance.value)
                                            drawerViewModel.setRightDrawerOffset(initialOffset)
                                            drawerViewModel.setRightDragging(true)
                                            return available // 完全消费事件，防止内容滚动
                                        }

                                        return Offset.Zero
                                    }

                                    override suspend fun onPreFling(available: Velocity): Velocity {
                                        val horizontalVelocity = available.x

                                        // 处理左侧拖拽结束
                                        if (drawerViewModel.isLeftDragging) {
                                            val dragDistance = drawerViewModel.leftDrawerOffset + drawerWidth.value
                                            val shouldOpen = dragDistance > openThreshold.value ||
                                                           horizontalVelocity > BaseDrawerConfig.VELOCITY_THRESHOLD

                                            if (shouldOpen) {
                                                drawerViewModel.openLeftDrawer()
                                            } else {
                                                drawerViewModel.closeLeftDrawer()
                                            }
                                            // 重置拖拽状态
                                            drawerViewModel.setLeftDrawerOffset(0f)
                                            drawerViewModel.setLeftDragging(false)
                                            return available
                                        }

                                        // 处理右侧拖拽结束
                                        if (drawerViewModel.isRightDragging) {
                                            val dragDistance = drawerWidth.value - drawerViewModel.rightDrawerOffset
                                            val shouldOpen = dragDistance > openThreshold.value ||
                                                           abs(horizontalVelocity) > BaseDrawerConfig.VELOCITY_THRESHOLD

                                            if (shouldOpen) {
                                                drawerViewModel.openRightDrawer()
                                            } else {
                                                drawerViewModel.closeRightDrawer()
                                            }
                                            // 重置拖拽状态
                                            drawerViewModel.setRightDrawerOffset(0f)
                                            drawerViewModel.setRightDragging(false)
                                            return available
                                        }

                                        return Velocity.Zero
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
                    )
                },
                bottomBar = {
                    BottomNavigationBar(layoutViewModel.navItems, pagerState)
                }
            )
        }

        LeftDrawer(
            isOpen =  isLeftDrawerOpen,
            drawerList = layoutViewModel.leftDrawerList,
            onClose = { drawerViewModel.closeLeftDrawer() },
            screenWidth = screenWidth,
            drawerWidthRatio = BaseDrawerConfig.DRAWER_WIDTH_RATIO,
            dragOffset = drawerViewModel.leftDrawerOffset,
            isDragging = drawerViewModel.isLeftDragging
        )

        RightDrawer(
            isOpen =  isRightDrawerOpen,
            drawerList = layoutViewModel.rightDrawerList,
            onClose = { drawerViewModel.closeRightDrawer() },
            screenWidth = screenWidth,
            drawerWidthRatio = BaseDrawerConfig.DRAWER_WIDTH_RATIO,
            dragOffset = drawerViewModel.rightDrawerOffset,
            isDragging = drawerViewModel.isRightDragging
        )
    }
}


