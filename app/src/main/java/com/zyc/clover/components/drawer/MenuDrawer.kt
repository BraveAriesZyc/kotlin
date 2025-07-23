package com.zyc.clover.components.drawer

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
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

val ICON_SIZE = 24.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDrawer(
    drawerList: List<NavigationDrawerItemType>,
    onClose: () -> Unit = {},
    content: @Composable () -> Unit,
    layout: Boolean = true
) {


    val drawerViewModel = viewModel<DrawerViewModel>()
    val showDrawer by drawerViewModel.drawerState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var lastDrawerValue by remember { mutableStateOf(drawerState.currentValue) }

    // 抽屉状态与外部状态同步（打开/关闭）
    LaunchedEffect(showDrawer, drawerState) {
        if (showDrawer && drawerState.currentValue == DrawerValue.Closed) {
            drawerState.open()
        } else if (!showDrawer && drawerState.currentValue == DrawerValue.Open) {
            drawerState.close()
        }
    }

    // 监听抽屉关闭事件，触发外部回调
    LaunchedEffect(drawerState) {
        snapshotFlow { drawerState.currentValue }
            .collect { currentValue ->
                if (lastDrawerValue == DrawerValue.Open && currentValue == DrawerValue.Closed) {
                    onClose()
                }
                lastDrawerValue = currentValue
            }
    }

    // 计算顶部安全区域（状态栏高度）
    val topInset = with(LocalDensity.current) {
        WindowInsets.systemBars.getTop(this).toDp()
    }

    // 布局方向：根据参数切换（默认LTR）
    val layoutDirection = remember(layout) {
        if (layout) LayoutDirection.Rtl else LayoutDirection.Ltr
    }

    // 独立组件：抽屉列表项（提取重复UI）
    @Composable
    fun DrawerItem(
        item: NavigationDrawerItemType
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clickable(
                    onClick = {
                        item.onClick()
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                .debounceClick(
                    onClick = {
                        item.onClick()
                        scope.launch {
                            drawerState.close()
                        }
                    }
                ),
            content = {
                Box(
                    modifier = Modifier.wrapContentSize(),
                    content = {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .padding(4.dp)
                                .drawBehind {
                                    drawIntoCanvas { canvas ->
                                        val paint = Paint().apply {
                                            color = item.color.copy(alpha = 0.3f)
                                            asFrameworkPaint().maskFilter =
                                                android.graphics.BlurMaskFilter(
                                                    60f,
                                                    android.graphics.BlurMaskFilter.Blur.NORMAL
                                                )
                                        }
                                        canvas.drawCircle(
                                            center = Offset(size.width / 2, size.height / 2),
                                            radius = size.minDimension / 2.5f,
                                            paint
                                        )
                                    }
                                },
                            contentAlignment = Alignment.Center,
                            content = {}
                        )
                        // 图标层
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.Center),
                            contentAlignment = Alignment.Center,
                            content = {
                                Text(
                                    text = item.icon,
                                    color = item.color,
                                    fontSize = ICON_SIZE,
                                    fontFamily = FontFamily(Font(R.font.icons))
                                )
                            }
                        )
                    }
                )

                Box(
                    modifier = Modifier.weight(1f),
                    content = {
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(start = 12.dp),
                            color = item.color,
                        )
                    }
                )
            })
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = false, // 禁用手势滑动（保持原有逻辑）
            scrimColor = Color.Transparent, // 无遮罩色
            drawerContent = {
                // 强制抽屉内容使用LTR布局（避免文字方向混乱）
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ModalDrawerSheet(
                        modifier = Modifier.fillMaxSize(),
                        drawerContainerColor = Color.Transparent,
                        drawerTonalElevation = 0.dp,
                        windowInsets = WindowInsets(0.dp), // 移除默认内边距
                        drawerShape = RoundedCornerShape(0.dp)
                    ) {
                        // 点击空白区域关闭抽屉
                        Row {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(2f)
                                    .background(Color.Transparent)
                                    .debounceClick { drawerViewModel.toggleDrawer() }
                            )

                            // 抽屉内容列表
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .background(
                                        MaterialTheme.colorScheme.onPrimary
                                    )
                                    .weight(3f)
                                    .padding(top = topInset)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                drawerList.forEach { DrawerItem(item = it) }
                            }
                        }
                    }
                }
            },
            content = {
                // 主内容区域强制LTR布局
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    content()
                }
            }
        )
    }
}

// 数据类保持不变
class NavigationDrawerItemType(
    val title: String,
    val icon: String,
    val color: Color,
    val onClick: () -> Unit = {}
)
