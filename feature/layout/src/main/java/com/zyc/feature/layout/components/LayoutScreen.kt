package com.zyc.feature.layout.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.feature.layout.viewmodel.LayoutScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun LayoutScreen(
    layoutViewModel: LayoutScreenViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 监听 ViewModel 中的抽屉状态变化
    LaunchedEffect(layoutViewModel.isDrawerOpen) {
        if (layoutViewModel.isDrawerOpen) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    // 监听抽屉状态变化，同步到 ViewModel
    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed && layoutViewModel.isDrawerOpen) {
            layoutViewModel.closeDrawer()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // 抽屉内容
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "菜单",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    // 这里可以添加更多抽屉菜单项
                }
            }
        },
        content = {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(
                        navItems = layoutViewModel.navItems,
                        selectedIndex = layoutViewModel.selectedIndex,
                        onItemSelected = { index ->
                            layoutViewModel.updateSelectedIndex(index)
                        }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // 显示当前选中的页面
                    layoutViewModel.navItems[layoutViewModel.selectedIndex].screen()
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(
    navItems: List<com.zyc.feature.layout.viewmodel.NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEachIndexed { index, item ->
            val isSelected = index == selectedIndex
            val iconRes = item.icon
            val textColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onItemSelected(index) }
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomNavigationItem(
                    icon = iconRes
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.title,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
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