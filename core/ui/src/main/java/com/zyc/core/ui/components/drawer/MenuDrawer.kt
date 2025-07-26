package com.zyc.core.ui.components.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.core.ui.R
import com.zyc.core.ui.components.drawer.DrawerViewModel
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick
import kotlinx.coroutines.launch

val ICON_SIZE = 24.sp

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
                                                                // 下层的模糊圆形Box
                                                                Box(
                                                                    modifier = Modifier
                                                                        .size(60.dp)
                                                                        .padding(16.dp)
                                                                        .drawBehind {
                                                                            drawIntoCanvas { canvas ->
                                                                                val paint = Paint().apply {
                                                                                    color = it.color.copy(alpha = 0.3f)
                                                                                    asFrameworkPaint().maskFilter =
                                                                                        android.graphics.BlurMaskFilter(
                                                                                            60f,
                                                                                            android.graphics.BlurMaskFilter.Blur.NORMAL
                                                                                        )
                                                                                }
                                                                                canvas.drawCircle(
                                                                                    center = Offset(
                                                                                        size.width / 2,
                                                                                        size.height / 2
                                                                                    ),
                                                                                    radius = size.minDimension / 2.5f,
                                                                                    paint
                                                                                )
                                                                            }
                                                                        },
                                                                    contentAlignment = Alignment.Center,
                                                                    content = {}
                                                                )

                                                                // 上层的文本Box（会重叠在下层Box上）
                                                                Box(
                                                                    modifier = Modifier
                                                                        .size(60.dp) // 与下层保持相同大小，确保居中重叠
                                                                        .padding(16.dp)
                                                                        .blur(0.2.dp)
                                                                    , // 保持一致的内边距，对齐位置
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Text(
                                                                        text = it.icon,
                                                                        color = it.color,
                                                                        fontSize = 24.sp,
                                                                        fontFamily = FontFamily(Font(R.font.icons)),
                                                                    )
                                                                }
                                                            }
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

// 数据类保持不变
class NavigationDrawerItemType(
    val title: String,
    val icon: String,
    val color: Color,
    val selected: Boolean = false,
    val onClick: () -> Unit = {}
)
