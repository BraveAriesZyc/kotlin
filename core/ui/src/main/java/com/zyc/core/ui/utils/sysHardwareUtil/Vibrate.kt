package com.zyc.core.ui.utils.sysHardwareUtil

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

/**
 * 触发短震动
 */
fun Context.vibrateShort() {
    vibrate(200)
}

/**
 * 触发长震动
 */
fun Context.vibrateLong() {
    vibrate(400)
}

/**
 * 触发模式震动 - 短-长-短的模式
 */
fun Context.vibratePattern() {
    try {
        val vibrator = getVibrator()
        if (vibrator?.hasVibrator() == true) {
            // 模式：等待100ms，震动100ms，等待50ms，震动200ms，等待50ms，震动100ms
            val pattern = longArrayOf(100, 100, 50, 200, 50, 100)
            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    pattern,
                    -1 // 不重复
                )
            )
        }
    } catch (e: Exception) {
        Log.e("vibrate", "vibrate error${e}")
        e.printStackTrace()
    }
}

private fun Context.vibrate(milliseconds: Long) {
    try {
        val vibrator = getVibrator()
        if (vibrator?.hasVibrator() == true) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    milliseconds,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
    } catch (e: Exception) {
        Log.e("vibrate", "vibrate error${e}")
        e.printStackTrace()
    }
}

/**
 * 获取振动器实例，兼容不同Android版本
 */
private fun Context.getVibrator(): Vibrator? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ 使用 VibratorManager
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            // Android 12以下使用传统方式
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    } catch (e: Exception) {
        Log.e("vibrate", "vibrate error${e}")
        e.printStackTrace()
        null
    }
}
