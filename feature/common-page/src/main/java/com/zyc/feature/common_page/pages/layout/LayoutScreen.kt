package com.zyc.feature.common_page.pages.layout

import android.annotation.SuppressLint
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
import com.zyc.core.ui.components.layout.page.PageScreen
import com.zyc.core.ui.components.layout.page.PageScreenData

import com.zyc.core.ui.route.LocalNavController
import com.zyc.feature.common_page.components.bottombar.BottomNavigationBar
import com.zyc.feature.common_page.components.slidedrawer.LeftDrawer
import com.zyc.feature.common_page.components.slidedrawer.RightDrawer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs

object DrawerConfig {
    const val DRAWER_WIDTH_RATIO = 0.7f
    const val SWIPE_THRESHOLD_RATIO = 0.25f
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LayoutScreen() {
    val navController = LocalNavController.current
    val layoutViewModel by remember { mutableStateOf(LayoutScreenViewModel(navController)) }
    val pagerState = rememberPagerState(pageCount = { layoutViewModel.navItems.size })

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current

    val drawerWidth = screenWidth * DrawerConfig.DRAWER_WIDTH_RATIO

    val swipeThreshold = with(density) { (screenWidth * DrawerConfig.SWIPE_THRESHOLD_RATIO).toPx() }
    val mainContentOffset by animateFloatAsState(
        targetValue = when {
            layoutViewModel.isLeftDrawerOpen -> drawerWidth.value
            layoutViewModel.isRightDrawerOpen -> -drawerWidth.value
            else -> 0f
        },
        animationSpec = tween(durationMillis = 300),
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


                                        if (pagerState.currentPage == 0 && horizontalDrag > 0) {
                                            if (layoutViewModel.leftDrawerOffset > 0f) {

                                                val newOffset =
                                                    (layoutViewModel.leftDrawerOffset + horizontalDrag).coerceAtLeast(0f)
                                                layoutViewModel.setLeftDrawerOffset(newOffset)
                                                return available
                                            }
                                        }


                                        if (pagerState.currentPage == lastPageIndex && horizontalDrag < 0) {
                                            if (layoutViewModel.rightDrawerOffset < 0f) {

                                                val newOffset =
                                                    (layoutViewModel.rightDrawerOffset + horizontalDrag).coerceAtMost(0f)
                                                layoutViewModel.setRightDrawerOffset(newOffset)
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
                                        val minDragThreshold = 20f


                                        if (pagerState.currentPage == 0 && horizontalDrag > minDragThreshold && layoutViewModel.leftDrawerOffset == 0f) {
                                            layoutViewModel.setLeftDrawerOffset(horizontalDrag)
                                            return available
                                        }


                                        if (pagerState.currentPage == lastPageIndex && horizontalDrag < -minDragThreshold && layoutViewModel.rightDrawerOffset == 0f) {
                                            layoutViewModel.setRightDrawerOffset(horizontalDrag)
                                            return available
                                        }

                                        return Offset.Zero
                                    }

                                    override suspend fun onPreFling(available: Velocity): Velocity {
                                        val horizontalVelocity = available.x


                                        if (pagerState.currentPage == 0 && horizontalVelocity > 0) {
                                            if (layoutViewModel.leftDrawerOffset > swipeThreshold) {
                                                layoutViewModel.openLeftDrawer()
                                            } else {
                                                layoutViewModel.closeLeftDrawer()
                                            }
                                            layoutViewModel.setLeftDrawerOffset(0f)
                                            return available
                                        }


                                        if (pagerState.currentPage == lastPageIndex && horizontalVelocity < 0) {
                                            if (abs(layoutViewModel.rightDrawerOffset) > swipeThreshold) {
                                                layoutViewModel.openRightDrawer()
                                            } else {
                                                layoutViewModel.closeRightDrawer()
                                            }
                                            layoutViewModel.setRightDrawerOffset(0f)
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
            isOpen = layoutViewModel.isLeftDrawerOpen,
            drawerList = layoutViewModel.leftDrawerList,
            onClose = { layoutViewModel.closeLeftDrawer() },
            screenWidth = screenWidth,
            drawerWidthRatio = DrawerConfig.DRAWER_WIDTH_RATIO
        )

        RightDrawer(
            isOpen = layoutViewModel.isRightDrawerOpen,
            drawerList = layoutViewModel.rightDrawerList,
            onClose = { layoutViewModel.closeRightDrawer() },
            screenWidth = screenWidth,
            drawerWidthRatio = DrawerConfig.DRAWER_WIDTH_RATIO
        )
    }
}


