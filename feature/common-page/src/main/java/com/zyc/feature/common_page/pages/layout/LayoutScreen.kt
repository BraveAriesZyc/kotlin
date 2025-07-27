package com.zyc.feature.common_page.pages.layout

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
import com.zyc.core.ui.components.drawer.MenuDrawer
import com.zyc.core.ui.components.page.PageScreen
import com.zyc.core.ui.components.page.PageScreenData
import com.zyc.core.ui.route.LocalNavController
import com.zyc.feature.common_page.model.NavItem
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun LayoutScreen() {
    val navController = LocalNavController.current

    val layoutViewModel by remember { mutableStateOf(LayoutScreenViewModel(navController)) }
    val pagerState = rememberPagerState(pageCount = { layoutViewModel.navItems.size })
    // 同步ViewModel与PagerState的状态
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                layoutViewModel.setCurrentPage(page)
            }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        content = {

            MenuDrawer(
                drawerList = layoutViewModel.drawerList,
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
                                            pageContents = layoutViewModel.navItems.map { it.screen }
                                        )
                                    )
                                }
                            )
                        },
                        bottomBar = {
                             BottomNavigationBar(layoutViewModel.navItems, pagerState)
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
            .background(color = MaterialTheme.colorScheme.surface),
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
