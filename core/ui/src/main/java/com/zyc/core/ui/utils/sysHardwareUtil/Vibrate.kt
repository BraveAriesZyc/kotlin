package com.zyc.core.ui.utils.sysHardwareUtil

import android.content.Context
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

/**
 * 系统震动工具类
 * 提供多种震动模式，兼容不同Android版本
 */
object VibrationUtils {
    private const val TAG = "VibrationUtils"

    /**
     * 触发短震动 (200ms)
     * @param amplitude 震动强度 (1-255，仅API 26+支持)
     */
    fun vibrateShort(context: Context, amplitude: Int = VibrationEffect.DEFAULT_AMPLITUDE) {
        vibrate(context, 200, amplitude)
    }

    /**
     * 触发长震动 (400ms)
     * @param amplitude 震动强度 (1-255，仅API 26+支持)
     */
    fun vibrateLong(context: Context, amplitude: Int = VibrationEffect.DEFAULT_AMPLITUDE) {
        vibrate(context, 400, amplitude)
    }

    /**
     * 触发模式震动 - 短-长-短的模式
     */
    fun vibratePattern(context: Context) {
        try {
            val vibrator = getVibrator(context)
            if (vibrator?.hasVibrator() == true) {
                // 模式：等待100ms，震动100ms，等待50ms，震动200ms，等待50ms，震动100ms
                val pattern = longArrayOf(100, 100, 50, 200, 50, 100)

                // 震动强度数组，与模式对应（0表示等待阶段）
                val intensities = intArrayOf(0, 200, 0, 255, 0, 150)

                val vibrationEffect =
                    if (vibrator.hasAmplitudeControl()) {
                        VibrationEffect.createWaveform(pattern, intensities, -1)
                    } else {
                        VibrationEffect.createWaveform(pattern, -1)
                    }

                vibrator.vibrate(vibrationEffect, getVibrationAttributes())
                Log.d(TAG, "模式震动已触发")
            }
        } catch (e: Exception) {
            Log.e(TAG, "模式震动失败: ${e.message}", e)
        }
    }

    /**
     * 自定义时长震动
     * @param milliseconds 震动时长(毫秒)
     * @param amplitude 震动强度 (1-255，仅API 26+支持)
     */
    fun vibrate(context: Context, milliseconds: Long, amplitude: Int = VibrationEffect.DEFAULT_AMPLITUDE) {
        try {
            val vibrator = getVibrator(context)
            if (vibrator?.hasVibrator() == true) {
                val vibrationEffect =
                    if (vibrator.hasAmplitudeControl() && amplitude != VibrationEffect.DEFAULT_AMPLITUDE) {
                        VibrationEffect.createOneShot(milliseconds, amplitude)
                    } else {
                        VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
                    }

                vibrator.vibrate(
                    vibrationEffect,
                    getVibrationAttributes()
                )
                Log.d(TAG, "震动已触发，时长: $milliseconds ms")
            }
        } catch (e: Exception) {
            Log.e(TAG, "震动失败: ${e.message}", e)
        }
    }

    /**
     * 获取振动器实例，兼容不同Android版本
     */
    private fun getVibrator(context: Context): Vibrator? {
        return try {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } catch (e: Exception) {
            Log.e(TAG, "获取振动器失败: ${e.message}", e)
            null
        }
    }

    private fun getVibrationAttributes(): VibrationAttributes {


        return VibrationAttributes.Builder()
            .setUsage(VibrationAttributes.USAGE_NOTIFICATION) // 震动用途（通知/游戏/触摸等）
            .build()
    }
}
