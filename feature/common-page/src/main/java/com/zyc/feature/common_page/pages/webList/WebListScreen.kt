package com.zyc.feature.common_page.pages.webList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.zyc.core.router.LocalNavController
import com.zyc.core.router.NavigationManager
import com.zyc.core.router.Routes
import com.zyc.core.ui.R
import com.zyc.core.ui.components.common.IconBackground
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick

@Composable
fun WebListScreen(
    onBack: () -> Unit = {},
) {
    val navController = LocalNavController.current
    val webListViewModel = viewModel<WebListViewModel>()
    val list by webListViewModel.webList.collectAsState()
    Scaffold(
        topBar = {
            ZAppBar(
                title = "网页列表",
                onBack = {
                    onBack()
                }
            )
        },
        content = { pd ->
            BounceListView(
                modifier = Modifier
                    .padding(top = pd.calculateTopPadding())
                    .padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                content = {
                    items(list.size) { it ->
                        WebListItem(
                            navController,
                            list[it]
                        )
                    }
                }
            )
        }
    )


}


@Composable
private fun WebListItem(
    navController: NavHostController,
    item: ItemType = ItemType("百度", "https://www.baidu.com", Color.Red)
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .debounceClick {
                NavigationManager(navController).navigateTo(Routes.Common.WebView(item.url))
            }
            .background(
                MaterialTheme.colorScheme.surfaceBright
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            IconBackground(
                icon = "\uECB4",
                color = item.color,
            )
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Text(text = item.title)
                }
            )
            Text(
                text = "\uEB3C",
                fontSize = 24.sp,
                color = item.color,
                fontFamily = FontFamily(
                    Font(
                        R.font.icons
                    )
                )
            )
        }
    )

}


data class ItemType(
    val title: String,
    val url: String,
    val color: Color
)
