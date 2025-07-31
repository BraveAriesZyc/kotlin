package com.zyc.feature.common_page.components.slidedrawer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.zyc.core.ui.R
import com.zyc.core.ui.components.layout.refreshview.BounceListView

import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LeftDrawer(
    isOpen: Boolean,
    drawerList: List<DefaultDrawerItemType>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp,
    drawerWidthRatio: Float = 0.5f,
    dragOffset: Float = 0f, // 新增：拖拽偏移量
    isDragging: Boolean = false // 新增：是否正在拖拽
) {
    BaseDrawer(
        isOpen = isOpen,
        onClose = onClose,
        position = DrawerPosition.LEFT,
        modifier = modifier,
        screenWidth = screenWidth,
        drawerWidthRatio = drawerWidthRatio,
        dragOffset = dragOffset,
        isDragging = isDragging,
        content = {
            Scaffold(
                content = { pd ->
                    BounceListView(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = pd.calculateTopPadding())
                            .background(MaterialTheme.colorScheme.background)
                            .baseDrawer(isDragging),
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                        content = {
                            item {
                                // 用户信息区域
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    content = {
                                        Row(
                                            modifier = Modifier,
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceEvenly,

                                            content = {
                                                Column(
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    content = {
                                                        Text(
                                                            text = "32°",
                                                            fontSize = 20.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            fontFamily = FontFamily(Font(R.font.icons)),
                                                        )
                                                        Row(
                                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            content = {
                                                                Text(
                                                                    text = "合肥",
                                                                    fontFamily = FontFamily(Font(R.font.icons)),
                                                                    fontSize = 12.sp,
                                                                )
                                                                Box(
                                                                    content = {
                                                                        Text(
                                                                            text = "\uEB3C",
                                                                            fontFamily = FontFamily(Font(R.font.icons)),
                                                                            fontSize = 14.sp,
                                                                        )
                                                                    }
                                                                )
                                                            }
                                                        )
                                                    }
                                                )
                                                Text(
                                                    "炎热",
                                                    fontSize = 14.sp,
                                                )
                                            }
                                        )
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            content = {
                                                Button(
                                                    title = "设置",
                                                    icon = "\uEE6D",
                                                    onClick = {
                                                        onClose()
                                                    }
                                                )
                                                Button(
                                                    title = "扫一扫",
                                                    icon = "\uEE5B",
                                                    onClick = {

                                                    }
                                                )
                                            }
                                        )

                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            items(drawerList) { item ->

                                DefaultDrawerItem(
                                    item = item,
                                    onItemClick = {
                                        item.onClick()
                                        onClose()
                                    }
                                )
                            }

                        })
                }
            )
        },
    )
}


@Composable
private fun Button(
    title: String,
    icon: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(40.dp))
            .background(MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.8f))
            .padding(4.dp)
            .padding(horizontal = 4.dp)
            .debounceClick {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        content = {
            // 箭头图标
            Text(
                text = icon,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.icons)),
            )
            Text(
                title,
                fontSize = 12.sp,
            )
        }
    )
}
