package com.zyc.widget.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent

import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import com.zyc.widget.R
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
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
                                    Text(text = "恋爱记")
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
                                            Text(text = "天数: ")
                                            Text(text = getDaysFromMarch1st())
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

    // 获取从3月1日到今天的天数
    private fun getDaysFromMarch1st(): String {
        val today = LocalDate.now()
        // 确定年份：如果当前月份在3月之前，使用上一年
        val year = if (today.monthValue < 3) today.year - 1 else today.year
        val march1st = LocalDate.of(year, 3, 1)

        // 计算天数差并加1，包含首尾日期
        val days = ChronoUnit.DAYS.between(march1st, today) + 1
        return "$days 天"
    }

    // 获取带秒数的当前时间
    private fun getCurrentTimeWithSeconds(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm", Locale.CHINA)
        return "${now.format(formatter)}"
    }
}