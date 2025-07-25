package com.zyc.feature.common_ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.core.ui.components.drawer.MenuDrawer
import com.zyc.core.ui.components.page.PageScreen
import com.zyc.core.ui.components.page.PageScreenData
import com.zyc.core.ui.route.LocalNavController
import com.zyc.feature.common_ui.model.NavItem
import com.zyc.feature.common_ui.viewmodel.LayoutScreenViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

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
