package com.zyc.feature.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.zyc.core.router.LocalNavController
import com.zyc.core.ui.components.common.CreateBackIcon
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.theme.LocalTheme
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick
import kotlinx.coroutines.launch

/**
 * 个人资料屏幕
 */
@Composable
fun ProfileScreen(
    openDrawer: () -> Unit,
) {
    val navController = LocalNavController.current
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
                    Text(text = "个人资料")
                }
            )

        }
    )
}
