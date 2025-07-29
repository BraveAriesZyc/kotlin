package com.zyc.core.ui.utils.sysHardwareUtil

import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager

/**
 * 触发短震动
 */
fun Context.vibrateShort() {
    vibrate(15)
}

/**
 * 触发长震动
 */
fun Context.vibrateLong() {
    vibrate(400)
}

private fun Context.vibrate(milliseconds: Long) {
    val vibrator = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    vibrator.defaultVibrator.vibrate(
        VibrationEffect.createOneShot(
            milliseconds,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
    )
}
