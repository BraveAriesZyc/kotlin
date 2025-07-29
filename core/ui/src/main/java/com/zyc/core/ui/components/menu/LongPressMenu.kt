package com.zyc.core.ui.components.menu

import MenuAction
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.zyc.core.ui.R

/**
 * 长按菜单容器组件
 * 支持动画效果、触觉反馈和优化的用户体验
 */
@Composable
fun LongPressMenuContainer(
    menuItems: List<MenuAction> = emptyList(),
    onTap: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(IntOffset.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .onSizeChanged { containerSize = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onTap?.invoke()
                    },
                    onLongPress = { offset ->
                        // 触觉反馈
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        // 计算菜单位置，确保不超出屏幕边界
                        val menuWidth = with(density) { 200.dp.toPx() }
                        val menuHeight = with(density) { (menuItems.size * 48 + (menuItems.size - 1) * 0.5).dp.toPx() }

                        val adjustedX = when {
                            offset.x + menuWidth > containerSize.width -> (containerSize.width - menuWidth).coerceAtLeast(
                                0f
                            )

                            else -> offset.x
                        }.toInt()

                        val adjustedY = when {
                            offset.y + menuHeight > containerSize.height -> (offset.y - menuHeight).coerceAtLeast(0f)
                            else -> offset.y
                        }.toInt()

                        menuOffset = IntOffset(x = adjustedX, y = adjustedY)
                        showMenu = true
                    }
                )
            }
    ) {
        content()

        if (showMenu) {
            Popup(
                offset = menuOffset,
                onDismissRequest = { showMenu = false },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                AnimatedVisibility(
                    visible = showMenu,
                    enter = fadeIn(animationSpec = tween(200)) + scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(200)
                    ),
                    exit = fadeOut(animationSpec = tween(150)) + scaleOut(
                        targetScale = 0.8f,
                        animationSpec = tween(150)
                    )
                ) {
                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .wrapContentHeight()
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(12.dp),
                                clip = false
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = null
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 0.dp)
                        ) {
                            menuItems.forEachIndexed { index, menuItem ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            content = {
                                                Text(
                                                    text = menuItem.icon,
                                                    fontSize = 24.sp,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    fontFamily = FontFamily(Font(R.font.icons)),

                                                    )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = menuItem.title,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        )
                                    },
                                    onClick = {
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        menuItem.onClickMenu()
                                        showMenu = false
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                                if (index < menuItems.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 12.dp),
                                        thickness = 0.5.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
