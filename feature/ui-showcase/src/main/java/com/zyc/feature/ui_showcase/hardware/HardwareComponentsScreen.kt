package com.zyc.feature.ui_showcase.hardware

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.feature.ui_showcase.components.ComponentSection
import com.zyc.feature.ui_showcase.components.ComponentDemo

/**
 * 硬件接口组件展示页面
 * 展示各种硬件设备交互相关的组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HardwareComponentsScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ZAppBar(
            title = "硬件接口组件",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 相机组件
            ComponentSection(
                title = "相机组件",
                description = "用于相机拍照和录像功能的组件"
            ) {
                CameraComponentDemo()
            }

            // 传感器组件
            ComponentSection(
                title = "传感器组件",
                description = "重力感应、陀螺仪、加速度计等传感器组件"
            ) {
                SensorComponentDemo()
            }

            // 蓝牙组件
            ComponentSection(
                title = "蓝牙组件",
                description = "蓝牙设备连接和通信组件"
            ) {
                BluetoothComponentDemo()
            }

            // GPS定位组件
            ComponentSection(
                title = "GPS定位组件",
                description = "位置获取和地图显示组件"
            ) {
                LocationComponentDemo()
            }

            // NFC组件
            ComponentSection(
                title = "NFC组件",
                description = "近场通信功能组件"
            ) {
                NFCComponentDemo()
            }

            // 指纹识别组件
            ComponentSection(
                title = "指纹识别组件",
                description = "生物识别认证组件"
            ) {
                BiometricComponentDemo()
            }

            // 振动反馈组件
            ComponentSection(
                title = "振动反馈组件",
                description = "触觉反馈和振动控制组件"
            ) {
                VibrationComponentDemo()
            }

            // 音频组件
            ComponentSection(
                title = "音频组件",
                description = "录音和播放功能组件"
            ) {
                AudioComponentDemo()
            }
        }
    }
}

@Composable
fun CameraComponentDemo() {
    var isCameraOpen by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "相机",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "相机控制",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "状态: ${if (isCameraOpen) "已开启" else "已关闭"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isCameraOpen = !isCameraOpen }
                ) {
                    Text(if (isCameraOpen) "关闭相机" else "打开相机")
                }

                OutlinedButton(
                    onClick = { /* 拍照逻辑 */ }
                ) {
                    Text("拍照")
                }

                OutlinedButton(
                    onClick = { /* 录像逻辑 */ }
                ) {
                    Text("录像")
                }
            }
        }
    }
}

@Composable
fun SensorComponentDemo() {
    var sensorData by remember { mutableStateOf("等待传感器数据...") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "传感器",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "传感器数据",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = sensorData,
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        sensorData = "加速度: X=0.1, Y=0.2, Z=9.8"
                    }
                ) {
                    Text("读取加速度")
                }

                OutlinedButton(
                    onClick = {
                        sensorData = "陀螺仪: X=0.01, Y=0.02, Z=0.03"
                    }
                ) {
                    Text("读取陀螺仪")
                }
            }
        }
    }
}

@Composable
fun BluetoothComponentDemo() {
    var bluetoothStatus by remember { mutableStateOf("未连接") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "蓝牙",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "蓝牙连接",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "状态: $bluetoothStatus",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        bluetoothStatus = "正在扫描设备..."
                    }
                ) {
                    Text("扫描设备")
                }

                OutlinedButton(
                    onClick = {
                        bluetoothStatus = "已连接到设备"
                    }
                ) {
                    Text("连接")
                }
            }
        }
    }
}

@Composable
fun LocationComponentDemo() {
    var locationInfo by remember { mutableStateOf("位置未获取") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "定位",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "GPS定位",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = locationInfo,
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        locationInfo = "纬度: 39.9042, 经度: 116.4074"
                    }
                ) {
                    Text("获取位置")
                }

                OutlinedButton(
                    onClick = {
                        locationInfo = "正在打开地图..."
                    }
                ) {
                    Text("打开地图")
                }
            }
        }
    }
}

@Composable
fun NFCComponentDemo() {
    var nfcStatus by remember { mutableStateOf("NFC未启用") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "NFC",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "NFC通信",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "状态: $nfcStatus",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        nfcStatus = "NFC已启用，等待标签..."
                    }
                ) {
                    Text("启用NFC")
                }

                OutlinedButton(
                    onClick = {
                        nfcStatus = "检测到NFC标签"
                    }
                ) {
                    Text("读取标签")
                }
            }
        }
    }
}

@Composable
fun BiometricComponentDemo() {
    var authStatus by remember { mutableStateOf("未认证") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "指纹",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "生物识别",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "状态: $authStatus",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        authStatus = "请验证指纹..."
                    }
                ) {
                    Text("指纹认证")
                }

                OutlinedButton(
                    onClick = {
                        authStatus = "认证成功"
                    }
                ) {
                    Text("面部识别")
                }
            }
        }
    }
}

@Composable
fun VibrationComponentDemo() {
    var vibrationStatus by remember { mutableStateOf("振动已停止") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "振动",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "振动反馈",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "状态: $vibrationStatus",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        vibrationStatus = "短振动"
                    }
                ) {
                    Text("短振动")
                }

                OutlinedButton(
                    onClick = {
                        vibrationStatus = "长振动"
                    }
                ) {
                    Text("长振动")
                }

                OutlinedButton(
                    onClick = {
                        vibrationStatus = "自定义振动模式"
                    }
                ) {
                    Text("模式振动")
                }
            }
        }
    }
}

@Composable
fun AudioComponentDemo() {
    var audioStatus by remember { mutableStateOf("音频已停止") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "音频",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "音频控制",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "状态: $audioStatus",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        audioStatus = "正在录音..."
                    }
                ) {
                    Text("开始录音")
                }

                OutlinedButton(
                    onClick = {
                        audioStatus = "正在播放..."
                    }
                ) {
                    Text("播放音频")
                }

                OutlinedButton(
                    onClick = {
                        audioStatus = "音频已停止"
                    }
                ) {
                    Text("停止")
                }
            }
        }
    }
}
