package com.zyc.feature.profile.pages.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.core.router.LocalNavController
import com.zyc.core.ui.R
import com.zyc.core.ui.components.common.IconBackground
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick

@Composable
fun SettingScreen() {
    val navController = LocalNavController.current
    val settingViewModel = viewModel { SettingViewModel(navController) }
    val settingList by settingViewModel.settingList.collectAsState()
    Scaffold(
        topBar = {
            ZAppBar(
                title = "设置",
            )
        },
        content = { pd ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = pd.calculateTopPadding()),
                content = {
                    BounceListView(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)
                    ) {
                        items(settingList.size) { i ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .clip(
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(MaterialTheme.colorScheme.surfaceBright)
                                    .debounceClick {
                                        settingList[i].onClick()
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                content = {

                                    IconBackground(
                                        icon = settingList[i].icon,
                                        color = settingList[i].color,
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    // 标题
                                    Text(
                                        text = settingList[i].title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )

                                    // 箭头图标
                                    Text(
                                        text = "\uEB3C",
                                        fontSize = 24.sp,
                                        color = settingList[i].color,
                                        fontFamily = FontFamily(Font(R.font.icons)),
                                    )
                                }
                            )
                        }


                    }
                }
            )

        }
    )
}