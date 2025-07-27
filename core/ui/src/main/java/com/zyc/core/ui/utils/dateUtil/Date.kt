package com.zyc.core.ui.utils.dateUtil

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtil {

    // 常用日期格式
    const val FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
    const val FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_MM_DD = "MM-dd"
    const val FORMAT_HH_MM = "HH:mm"
    const val FORMAT_MM_DD_HH_MM = "MM-dd HH:mm"

    /**
     * 获取当前时间戳（毫秒）
     */
    fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

    /**
     * 获取当前日期
     */
    fun getCurrentDate(): Date = Date()

    /**
     * 格式化日期
     * @param date 日期
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    fun formatDate(date: Date, pattern: String = FORMAT_YYYY_MM_DD_HH_MM_SS): String {
        return try {
            SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 格式化时间戳
     * @param timestamp 时间戳（毫秒）
     * @param pattern 格式模式
     * @return 格式化后的字符串
     */
    fun formatTimestamp(timestamp: Long, pattern: String = FORMAT_YYYY_MM_DD_HH_MM_SS): String {
        return formatDate(Date(timestamp), pattern)
    }

    /**
     * 解析日期字符串
     * @param dateString 日期字符串
     * @param pattern 格式模式
     * @return Date对象，解析失败返回null
     */
    fun parseDate(dateString: String, pattern: String = FORMAT_YYYY_MM_DD_HH_MM_SS): Date? {
        return try {
            SimpleDateFormat(pattern, Locale.getDefault()).parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取友好的时间显示
     * @param timestamp 时间戳（毫秒）
     * @return 友好时间字符串（如：刚刚、5分钟前、今天 14:30、昨天 14:30、2023-12-01）
     */
    fun getFriendlyTime(timestamp: Long): String {
        val now = getCurrentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "刚刚"
            diff < TimeUnit.HOURS.toMillis(1) -> "${diff / TimeUnit.MINUTES.toMillis(1)}分钟前"
            diff < TimeUnit.DAYS.toMillis(1) -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timestamp
                val today = Calendar.getInstance()
                if (isSameDay(calendar, today)) {
                    "今天 ${formatTimestamp(timestamp, FORMAT_HH_MM)}"
                } else {
                    "${diff / TimeUnit.HOURS.toMillis(1)}小时前"
                }
            }
            diff < TimeUnit.DAYS.toMillis(2) -> "昨天 ${formatTimestamp(timestamp, FORMAT_HH_MM)}"
            diff < TimeUnit.DAYS.toMillis(7) -> "${diff / TimeUnit.DAYS.toMillis(1)}天前"
            else -> formatTimestamp(timestamp, FORMAT_YYYY_MM_DD)
        }
    }

    /**
     * 判断两个日期是否是同一天
     */
    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * 获取今天的开始时间（00:00:00）
     */
    fun getTodayStart(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    /**
     * 获取今天的结束时间（23:59:59）
     */
    fun getTodayEnd(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }

    /**
     * 计算两个日期之间的天数差
     */
    fun getDaysBetween(startDate: Date, endDate: Date): Long {
        val diffInMillis = endDate.time - startDate.time
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加天数
     */
    fun addDays(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return calendar.time
    }

    /**
     * 添加小时
     */
    fun addHours(date: Date, hours: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR_OF_DAY, hours)
        return calendar.time
    }

    /**
     * 添加分钟
     */
    fun addMinutes(date: Date, minutes: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.time
    }

    /**
     * 判断是否是今天
     */
    fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        val target = Calendar.getInstance()
        target.time = date
        return isSameDay(today, target)
    }

    /**
     * 判断是否是昨天
     */
    fun isYesterday(date: Date): Boolean {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_MONTH, -1)
        val target = Calendar.getInstance()
        target.time = date
        return isSameDay(yesterday, target)
    }

    /**
     * 获取年龄
     */
    fun getAge(birthDate: Date): Int {
        val birth = Calendar.getInstance()
        birth.time = birthDate
        val now = Calendar.getInstance()

        var age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)

        if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }
}
