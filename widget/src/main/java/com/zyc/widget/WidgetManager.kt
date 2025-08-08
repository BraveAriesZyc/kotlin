package com.zyc.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

import android.util.Log

/**
 * 小组件管理器
 * 提供小组件的初始化和更新功能
 */
// WidgetManager.kt 中补充
object WidgetManager {
    fun initialize(context: Context) {
        Log.d("刷新排查", "WidgetManager：启动前台服务")
        // 直接在这里启动前台服务，避免依赖其他逻辑
        val intent = Intent(context, WidgetForegroundService::class.java)
        context.startForegroundService(intent)
    }

    @SuppressLint("ImplicitSamInstance")
    fun cleanup(context: Context) {
        Log.d("刷新排查", "WidgetManager：停止前台服务")
        context.stopService(Intent(context, WidgetForegroundService::class.java))
    }
}