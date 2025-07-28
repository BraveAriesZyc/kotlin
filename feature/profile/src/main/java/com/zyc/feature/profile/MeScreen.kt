package com.zyc.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.core.common.utils.event.GlobalAntiShake.debounceClick

import com.zyc.core.ui.components.ZAppBar
import com.zyc.core.ui.components.drawer.DrawerViewModel
import com.zyc.core.ui.components.refreshview.BounceListView
import com.zyc.core.ui.route.LocalNavController

import com.zyc.core.ui.theme.LocalTheme


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
                    IconButton(
                        onClick = {
                            scope.launch {
                                openDrawer()
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
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
                                            .background(Color.White)
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
