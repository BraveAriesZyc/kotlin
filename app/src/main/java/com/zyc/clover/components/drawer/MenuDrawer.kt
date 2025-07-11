package com.zyc.clover.components.drawer

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.clover.utils.event.GlobalAntiShake.debounceClick
import com.zyc.clover.R

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDrawer(
    drawerList: List<NavigationDrawerItemType>,
    onClose: () -> Unit = {}, // 添加关闭回调
    content: @Composable () -> Unit,
    layout: Boolean = true
) {
    val drawerViewModel = viewModel<DrawerViewModel>()

    val showDrawer by drawerViewModel.drawerState.collectAsState()
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 跟踪抽屉状态的变化
    var lastDrawerValue by remember { mutableStateOf(drawerState.currentValue) }

    // 当外部状态变化时，更新抽屉状态
    LaunchedEffect(showDrawer) {
        if (showDrawer) {
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
    // 计算顶部安全距离（状态栏高度）
    val topInset = with(LocalDensity.current) {
        insets.getTop(
            this
        ).toDp()
    }

    val layout = remember(layout) {
        if (layout) LayoutDirection.Rtl else LayoutDirection.Ltr
    }
    CompositionLocalProvider(LocalLayoutDirection provides layout) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = false,
            scrimColor = Color.Transparent,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ModalDrawerSheet(
                        modifier = Modifier
                            .fillMaxSize(),
                        drawerContainerColor = Color.Transparent,
                        drawerTonalElevation = 0.dp,
                        windowInsets = WindowInsets(0),
                        drawerShape = RoundedCornerShape(0.dp),
                        content = {
                            // 创建一个可点击的背景，点击时关闭抽屉
                            Row(
                                content = {
                                    Spacer(
                                        modifier = Modifier.fillMaxHeight().weight(2f).background(Color.Transparent)
                                            .debounceClick {
                                                drawerViewModel.toggleDrawer()
                                            },
                                    )
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .background(MaterialTheme.colorScheme.onPrimary)
                                            .weight(3f)
                                            .padding(top = topInset)
                                            .verticalScroll(rememberScrollState()),
                                        content = {
                                            drawerList.map {
                                                NavigationDrawerItem(
                                                    modifier = Modifier.wrapContentWidth(),
                                                    shape = RoundedCornerShape(2.dp),
                                                    label = { Text(it.title) },
                                                    icon = {
                                                        Box(
                                                            content = {
                                                                // 上层清晰图标（使用 BoxScope 的 align 扩展函数居中对齐）
                                                                Text(
                                                                    text = it.icon,
                                                                    color = MaterialTheme.colorScheme.primary,
                                                                    fontSize = 24.sp,
                                                                    fontFamily = FontFamily(Font(R.font.icons)),
                                                                    modifier = Modifier.align(Alignment.Center)
                                                                )
                                                            },
                                                            modifier = Modifier
                                                                .wrapContentSize()
                                                                .clip(CircleShape)
                                                                .background(
                                                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                                                )
                                                                .padding(5.dp)
                                                        )
                                                    },
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
                        }
                    )
                }


            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    content()
                }
            }
        )
    }


}

class NavigationDrawerItemType(
    val title: String,
    val icon: String,
    val selected: Boolean = false,
    val onClick: () -> Unit = {}
)
