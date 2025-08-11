package com.zyc.core.permission.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
data object  NotificationUtil {

    @SuppressLint("MissingPermission")
     fun sendNotification(
        context: Context,
        channelId: String,
        channelName: String,
        title: String,
        content: String,
        icon: Int = android.R.drawable.ic_dialog_info
    ) {
         createNotificationChannel(context, channelId, channelName)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon) // 设置通知小图标
            .setContentTitle(title) // 设置通知标题
            .setContentText(content) // 设置通知内容
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(context).apply {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "测试通道"
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}