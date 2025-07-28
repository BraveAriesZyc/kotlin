package com.zyc.feature.common_page.components.slidedrawer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.R
import com.zyc.core.ui.components.drawer.NavigationDrawerItemType
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LeftDrawer(
    isOpen: Boolean,
    drawerList: List<DefaultDrawerItemType>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp,
    drawerWidthRatio: Float = 0.5f
) {
    BaseDrawer(
        isOpen = isOpen,
        onClose = onClose,
        position = DrawerPosition.LEFT,
        modifier = modifier,
        screenWidth = screenWidth,
        drawerWidthRatio = drawerWidthRatio,
        content = {
            Scaffold(
                content = { pd ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = pd.calculateTopPadding())
                            .padding(horizontal = 8.dp)
                            .background(MaterialTheme.colorScheme.background),
                        content = {
                            item {
                                // 用户信息区域
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    content = {
                                        Row(
                                            modifier = Modifier,
                                            verticalAlignment = Alignment.CenterVertically,

                                            content = {
                                                Column(
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    content = {
                                                        Text(
                                                            text = "32°",
                                                            fontSize = 20.sp,
                                                            fontFamily = FontFamily(Font(R.font.icons)),
                                                        )
                                                        Text(
                                                            text = "合肥",
                                                            fontFamily = FontFamily(Font(R.font.icons)),
                                                        )
                                                    }
                                                )

                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    "炎热"
                                                )
                                            }
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            horizontalArrangement = Arrangement.End,
                                            content = {
                                                Button(
                                                    title = "设置",
                                                    icon = "\uEE6D",
                                                    onClick = {
                                                        onClose()
                                                    }
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
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
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
            .padding(4.dp)
            .padding(horizontal = 4.dp)
            .debounceClick {
                onClick()
            },
        content = {
            // 箭头图标
            Text(
                text = icon,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.icons)),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                title,
                fontSize = 12.sp,
            )
        }
    )
}
