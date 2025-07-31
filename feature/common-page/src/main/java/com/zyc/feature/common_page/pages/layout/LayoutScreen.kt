package com.zyc.feature.common_page.pages.layout

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import com.zyc.feature.common_page.components.slidedrawer.LeftDrawer
import com.zyc.feature.common_page.components.slidedrawer.RightDrawer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs
import kotlin.math.sign

object DrawerConfig {
    const val DRAWER_WIDTH_RATIO = 0.7f
    const val SWIPE_THRESHOLD_RATIO = 0.25f
    const val MAX_DRAG_RATIO = 0.85f // 稍微增加最大拖拽距离，提供更好的视觉反馈
    const val MIN_DRAG_THRESHOLD = 8f // 降低阈值，提高敏感度
    const val OPEN_THRESHOLD_RATIO = 0.25f // 降低打开阈值，更容易触发
    const val ANIMATION_DURATION_MS = 350 // 动画持续时间，更平滑
    const val DRAG_RESISTANCE_FACTOR = 0.8f // 拖拽阻力系数，提供更自然的手感
    const val VELOCITY_THRESHOLD = 800f // 速度阈值，用于快速滑动判断
    const val MAX_DRAG_SPEED = 15f // 最大单次拖拽距离，防止闪烁
    const val SMOOTH_FACTOR = 0.7f // 平滑系数，减少突兀变化
    
    // 限制拖拽速度，防止视觉闪烁
    fun limitDragSpeed(dragValue: Float): Float {
        val maxSpeed = MAX_DRAG_SPEED
        return when {
            dragValue > maxSpeed -> maxSpeed
            dragValue < -maxSpeed -> -maxSpeed
            else -> dragValue
        }
    }
    
    // 平滑拖拽变化
    fun smoothDrag(currentOffset: Float, targetOffset: Float): Float {
        return currentOffset + (targetOffset - currentOffset) * SMOOTH_FACTOR
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LayoutScreen(
    isOverlayMode: Boolean = false // 默认为重叠模式
) {
    val navController = LocalNavController.current
    val layoutViewModel by remember { mutableStateOf(LayoutScreenViewModel(navController)) }
    val pagerState = rememberPagerState(pageCount = { layoutViewModel.navItems.size })
    val isLeftDrawerOpen by layoutViewModel.isLeftDrawerOpen.collectAsState()
    val isRightDrawerOpen by layoutViewModel.isRightDrawerOpen.collectAsState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current

    val drawerWidth = screenWidth * DrawerConfig.DRAWER_WIDTH_RATIO

    val swipeThreshold = remember(screenWidth) { 
        with(density) { (screenWidth * DrawerConfig.SWIPE_THRESHOLD_RATIO).toPx() }
    }
    val maxDragDistance = remember(drawerWidth) { drawerWidth * DrawerConfig.MAX_DRAG_RATIO }
    val openThreshold = remember(drawerWidth) { drawerWidth * DrawerConfig.OPEN_THRESHOLD_RATIO }
    // 根据isOverlayMode参数决定是否应用主内容偏移
    val mainContentOffset by animateFloatAsState(
        targetValue = if (isOverlayMode) {
            0f // 重叠模式：主内容不偏移
        } else {
            when {
                // 拖拽时实时偏移主内容
                layoutViewModel.isLeftDragging -> {
                    val dragProgress = (layoutViewModel.leftDrawerOffset + drawerWidth.value) / drawerWidth.value
                    (dragProgress * drawerWidth.value).coerceAtLeast(0f)
                }
                layoutViewModel.isRightDragging -> {
                    val dragProgress = (drawerWidth.value - layoutViewModel.rightDrawerOffset) / drawerWidth.value
                    -(dragProgress * drawerWidth.value).coerceAtLeast(0f)
                }
                // 正常开关状态
                isLeftDrawerOpen -> drawerWidth.value
                isRightDrawerOpen -> -drawerWidth.value
                else -> 0f
            }
        },
        animationSpec = if (layoutViewModel.isLeftDragging || layoutViewModel.isRightDragging) {
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
                                            if (layoutViewModel.isLeftDragging || layoutViewModel.leftDrawerOffset > 0f) {
                                                // 限制拖拽速度，防止闪烁
                                                val limitedDrag = DrawerConfig.limitDragSpeed(horizontalDrag)
                                                // 应用阻力系数，提供更自然的手感
                                                val resistedDrag = limitedDrag * DrawerConfig.DRAG_RESISTANCE_FACTOR
                                                // 计算目标偏移量
                                                val targetOffset = (layoutViewModel.leftDrawerOffset + resistedDrag)
                                                    .coerceAtLeast(-drawerWidth.value) // 最小值：完全隐藏
                                                    .coerceAtMost(-drawerWidth.value + maxDragDistance.value) // 最大值：限制拖拽距离
                                                // 应用平滑处理
                                                val smoothOffset = DrawerConfig.smoothDrag(layoutViewModel.leftDrawerOffset, targetOffset)
                                                layoutViewModel.setLeftDrawerOffset(smoothOffset)
                                                layoutViewModel.setLeftDragging(true)
                                                return available
                                            }
                                        }

                                        // 右侧拖拽：在最后一页且向左拖拽
                                        if (pagerState.currentPage == lastPageIndex && horizontalDrag < 0) {
                                            // 如果已经在拖拽中，继续更新偏移量
                                            if (layoutViewModel.isRightDragging || layoutViewModel.rightDrawerOffset < 0f) {
                                                // 限制拖拽速度，防止闪烁
                                                val limitedDrag = DrawerConfig.limitDragSpeed(horizontalDrag)
                                                // 应用阻力系数，提供更自然的手感
                                                val resistedDrag = limitedDrag * DrawerConfig.DRAG_RESISTANCE_FACTOR
                                                // 计算目标偏移量
                                                val targetOffset = (layoutViewModel.rightDrawerOffset + resistedDrag)
                                                    .coerceAtMost(drawerWidth.value) // 最大值：完全隐藏
                                                    .coerceAtLeast(drawerWidth.value - maxDragDistance.value) // 最小值：限制拖拽距离
                                                // 应用平滑处理
                                                val smoothOffset = DrawerConfig.smoothDrag(layoutViewModel.rightDrawerOffset, targetOffset)
                                                layoutViewModel.setRightDrawerOffset(smoothOffset)
                                                layoutViewModel.setRightDragging(true)
                                                return available
                                            }
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
                                        if (pagerState.currentPage == 0 && horizontalDrag > DrawerConfig.MIN_DRAG_THRESHOLD && 
                                            !layoutViewModel.isLeftDragging && layoutViewModel.leftDrawerOffset == 0f) {
                                            // 开始拖拽，设置初始偏移量为从完全隐藏开始，但限制最大拖拽距离
                                            val limitedDrag = DrawerConfig.limitDragSpeed(horizontalDrag)
                                            val resistedDrag = limitedDrag * DrawerConfig.DRAG_RESISTANCE_FACTOR
                                            val initialOffset = (-drawerWidth.value + resistedDrag)
                                                .coerceAtMost(-drawerWidth.value + maxDragDistance.value)
                                            layoutViewModel.setLeftDrawerOffset(initialOffset)
                                            layoutViewModel.setLeftDragging(true)
                                            return available
                                        }

                                        // 右侧拖拽开始：在最后一页且向左拖拽，且还未开始拖拽
                                        if (pagerState.currentPage == lastPageIndex && horizontalDrag < -DrawerConfig.MIN_DRAG_THRESHOLD && 
                                            !layoutViewModel.isRightDragging && layoutViewModel.rightDrawerOffset == 0f) {
                                            // 开始拖拽，设置初始偏移量为从完全隐藏开始，但限制最大拖拽距离
                                            val limitedDrag = DrawerConfig.limitDragSpeed(horizontalDrag)
                                            val resistedDrag = limitedDrag * DrawerConfig.DRAG_RESISTANCE_FACTOR
                                            val initialOffset = (drawerWidth.value + resistedDrag)
                                                .coerceAtLeast(drawerWidth.value - maxDragDistance.value)
                                            layoutViewModel.setRightDrawerOffset(initialOffset)
                                            layoutViewModel.setRightDragging(true)
                                            return available
                                        }

                                        return Offset.Zero
                                    }

                                    override suspend fun onPreFling(available: Velocity): Velocity {
                                        val horizontalVelocity = available.x
                                        
                                        // 处理左侧拖拽结束
                                        if (layoutViewModel.isLeftDragging) {
                                            val dragDistance = layoutViewModel.leftDrawerOffset + drawerWidth.value
                                            val shouldOpen = dragDistance > openThreshold.value || 
                                                           horizontalVelocity > DrawerConfig.VELOCITY_THRESHOLD
                                            
                                            if (shouldOpen) {
                                                layoutViewModel.openLeftDrawer()
                                            } else {
                                                layoutViewModel.closeLeftDrawer()
                                            }
                                            // 重置拖拽状态
                                            layoutViewModel.setLeftDrawerOffset(0f)
                                            layoutViewModel.setLeftDragging(false)
                                            return available
                                        }

                                        // 处理右侧拖拽结束
                                        if (layoutViewModel.isRightDragging) {
                                            val dragDistance = drawerWidth.value - layoutViewModel.rightDrawerOffset
                                            val shouldOpen = dragDistance > openThreshold.value || 
                                                           abs(horizontalVelocity) > DrawerConfig.VELOCITY_THRESHOLD
                                            
                                            if (shouldOpen) {
                                                layoutViewModel.openRightDrawer()
                                            } else {
                                                layoutViewModel.closeRightDrawer()
                                            }
                                            // 重置拖拽状态
                                            layoutViewModel.setRightDrawerOffset(0f)
                                            layoutViewModel.setRightDragging(false)
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
            onClose = { layoutViewModel.closeLeftDrawer() },
            screenWidth = screenWidth,
            drawerWidthRatio = DrawerConfig.DRAWER_WIDTH_RATIO,
            dragOffset = layoutViewModel.leftDrawerOffset,
            isDragging = layoutViewModel.isLeftDragging
        )

        RightDrawer(
            isOpen =  isRightDrawerOpen,
            drawerList = layoutViewModel.rightDrawerList,
            onClose = { layoutViewModel.closeRightDrawer() },
            screenWidth = screenWidth,
            drawerWidthRatio = DrawerConfig.DRAWER_WIDTH_RATIO,
            dragOffset = layoutViewModel.rightDrawerOffset,
            isDragging = layoutViewModel.isRightDragging
        )
    }
}


