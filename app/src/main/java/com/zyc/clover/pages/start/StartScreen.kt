package com.zyc.clover.pages.start

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import com.zyc.clover.components.loading.Loading
import com.zyc.clover.route.LayoutRoute
import com.zyc.clover.route.LocalNavController
import com.zyc.clover.route.StartRoute

import com.zyc.clover.utils.event.GlobalAntiShake
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable


@Composable
fun StartScreen() {
    val navController = LocalNavController.current
    // 使用状态管理倒计时
    val countdownSeconds = remember { mutableIntStateOf(3) }

    // 控制Logo和应用名称的淡入动画
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    // 自动跳转逻辑
    LaunchedEffect(Unit) {
        startAnimation = true // 开始动画
        // 倒计时逻辑
        repeat(countdownSeconds.intValue) {
            delay(1000L)
            if (countdownSeconds.intValue > 0) {
                countdownSeconds.intValue--
            }
        }
        GlobalAntiShake.runWithDebounce {
            // 倒计时结束后跳转
            navController.navigate(LayoutRoute) {
                popUpTo(StartRoute) { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { pd ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(pd)
        ) {
            // 应用Logo和名称
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Loading.AnimatedBallLoader()
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 倒计时跳过按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    content = {

                        Button(
                            shape = RoundedCornerShape(4.dp),
                            onClick = {
                                GlobalAntiShake.runWithDebounce {
                                    navController.navigate(LayoutRoute) {
                                        popUpTo(StartRoute) { inclusive = true }
                                    }
                                }
                            },
                            modifier = Modifier
                                .wrapContentSize()
                                .height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black.copy(alpha = 0.2f),
                                contentColor = Color.White
                            ),
                            content = {
                                Text(
                                    text = "跳过 ${countdownSeconds.intValue}s",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        )
                    }
                )

                // 底部版权信息
                Text(
                    text = "© 2025 Clover 版权所有",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .alpha(alphaAnim)
                )
            }


        }
    }
}
