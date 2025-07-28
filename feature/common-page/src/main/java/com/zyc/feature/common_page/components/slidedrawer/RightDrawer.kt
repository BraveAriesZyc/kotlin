package com.zyc.feature.common_page.components.slidedrawer

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.drawer.NavigationDrawerItemType

@Composable
fun RightDrawer(
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
        position = DrawerPosition.RIGHT,
        modifier = modifier,
        screenWidth = screenWidth,
        drawerWidthRatio = drawerWidthRatio,
        content = {
            Scaffold(
                content = { pd ->
                    LazyColumn(
                        modifier = Modifier.padding(top = pd.calculateTopPadding()),
                        content = {
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
        }
    )
}
