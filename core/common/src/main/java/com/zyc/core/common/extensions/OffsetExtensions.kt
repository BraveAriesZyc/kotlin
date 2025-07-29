package com.zyc.core.common.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset

/**
 * Offset 扩展函数
 */

/**
 * 将 Offset 转换为 IntOffset
 */
fun Offset.toIntOffset() = IntOffset(x.toInt(), y.toInt())