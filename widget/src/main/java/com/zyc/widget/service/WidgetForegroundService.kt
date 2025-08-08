package com.zyc.widget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.glance.appwidget.updateAll
import com.zyc.core.ui.R
import com.zyc.widget.ui.LoveWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 小组件前台服务
 * 负责每秒刷新一次小组件，突破系统后台限制
 */
class WidgetForegroundService : Service() {
    // 协程作用域，用于执行定时任务
    private val serviceScope = CoroutineScope(Dispatchers.Main)

    // 通知渠道ID（必须唯一）
    private val CHANNEL_ID = "widget_foreground_channel"
    // 通知ID（必须唯一）
    private val NOTIFICATION_ID = 10086

    override fun onBind(intent: Intent?): IBinder? {
        // 前台服务通常不需要绑定，返回null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("刷新排查", "前台服务：已启动，准备初始化")

        // 1. 创建通知渠道（Android 8.0+ 必需）
        createNotificationChannel()

        // 2. 创建前台通知（必须显示，否则服务会被杀死）
        val notification = buildNotification()

        // 3. 启动前台服务（核心步骤！）
        startForeground(NOTIFICATION_ID, notification)
        Log.d("刷新排查", "前台服务：已进入前台状态，通知已显示")

        // 4. 启动秒级刷新循环
        startRefreshLoop()

        // 5. 服务被杀死后自动重启
        return START_STICKY
    }

    /**
     * 创建通知渠道（Android 8.0+ 必需）
     * 否则通知无法显示，服务会被立即终止
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "小组件刷新服务", // 渠道名称（用户可见）
                NotificationManager.IMPORTANCE_LOW // 低重要性，不打扰用户
            ).apply {
                description = "保持恋爱记小组件实时更新" // 渠道描述
                setSound(null, null) // 关闭通知声音
                enableVibration(false) // 关闭震动
                setShowBadge(false) // 不在应用图标上显示角标
            }

            // 注册渠道到系统
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d("刷新排查", "前台服务：通知渠道创建成功")
        }
    }

    /**
     * 构建前台通知
     * 必须包含标题和小图标，否则会报错
     */
    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("恋爱记小组件") // 通知标题
            .setContentText("正在实时更新中...") // 通知内容
            .setSmallIcon(R.drawable.my) // 必须设置（替换为你的图标）
            .setPriority(NotificationCompat.PRIORITY_LOW) // 低优先级，不弹窗
            .setOngoing(true) // 设为持续通知（用户不能手动删除）
            .setSilent(true) // 静音通知
            .build()
    }

    /**
     * 启动秒级刷新循环
     * 每秒调用一次小组件的更新方法
     */
    private fun startRefreshLoop() {
        serviceScope.launch {
            Log.d("刷新排查", "前台服务：刷新循环已启动，开始每秒刷新")

            while (true) { // 无限循环，持续刷新
                try {
                    // 强制刷新所有LoveWidget实例
                    LoveWidget().updateAll(applicationContext)

                    // 打印当前时间，验证刷新频率
                    val currentTime = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"))
                    Log.d("刷新排查", "前台服务：刷新成功，当前时间=$currentTime")
                } catch (e: Exception) {
                    Log.e("刷新排查", "前台服务：刷新失败，原因=${e.message}", e)
                }

                // 等待1秒后再次刷新
                delay(1000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("刷新排查", "前台服务：已停止，清理资源")
        // 取消所有协程任务
        serviceScope.coroutineContext.cancel()
    }
}
