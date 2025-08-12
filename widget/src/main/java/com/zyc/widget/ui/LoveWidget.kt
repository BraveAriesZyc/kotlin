package com.zyc.widget.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.zyc.widget.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class LoveWidget : GlanceAppWidget() {
    @SuppressLint("RestrictedApi", "ResourceType")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val intent = Intent().apply {
                component = ComponentName(
                    "com.zyc.clover",
                    "com.zyc.clover.MainActivity"
                )
            }

            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .clickable(
                        onClick = actionStartActivity(
                            intent
                        )
                    )
                    .cornerRadius(8.dp)
                    .background(ColorProvider(R.color.widget_color)),
                content = {
                    Column(
                        modifier = GlanceModifier.fillMaxSize().padding(8.dp),
                        content = {
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                content = {
                                    Text(text = "🍀☀🍂❄", style = TextStyle(
                                        fontSize = 16.sp
                                    ))
                                }
                            )
                            Spacer(modifier = GlanceModifier.height(5.dp))
                            Column(
                                modifier = GlanceModifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top,
                                horizontalAlignment = Alignment.Start,
                                content = {
                                    Row(
                                        modifier = GlanceModifier.fillMaxWidth(),
                                        content = {
                                            Text(text = "存活天数: ")
                                            Text(text = getDaysFrom2002March25thToToday())
                                        }
                                    )
                                    Spacer(modifier = GlanceModifier.height(5.dp))
                                    Row(
                                        modifier = GlanceModifier.fillMaxWidth(),
                                        content = {
                                            Text(text = "日期: ")
                                            Text(text = getCurrentTimeWithSeconds())
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            )

        }
    }

    private fun getDaysFrom2002March25thToToday(): String {
        // 固定起始日期：2002年3月25日
        val startDate = LocalDate.of(2002, 3, 25)
        // 获取系统当前日期（动态变化）
        val today = LocalDate.now()

        // 计算天数：两个日期之间的间隔天数 + 1（包含首尾两天）
        val days = ChronoUnit.DAYS.between(startDate, today) + 1

        // 返回结果（例如："8513 天"）
        return "$days 天"
    }
    // 获取带秒数的当前时间
    private fun getCurrentTimeWithSeconds(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm", Locale.CHINA)
        return "${now.format(formatter)}"
    }
}