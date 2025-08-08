package com.zyc.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.work.*
import com.zyc.widget.ui.LoveWidget
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

/**
 * 小组件更新工作器
 * 定期更新小组件数据
 */
class WidgetUpdateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // 记录更新时间和日志
            val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            Log.d("小组件更新", "正在更新小组件，当前时间：$currentTime")

            // 更新小组件
            LoveWidget().updateAll(applicationContext)

            Log.d("小组件更新", "更新成功")
            Result.success()
        } catch (e: Exception) {
            Log.e("小组件更新", "更新失败：${e.message}", e)
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "widget_update_work"

        fun startOneSecondUpdate(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
                1, TimeUnit.SECONDS
            ).build()
            Log.d("小组件更新", "更新成功")
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }

        fun stopUpdate(context: Context) {
            Log.d("小组件更新", "停止更新任务")
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}