package com.zyc.feature.common_page.components.slidedrawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.zyc.core.ui.components.layout.refreshview.BounceListView


@Composable
fun RightDrawer(
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
        position = DrawerPosition.RIGHT,
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
                            items(drawerList) { item ->
                                DefaultDrawerItem(
                                    item = item,
                                    onItemClick = {
                                        item.onClick()
//                                        onClose()
                                    }
                                )
                            }
                        })
                }
            )
        }
    )
}
