package com.zyc.clover.pages.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Scaffold


import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zyc.clover.InitAppViewModel
import com.zyc.clover.components.drawer.MenuDrawer

import com.zyc.clover.components.page_screen.PageScreen
import com.zyc.clover.components.page_screen.PageScreenData
import com.zyc.clover.route.LocalNavController

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LayoutScreen() {
    val navController = LocalNavController.current
    val appView = koinViewModel<InitAppViewModel>()
    val viewModel by remember { mutableStateOf(LayoutScreenViewModel(navController)) }
    // 在Composable层创建PagerState
    val pagerState = rememberPagerState(pageCount = { viewModel.navItems.size })
    //
    val drawerState by viewModel.drawerState.collectAsState()
    // 同步ViewModel与PagerState的状态
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                viewModel.setCurrentPage(page)
            }
    }
    LaunchedEffect(Unit) {
        appView.initApp()
    }
    // 当 ViewModel 中的状态变化时，更新 DrawerState

    Box(
        modifier = Modifier.fillMaxSize(),
        content = {

            MenuDrawer(
                drawerList = viewModel.drawerList,
                content = {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        content = { pd ->
                            Box(
                                modifier = Modifier.fillMaxSize().padding(bottom = pd.calculateBottomPadding()),
                                content = {
                                    PageScreen(
                                        data = PageScreenData(
                                            pagerState = pagerState,
                                            pageContents = viewModel.navItems.map { it.screen }
                                        )
                                    )
                                }
                            )
                        },
                        bottomBar = {
                            BottomNavigationBar(viewModel.navItems, pagerState)
                        }
                    )
                }
            )
        }
    )
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
            .background(color = MaterialTheme.colorScheme.background),
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
                        .background(color = MaterialTheme.colorScheme.background),
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
