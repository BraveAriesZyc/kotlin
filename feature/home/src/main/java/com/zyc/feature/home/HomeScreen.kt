package com.zyc.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 首页屏幕 - 空白页面
 */
@Composable
fun HomeScreen(
    openDrawer: () -> Unit
) {
    Scaffold(
        content = { pd ->
            Box(
                modifier = Modifier.fillMaxSize().padding(top = pd.calculateTopPadding()),
                content = {
                    Column() {
                        Row {
                            IconButton(
                                onClick = {
                                    openDrawer()
                                },
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "menu",
                                    )
                                },
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {
                                Text(
                                    text = "首页",
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "页面开发中...",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                }
            )
        }
    )
}
