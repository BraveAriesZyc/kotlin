package com.zyc.clover



import android.os.Bundle

import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.zyc.clover.route.NavigationRouterScreen
import com.zyc.clover.ui.theme.CloverAppTheme
import com.zyc.clover.utils.event.GlobalAntiShake

/**
 * 应用主Activity，负责初始化应用环境和管理导航
 * 使用 Jetpack Compose 构建UI，并实现双击返回键退出功能
 */
class MainActivity : ComponentActivity() {
    // 上次点击返回键的时间戳
    private var lastBackPressedTime: Long = 0

    // 双击退出的时间间隔（毫秒）
    private val exitInterval: Long = 2000 // 2秒内双击有效

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 获取 WindowInsetsController 对象，用于控制系统栏的外观
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // 设置导航栏图标为浅色（黑色图标，适用于浅色背景）
        windowInsetsController.isAppearanceLightNavigationBars = true

        // 设置窗口标志，允许内容延伸到系统栏区域（状态栏和导航栏）
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        // 启用边缘到边缘显示（沉浸式状态栏）
        enableEdgeToEdge()

        // 设置Compose内容
        setContent {
            // 配置全局防抖策略（防止重复点击）
            GlobalAntiShake.SetupGlobalConfig(
                GlobalAntiShake.Config(
                    defaultDelay = 500L,
                    enableGlobalCheck = true
                )
            )

            // 应用主题包装
            CloverAppTheme {
                    // 导航入口组件，管理应用内的页面跳转
                    NavigationRouterScreen()
                }
            }


        // 注册返回键回调，实现双击退出功能
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 触发返回键确认逻辑
                showExitConfirmation()
            }
        })
    }

    /**
     * 处理返回键确认逻辑，实现双击退出功能
     * 1. 首次点击：显示提示信息
     * 2. 二次点击（间隔<2秒）：退出应用
     */
    private fun showExitConfirmation() {
        // 获取当前时间戳
        val currentTime = System.currentTimeMillis()

        // 判断是否在有效时间间隔内连续点击
        if (currentTime - lastBackPressedTime < exitInterval) {
            // 间隔小于2秒，允许退出应用
            finish() // 直接关闭Activity
        } else {
            // 首次点击或间隔超过2秒，记录当前时间并显示提示
            lastBackPressedTime = currentTime
            Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show()
        }
    }
}
