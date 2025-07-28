package com.zyc.feature.common_page.components.slidedrawer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.zyc.core.ui.R

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
    content: @Composable () -> Unit = {},
) {
    val drawerWidth = screenWidth * drawerWidthRatio

    // 动画偏移量
    val offsetX by animateFloatAsState(
        targetValue = when (position) {
            DrawerPosition.LEFT -> if (isOpen) 0f else -drawerWidth.value
            DrawerPosition.RIGHT -> if (isOpen) 0f else drawerWidth.value
        },
        animationSpec = tween(durationMillis = 300),
        label = "${position.name.lowercase()}DrawerOffset"
    )

    // 背景遮罩透明度
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isOpen) 0.5f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "overlayAlpha"
    )

    val shouldShow = when (position) {
        DrawerPosition.LEFT -> isOpen || offsetX > -drawerWidth.value
        DrawerPosition.RIGHT -> isOpen || offsetX < drawerWidth.value
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
            .background(MaterialTheme.colorScheme.surfaceBright)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                                    color = item.color.copy(alpha = 0.3f)
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
                        .blur(0.2.dp), // 保持一致的内边距，对齐位置
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.icon,
                        color = item.color,
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.icons)),
                    )
                }
            }
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
