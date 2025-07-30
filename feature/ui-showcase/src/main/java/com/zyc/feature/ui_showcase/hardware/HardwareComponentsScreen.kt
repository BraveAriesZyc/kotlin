package com.zyc.feature.ui_showcase.hardware

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView
import com.zyc.feature.ui_showcase.components.ComponentSection
import com.zyc.feature.ui_showcase.hardware.audio.AudioComponentDemo
import com.zyc.feature.ui_showcase.hardware.biometric.BiometricComponentDemo
import com.zyc.feature.ui_showcase.hardware.bluetooth.BluetoothComponentDemo
import com.zyc.feature.ui_showcase.hardware.camera.CameraComponentDemo
import com.zyc.feature.ui_showcase.hardware.location.LocationComponentDemo
import com.zyc.feature.ui_showcase.hardware.network.NetworkComponentDemo
import com.zyc.feature.ui_showcase.hardware.nfc.NFCComponentDemo
import com.zyc.feature.ui_showcase.hardware.sensor.SensorComponentDemo
import com.zyc.feature.ui_showcase.hardware.vibration.VibrationComponentDemo

/**
 * 硬件接口组件展示页面
 * 展示各种硬件设备交互相关的组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HardwareComponentsScreen(
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ZAppBar(
            title = "硬件接口组件",
            onBack = onBack
        )

        BounceListView(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                // 相机组件
                ComponentSection(
                    title = "相机组件",
                    description = "用于相机拍照和录像功能的组件",
                    content = {
                        CameraComponentDemo()
                    }
                )
            }
            
            item {
                // 传感器组件
                ComponentSection(
                    title = "传感器组件",
                    description = "重力感应、陀螺仪、加速度计等传感器组件",
                    content = {
                        SensorComponentDemo()
                    }
                )
            }

            item {
                // 蓝牙组件
                ComponentSection(
                    title = "蓝牙组件",
                    description = "蓝牙设备连接和通信组件",
                    content = {
                        BluetoothComponentDemo()
                    }
                )
            }

            item {
                // GPS定位组件
                ComponentSection(
                    title = "GPS定位组件",
                    description = "位置获取和地图显示组件",
                    content = {
                        LocationComponentDemo()
                    }
                )
            }
            
            item {
                // NFC组件
                ComponentSection(
                    title = "NFC组件",
                    description = "近场通信功能组件",
                    content = {
                        NFCComponentDemo()
                    }
                )
            }
            
            item {
                // 指纹识别组件
                ComponentSection(
                    title = "指纹识别组件",
                    description = "生物识别认证组件",
                    content = {
                        BiometricComponentDemo()
                    }
                )
            }
            
            item {
                // 振动反馈组件
                ComponentSection(
                    title = "振动反馈组件",
                    description = "触觉反馈和振动控制组件",
                    content = {
                        VibrationComponentDemo()
                    }
                )
            }

            item {
                // 音频组件
                ComponentSection(
                    title = "音频组件",
                    description = "录音和播放功能组件",
                    content = {
                        AudioComponentDemo()
                    }
                )
            }
            
            item {
                // 网络组件
                ComponentSection(
                    title = "网络组件",
                    description = "网络连接状态和功能组件",
                    content = {
                        NetworkComponentDemo()
                    }
                )
            }
        }
    }
}
