package com.zyc.clover.pages.layout.children.me

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.zyc.clover.components.ZAppBar

import com.zyc.clover.route.LocalNavController
import com.zyc.clover.ui.theme.LocalTheme


import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeScreen(toggleDrawer: () -> Unit) {
    val navController = LocalNavController.current
    val meViewModel = viewModel<MeViewModel>()

    val themeModel = LocalTheme.current
    val themeMap by themeModel.themeMap.collectAsState()
//    val currentTheme by themeMap.currentTheme.collectAsState()

    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ZAppBar(
                title = "我的",
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                toggleDrawer()
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

                    Column {
                        Text(text = "主题选择")
                        Text(text = "${themeMap.size}")
                        // 添加垂直间距
                        themeMap.forEach { entry ->
                            Button(
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    themeModel.updateTheme(entry.key)
                                }
                            ) {
                                Text(text = entry.key.name)
                            }
                        }
                    }


                    Text(modifier = Modifier.padding(pd), text = "我的")
                }
            )

        }
    )

}
