package com.zyc.feature.common_page.components.slidedrawer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

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
                            .clickable { onClose() }
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
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = drawerShape
                        )
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = item.color.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center,
                content = {
                    Text(
                        text = item.icon,
                        fontSize = 20.sp,
                        color = item.color
                    )
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
                text = "›",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}


data class DefaultDrawerItemType(
    val icon: String,
    val color: Color,
    val title: String,
    val onClick: () -> Unit
)
