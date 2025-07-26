package com.zyc.core.ui.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft

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
import kotlinx.coroutines.launch


@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    actions: @Composable () -> Unit? = {},
    navigationIcon: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    isNavigationIcon: Boolean = false,
    background: Color = MaterialTheme.colorScheme.surface,
    onBack: () -> Unit = {},
    navController: Any? = null,
    routeClass: Any? = null
) {
    // 获取系统窗口
    val insets = WindowInsets.systemBars
    // 计算底部安全距离（导航栏高度）
    val topInset = with(LocalDensity.current) {
        insets.getTop(
            this
        ).toDp()
    }
    val routeListSize = remember { mutableIntStateOf(0) }

    Row(
        modifier.background(background).padding(top = topInset),
        verticalAlignment = Alignment.CenterVertically,
        content = {

            // 简化导航逻辑，由调用方决定是否显示返回按钮
            if (routeListSize.intValue > 0) {
                Box(
                    modifier = Modifier.weight(1f).padding(start = 14.dp),
                    content = {
                        Icon(
                            imageVector = navigationIcon,
                            contentDescription = null,
                            modifier = Modifier.clickable {
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
