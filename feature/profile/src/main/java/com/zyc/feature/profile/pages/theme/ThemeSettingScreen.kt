package com.zyc.feature.profile.pages.theme

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.components.common.IconBackground
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView
import com.zyc.core.ui.theme.LocalTheme
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick

@Composable
fun ThemeSettingScreen() {
    val themeModel = LocalTheme.current
    val themeList by themeModel.themeList.collectAsState()
    Scaffold(
        topBar = {
            ZAppBar(
                title = "主题设置",
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
                        items(themeList.size) { index ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .clip(
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(MaterialTheme.colorScheme.surfaceBright)
                                    .debounceClick {
                                        themeModel.updateTheme(index)
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                content = {
                                    IconBackground(
                                        icon = "\uEB7F",
                                        color = themeList[index].theme.error,
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    // 标题
                                    Text(
                                        text = themeList[index].title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
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