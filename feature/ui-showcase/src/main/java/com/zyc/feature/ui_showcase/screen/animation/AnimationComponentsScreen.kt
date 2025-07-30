package com.zyc.feature.ui_showcase.screen.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.feature.ui_showcase.screen.components.ComponentDemo
import com.zyc.feature.ui_showcase.screen.components.ComponentSection

import kotlinx.coroutines.delay

@Composable
fun AnimationComponentsScreen(
    onBack: () -> Unit = {}
) {
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    // 动画状态
    var isAnimating by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableStateOf(0f) }
    var scaleValue by remember { mutableStateOf(1f) }
    var colorAnimation by remember { mutableStateOf(false) }

    // 动画定义
    val rotation by animateFloatAsState(
        targetValue = if (isAnimating) 360f else 0f,
        animationSpec = tween(durationMillis = 2000, easing = LinearEasing),
        label = "rotation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.5f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val animatedColor by animateColorAsState(
        targetValue = if (colorAnimation) Color.Red else Color.Blue,
        animationSpec = tween(durationMillis = 1000),
        label = "color"
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ZAppBar(
            title = "动画组件",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 高级动画组件部分
            ComponentSection(
                title = "高级动画效果",
                description = "复杂的动画组合和效果"
            ) {
                ComponentDemo(
                    title = "组合动画示例",
                    description = "多种动画效果的组合"
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 组合动画效果
                            val animatedScale by animateFloatAsState(
                                targetValue = if (isAnimating) 1.2f else 1.0f,
                                animationSpec = tween(durationMillis = 1000),
                                label = "scale"
                            )
                            val animatedRotation by animateFloatAsState(
                                targetValue = if (isAnimating) 360f else 0f,
                                animationSpec = tween(durationMillis = 2000),
                                label = "rotation"
                            )

                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .scale(animatedScale)
                                    .rotate(animatedRotation)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "组合\n动画",
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "组合动画用法：",
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "val scale by animateFloatAsState(...)\nval rotation by animateFloatAsState(...)\n\nBox(\n    modifier = Modifier\n        .scale(scale)\n        .rotate(rotation)\n)",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 基础动画组件部分
            ComponentSection(
                title = "基础动画效果",
                description = "Compose 内置的各种动画效果"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 旋转动画
                    ComponentDemo(
                        title = "旋转动画",
                        description = "使用 animateFloatAsState 实现旋转效果"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .rotate(rotation)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "旋转",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        isAnimating = !isAnimating
                                        snackbarMessage = if (isAnimating) "开始旋转动画" else "停止旋转动画"
                                        showSnackbar = true
                                    }
                                ) {
                                    Text(if (isAnimating) "停止旋转" else "开始旋转")
                                }
                            }
                        }
                    }

                    // 缩放动画
                    ComponentDemo(
                        title = "缩放动画",
                        description = "使用 Spring 动画实现弹性缩放效果"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .scale(scale)
                                        .background(
                                            color = MaterialTheme.colorScheme.secondary,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "缩放",
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        isAnimating = !isAnimating
                                        snackbarMessage = if (isAnimating) "开始缩放动画" else "停止缩放动画"
                                        showSnackbar = true
                                    }
                                ) {
                                    Text(if (isAnimating) "停止缩放" else "开始缩放")
                                }
                            }
                        }
                    }

                    // 颜色动画
                    ComponentDemo(
                        title = "颜色动画",
                        description = "使用 animateColorAsState 实现颜色渐变"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(
                                            color = animatedColor,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "颜色",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        colorAnimation = !colorAnimation
                                        snackbarMessage = "切换颜色动画"
                                        showSnackbar = true
                                    }
                                ) {
                                    Text("切换颜色")
                                }
                            }
                        }
                    }

                    // 组合动画
                    ComponentDemo(
                        title = "组合动画",
                        description = "多种动画效果的组合"
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .rotate(rotation)
                                        .scale(scale)
                                        .background(
                                            color = animatedColor,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "组合",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            isAnimating = !isAnimating
                                            colorAnimation = !colorAnimation
                                            snackbarMessage = "启动组合动画"
                                            showSnackbar = true
                                        }
                                    ) {
                                        Text("组合动画")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 进入退出动画
            ComponentSection(
                title = "进入退出动画",
                description = "组件的显示和隐藏动画效果"
            ) {
                var visible by remember { mutableStateOf(true) }

                ComponentDemo(
                    title = "淡入淡出动画",
                    description = "使用 AnimatedVisibility 实现淡入淡出"
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn(animationSpec = tween(1000)),
                                exit = fadeOut(animationSpec = tween(1000))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.tertiary,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "淡入\n淡出",
                                        color = MaterialTheme.colorScheme.onTertiary,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    visible = !visible
                                    snackbarMessage = if (visible) "显示组件" else "隐藏组件"
                                    showSnackbar = true
                                }
                            ) {
                                Text(if (visible) "隐藏" else "显示")
                            }
                        }
                    }
                }
            }

            // 动画控制
            ComponentSection(
                title = "动画控制",
                description = "控制所有动画的播放状态"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            isAnimating = true
                            colorAnimation = true
                            snackbarMessage = "启动所有动画"
                            showSnackbar = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("启动所有动画")
                    }

                    Button(
                        onClick = {
                            isAnimating = false
                            colorAnimation = false
                            snackbarMessage = "停止所有动画"
                            showSnackbar = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("停止所有动画")
                    }
                }
            }

            // 使用说明
            ComponentSection(
                title = "使用说明",
                description = "动画组件的基本使用方法和参数说明"
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "导入方式：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "import androidx.compose.animation.*\nimport androidx.compose.animation.core.*",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "主要动画类型：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• animateFloatAsState - 浮点数动画\n• animateColorAsState - 颜色动画\n• AnimatedVisibility - 显示隐藏动画\n• Spring 动画 - 弹性动画效果\n• Transition 动画 - 状态转换动画",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "使用建议：",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• 合理使用动画提升用户体验\n• 避免过度动画影响性能\n• 根据场景选择合适的动画类型\n• 注意动画的时长和缓动效果",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    // Snackbar 显示
    if (showSnackbar) {
        LaunchedEffect(snackbarMessage) {
            delay(2000)
            showSnackbar = false
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(
                        onClick = { showSnackbar = false }
                    ) {
                        Text("关闭")
                    }
                }
            ) {
                Text(snackbarMessage)
            }
        }
    }
}
