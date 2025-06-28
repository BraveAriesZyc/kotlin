package com.zyc.clover.components.drawer

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDrawer(
    drawerValue: Boolean = false,
    drawerList: List<NavigationDrawerItemType>,
    onClose: () -> Unit = {}, // 添加关闭回调
    content: @Composable () -> Unit
) {
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 跟踪抽屉状态的变化
    var lastDrawerValue by remember { mutableStateOf(drawerState.currentValue) }

    // 当外部状态变化时，更新抽屉状态
    LaunchedEffect(drawerValue) {
        if (drawerValue) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    // 当抽屉状态变化时，检查是否已关闭
    LaunchedEffect(drawerState) {
        snapshotFlow { drawerState.currentValue }
            .collect { value ->
                // 如果抽屉从打开变为关闭，触发关闭回调
                if (lastDrawerValue == DrawerValue.Open && value == DrawerValue.Closed) {
                    onClose()
                }
                lastDrawerValue = value
            }
    }
    val insets = WindowInsets.systemBars
    // 计算底部安全距离（导航栏高度）
    val topInset = with(LocalDensity.current) {
        insets.getTop(
            this
        ).toDp()
    }
    ModalNavigationDrawer(
        drawerState = drawerState,

        drawerContent = {
            ModalDrawerSheet(
                windowInsets = WindowInsets(0),
                drawerShape = RoundedCornerShape(0.dp),
                content = {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxHeight()
                            .padding(top =topInset)

                            .verticalScroll(rememberScrollState()),
                        content = {
                            drawerList.map {
                                NavigationDrawerItem(
                                    shape = RoundedCornerShape(2.dp),
                                    label = { Text(it.title) },
                                    icon = { Icon(it.icon, contentDescription = null) },
                                    selected = it.selected,
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                        it.onClick()
                                    }
                                )
                            }
                        }
                    )
                }
            )
        },
        content = {
            content()
        }
    )
}

class NavigationDrawerItemType(
    val title: String,
    val icon: ImageVector,
    val selected: Boolean = false,
    val onClick: () -> Unit = {}
)
