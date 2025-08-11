package com.zyc.widget


import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.zyc.widget.ui.LoveWidget
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
            Log.d("小组件定时更新", "开始更新小组件数据")
            // 更新今日课程小组件
            LoveWidget().updateAll(applicationContext)
            Result.success()
        } catch (e: Exception) {
             Log.e("小组件定时更新", "Failed to update widgets", e)
            Result.retry()
        }
    }


    companion object {
        private const val WORK_NAME = "widget_update_work"

        /**
         * 启动定期更新任务
         */
        fun startPeriodicUpdate(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
                30, TimeUnit.MINUTES // 每30分钟更新一次
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }

        /**
         * 停止定期更新任务
         */
        fun stopPeriodicUpdate(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}