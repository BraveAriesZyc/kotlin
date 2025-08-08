package com.zyc.widget.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import androidx.compose.ui.graphics.Color as ComposeColor

class LoveWidget : GlanceAppWidget() {
    @SuppressLint("RestrictedApi")
    override suspend fun provideGlance(context: Context, id: GlanceId) {


        val timeInfo = getDaysFromMarch1st()
        val currentTime = getCurrentTimeWithSeconds()
        Log.d("刷新排查", "provideGlance：开始刷新UI内容")

        Log.d("刷新排查", "provideGlance：最新时间=$currentTime")
        provideContent {

            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .cornerRadius(8.dp)
                    .padding(8.dp)
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                content = {
                    Column(
                        modifier = GlanceModifier.fillMaxSize(),
                        content = {
                            Row(
                                modifier = GlanceModifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                content = {
                                    Text(
                                        "恋爱记",
                                        style = TextStyle(color = ColorProvider(ComposeColor.Red.copy(alpha = 0.5f)))
                                    )
                                }
                            )
                            Spacer(modifier = GlanceModifier.height(4.dp))
                            Text(
                                timeInfo,
                                style = TextStyle(
                                    color = ColorProvider(ComposeColor.White),
                                    fontSize = 12.sp
                                )
                            )
                            Spacer(modifier = GlanceModifier.height(4.dp))
                            Text(
                                currentTime,
                                style = TextStyle(
                                    color = ColorProvider(ComposeColor.White),
                                    fontSize = 12.sp
                                )
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
        return "从3月1日到今天已有$days 天"
    }

    // 获取带秒数的当前时间
    private fun getCurrentTimeWithSeconds(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 EEEE HH:mm:ss", Locale.CHINA)
        return "当前时间：${now.format(formatter)}"
    }
}
