package com.zyc.widget.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.zyc.widget.WidgetManager
import com.zyc.widget.ui.LoveWidget

class LoveReceiver  : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LoveWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("刷新排查", "onEnabled：小组件被添加到桌面，准备启动更新任务")
        WidgetManager.initialize(context)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d("刷新排查", "onUpdate：系统触发小组件更新")
    }
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d("刷新排查", "onDisabled：小组件被从桌面删除，准备停止更新任务")
        WidgetManager.cleanup(context)
    }
}