package com.zyc.widget.provider;

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.zyc.widget.manager.WidgetManager
import com.zyc.widget.ui.LoveWidget


class LoveProvider : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LoveWidget()

    override fun onEnabled(context: Context) {
        WidgetManager.initialize(context)
    }

    override fun onDisabled(context: Context) {
        WidgetManager.cleanup(context)
    }
}
