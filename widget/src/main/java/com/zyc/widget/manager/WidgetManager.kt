package com.zyc.widget.manager


import android.content.Context
import com.zyc.widget.WidgetUpdateWorker

/**
 * 小组件管理器
 * 提供小组件的初始化和更新功能
 */
object WidgetManager {

    /**
     * 初始化小组件服务
     * 应在应用启动时调用
     */
    fun initialize(context: Context) {
        // 启动定期更新任务
        WidgetUpdateWorker.startPeriodicUpdate(context)
    }

    /**
     * 清理小组件服务
     * 应在应用关闭时调用
     */
    fun cleanup(context: Context) {
        WidgetUpdateWorker.stopPeriodicUpdate(context)
    }
}