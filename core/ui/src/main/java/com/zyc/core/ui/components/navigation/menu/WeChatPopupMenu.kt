package com.zyc.core.ui.components.navigation.menu
import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.zyc.core.common.extensions.toIntOffset
import com.zyc.core.ui.R

import com.zyc.core.ui.utils.event.GlobalAntiShake
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick

// 常量定义
private const val MENU_ITEM_HEIGHT_DP = 56
private const val MENU_WIDTH_DP = 160
private const val BOTTOM_SAFE_MARGIN_PX = 100
private const val TOP_SAFE_MARGIN_PX = 50
private const val MENU_PADDING_DP = 8
private const val ITEM_HORIZONTAL_PADDING_DP = 16
private const val SHADOW_ELEVATION_DP = 8
private const val CORNER_RADIUS_DP = 8
private const val TEXT_SIZE_SP = 17

// 动画常量
private const val ANIMATION_DURATION_MS = 200
private const val SCALE_START_VALUE = 0.8f
private const val OVERLAY_ALPHA = 0.15f

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun WeContextMenu(
    position: IntOffset,
    options: List<MenuAction>,
    onCancel: () -> Unit,
    onTap: (index: Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // 动画状态
    var animationStarted by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
        label = "menu_alpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (animationStarted) 1f else SCALE_START_VALUE,
        animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
        label = "menu_scale"
    )

    // 启动动画
    LaunchedEffect(Unit) {
        animationStarted = true
    }

    // 计算菜单高度和屏幕高度
    val menuHeight = with(density) { (options.size * MENU_ITEM_HEIGHT_DP).dp.toPx().toInt() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx().toInt() }

    // 检查是否需要向上偏移
    val adjustedPosition = remember(position, menuHeight, screenHeight) {
        val bottomSpace = screenHeight - position.y
        if (bottomSpace < menuHeight + BOTTOM_SAFE_MARGIN_PX) {
            IntOffset(
                x = position.x,
                y = (position.y - menuHeight - TOP_SAFE_MARGIN_PX).coerceAtLeast(TOP_SAFE_MARGIN_PX)
            )
        } else {
            position
        }
    }

    // 透明遮罩层
    Popup(
        offset = IntOffset(0, 0),
        onDismissRequest = onCancel
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha)
                .background(Color.Black.copy(alpha = OVERLAY_ALPHA))
                .debounceClick { onCancel() }
        )
    }

    // 菜单内容
    Popup(offset = adjustedPosition, onDismissRequest = onCancel) {
        Column(
            modifier = Modifier
                .padding(MENU_PADDING_DP.dp)
                .width(MENU_WIDTH_DP.dp)
                .scale(scale)
                .alpha(alpha)
                .shadow(SHADOW_ELEVATION_DP.dp)
                .clip(RoundedCornerShape(CORNER_RADIUS_DP.dp))
                .background(MaterialTheme.colorScheme.surfaceBright)
                .navigationBarsPadding()
        ) {
            options.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(MENU_ITEM_HEIGHT_DP.dp)
                        .debounceClick { item.onClickMenu() }
                        .padding(horizontal = ITEM_HORIZONTAL_PADDING_DP.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    content = {
                        Text(
                            text = item.title,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = TEXT_SIZE_SP.sp
                        )
                        Text(
                            text = item.icon,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = TEXT_SIZE_SP.sp,
                            fontFamily = FontFamily(Font(R.font.icons))
                        )
                    }
                )
            }
        }
    }


}

@Stable
interface ContextMenuState {
    /**
     * 是否显示
     */
    val visible: Boolean

    /**
     * 显示菜单
     */
    fun show(position: IntOffset, options: List<MenuAction>, listIndex: Int)

    /**
     * 隐藏菜单
     */
    fun hide()
}

@Composable
fun rememberContextMenuState(onTap: (listIndex: Int, menuIndex: Int) -> Unit): ContextMenuState {
    val state = remember { ContextMenuStateImpl() }

    if (state.visible) {
        state.props?.let { props ->
            WeContextMenu(
                position = props.position,
                options = props.options,
                onCancel = { state.hide() },
            ) { menuIndex ->
                onTap(props.listIndex, menuIndex)
                state.hide()
            }
        }
    }

    return state
}

private class ContextMenuStateImpl : ContextMenuState {
    override var visible by mutableStateOf(false)
    var props by mutableStateOf<ContextMenuProps?>(null)
        private set

    override fun show(position: IntOffset, options: List<MenuAction>, listIndex: Int) {
        props = ContextMenuProps(position, options, listIndex)
        visible = true
    }

    override fun hide() {
        visible = false
    }
}

private data class ContextMenuProps(
    val position: IntOffset,
    val options: List<MenuAction>,
    val listIndex: Int
)

@Composable
fun Modifier.contextMenu(
    onLongPress: (IntOffset) -> Unit,
    onTap: (() -> Unit)? = null
): Modifier {
    var offset by remember { mutableStateOf(Offset.Zero) }

    return this
        .onGloballyPositioned {
            offset = it.positionInParent()
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    GlobalAntiShake.runWithDebounce {
                        onTap?.invoke()
                    }
                },
                onLongPress = {
                    onLongPress((offset + it).toIntOffset())
                }
            )
        }
}
