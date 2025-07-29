package com.zyc.feature.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.core.ui.components.common.CreateBackIcon
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView

import com.zyc.core.ui.route.LocalNavController
import com.zyc.core.ui.theme.LocalTheme
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeScreen(
    openDrawer: () -> Unit,
) {
    val navController = LocalNavController.current
    val meViewModel = viewModel<MeViewModel>()
    val themeModel = LocalTheme.current
    val themeMap by themeModel.themeMap.collectAsState()
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ZAppBar(
                title = "我的",
                actions = {


                    Box(
                        modifier = Modifier.debounceClick {
                            scope.launch {
                                openDrawer()
                            }
                        },
                        content = {
                            CreateBackIcon("\uEBCF")
                        }
                    )
                }
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
                        themeMap.forEach { entry ->
                            item(
                                key = entry.key,
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()

                                            .padding(bottom = 8.dp)
                                            .clip(
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .background(MaterialTheme.colorScheme.surfaceBright)
                                            .padding(16.dp)
                                            .debounceClick {
                                                themeModel.updateTheme(entry.key)
                                            },
                                        content = {
                                            Text(text = entry.key.name, color = MaterialTheme.colorScheme.onSurface)
                                        }
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
