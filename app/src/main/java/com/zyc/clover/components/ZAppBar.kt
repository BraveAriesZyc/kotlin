package com.zyc.clover.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*


import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.clover.route.LayoutRoute
import com.zyc.clover.route.LocalNavController
import com.zyc.clover.route.RootRoute
import com.zyc.clover.utils.event.GlobalAntiShake.debounceClick
import kotlinx.coroutines.launch


@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    actions: @Composable () -> Unit? = {},
    navigationIcon: ImageVector = Icons.Filled.KeyboardArrowLeft,
    isNavigationIcon: Boolean = false,
    background: Color = MaterialTheme.colorScheme.background,
    onBack: () -> Unit = {}
) {
    // 获取导航控制器
    val navController = LocalNavController.current
    val list = navController.currentBackStack.value
    // 获取系统窗口
    val insets = WindowInsets.systemBars
    // 计算底部安全距离（导航栏高度）
    val topInset = with(LocalDensity.current) {
        insets.getTop(
            this
        ).toDp()
    }
    val routeListSize = remember { mutableIntStateOf(0) }
    LaunchedEffect(key1 = list) {

      val list2 =  list.filter {
            it.destination.route.hashCode() != RootRoute.hashCode() && it.destination.route.hashCode() != LayoutRoute.hashCode() && it.destination.route.hashCode() != 0
        }
        routeListSize.intValue = list2.size
    }
    Row(
        modifier.background(background).padding(top = topInset),
        verticalAlignment = Alignment.CenterVertically,
        content = {


            if (list.size > 3) {
                Box(
                    modifier = Modifier.weight(1f).padding(start = 14.dp),
                    content = {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = null,
                            modifier = Modifier.debounceClick {
                                onBack()
                            }
                        )
                    })
            } else {
                Box(modifier = Modifier.weight(1f)) {}
            }
            Column(
                Modifier.weight(1f).padding(bottom = 15.dp, top = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(title, fontSize = 16.sp, style = MaterialTheme.typography.bodyLarge)
                }
            )
            Row(modifier = Modifier.weight(1f).padding(end = 14.dp), horizontalArrangement = Arrangement.End) {
                actions()
            }

        })


}
