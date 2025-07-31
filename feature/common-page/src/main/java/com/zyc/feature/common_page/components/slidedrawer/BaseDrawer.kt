package com.zyc.feature.common_page.components.slidedrawer

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.zyc.core.ui.R
import com.zyc.core.ui.components.common.IconBackground

enum class DrawerPosition {
    LEFT, RIGHT
}

@Composable
fun BaseDrawer(
    isOpen: Boolean,
    onClose: () -> Unit,
    position: DrawerPosition,
    modifier: Modifier = Modifier,
    screenWidth: Dp,
    drawerWidthRatio: Float = 0.5f,
    dragOffset: Float = 0f, // 新增：拖拽偏移量
    isDragging: Boolean = false, // 新增：是否正在拖拽
    content: @Composable () -> Unit = {},
) {
    val drawerWidth = screenWidth * drawerWidthRatio

    // 计算偏移量：拖拽时使用实时偏移，否则使用动画偏移
    val offsetX by animateFloatAsState(
        targetValue = if (isDragging) {
            // 拖拽时直接使用拖拽偏移量
            when (position) {
                DrawerPosition.LEFT -> dragOffset.coerceAtLeast(-drawerWidth.value)
                DrawerPosition.RIGHT -> dragOffset.coerceAtMost(drawerWidth.value)
            }
        } else {
            // 非拖拽时使用正常的开关状态
            when (position) {
                DrawerPosition.LEFT -> if (isOpen) 0f else -drawerWidth.value
                DrawerPosition.RIGHT -> if (isOpen) 0f else drawerWidth.value
            }
        },
        animationSpec = if (isDragging) {
            // 拖拽时使用极短的动画，减少闪烁但保持响应性
            tween(durationMillis = 16, easing = LinearEasing) // 约1帧的时间
        } else {
            // 非拖拽时使用平滑的缓动动画，避免弹跳
            // 关闭时使用稍慢的动画，打开时使用正常速度
            val duration = if (isOpen) 300 else 400
            tween(
                durationMillis = duration,
                easing = FastOutSlowInEasing
            )
        },
        label = "${position.name.lowercase()}DrawerOffset"
    )

    // 背景遮罩透明度
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isDragging) {
            // 拖拽时根据拖拽进度计算透明度
            val dragProgress = when (position) {
                DrawerPosition.LEFT -> {
                    val progress = (dragOffset + drawerWidth.value) / drawerWidth.value
                    progress.coerceIn(0f, 1f)
                }

                DrawerPosition.RIGHT -> {
                    val progress = (drawerWidth.value - dragOffset) / drawerWidth.value
                    progress.coerceIn(0f, 1f)
                }
            }
            dragProgress * 0.5f // 最大透明度为0.5
        } else {
            // 非拖拽时使用正常的开关状态
            if (isOpen) 0.5f else 0f
        },
        animationSpec = if (isDragging) {
            // 拖拽时使用极短的动画，减少闪烁
            tween(durationMillis = 16, easing = LinearEasing)
        } else {
            // 非拖拽时使用与侧边栏同步的缓动动画
            val duration = if (isOpen) 300 else 400
            tween(
                durationMillis = duration,
                easing = FastOutSlowInEasing
            )
        },
        label = "overlayAlpha"
    )

    val shouldShow = when (position) {
        DrawerPosition.LEFT -> isOpen || isDragging || offsetX > -drawerWidth.value
        DrawerPosition.RIGHT -> isOpen || isDragging || offsetX < drawerWidth.value
    }

    if (shouldShow) {
        Box(
            modifier = modifier.fillMaxSize(),
            content = {
                // 背景遮罩
                if (overlayAlpha > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = overlayAlpha))
                            .clickable(
                                indication = null, // 关闭波纹
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    onClose()
                                }
                            )
                            .zIndex(1f)
                    )
                }

                // 抽屉
                val drawerShape = when (position) {
                    DrawerPosition.LEFT -> RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                    DrawerPosition.RIGHT -> RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                }

                val drawerOffsetX = when (position) {
                    DrawerPosition.LEFT -> offsetX.dp
                    DrawerPosition.RIGHT -> (screenWidth.value - drawerWidth.value + offsetX).dp
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(drawerWidth)
                        .offset(x = drawerOffsetX)
                        .zIndex(2f)
                        .clip(drawerShape),
                    content = {
                        content()
                    }
                )
            }
        )
    }
}

@Composable
fun DefaultDrawerItem(
    item: DefaultDrawerItemType,
    onItemClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { onItemClick() }
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceBright),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconBackground(
            icon = item.icon,
            color = item.color,
        )
        Spacer(modifier = Modifier.width(16.dp))

        // 标题
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        // 箭头图标
        Text(
            text = "\uEB3C",
            fontSize = 24.sp,
            color = item.color,
            fontFamily = FontFamily(Font(R.font.icons)),
        )
    }
}


data class DefaultDrawerItemType(
    val icon: String,
    val color: Color,
    val title: String,
    val onClick: () -> Unit
)


@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.baseDrawer(isDragging: Boolean): Modifier {
    return Modifier.let { modifier ->
        if (isDragging) {
            // 在拖拽时添加一个拦截所有滚动事件的nestedScroll
            modifier.nestedScroll(object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    return available // 消费所有滚动事件
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    return available // 消费所有滚动事件
                }
            })
        } else {
            modifier
        }
    }

}
