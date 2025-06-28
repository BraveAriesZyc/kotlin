package com.zyc.clover.utils.event

import android.view.ViewTreeObserver
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 自定义 Modifier，用于监听软键盘的显示和隐藏状态。
 *
 * 这个 Modifier 通过监听视图树的全局布局变化，检测软键盘的状态变化，
 * 并通过回调函数通知调用者。
 *
 * 使用示例：
 * ```
 * var isKeyboardOpen by remember { mutableStateOf(false) }
 *
 * Box(
 *     modifier = Modifier
 *         .fillMaxSize()
 *         .keyboardStateListener { isOpen ->
 *             isKeyboardOpen = isOpen
 *         }
 * ) {
 *     // 内容
 * }
 * ```
 *
 * @param onKeyboardStateChanged 当软键盘状态发生变化时调用的回调函数，
 * 参数为 true 表示键盘显示，false 表示键盘隐藏。
 */
fun Modifier.keyboardStateListener(onKeyboardStateChanged: (Boolean) -> Unit): Modifier {
    return composed {
        // 获取当前 Compose 视图
        val view = LocalView.current

        // 创建一次性效果，在视图创建时设置监听器，在视图销毁时移除监听器
        DisposableEffect(view) {
            // 获取视图树观察者，用于监听布局变化
            val viewTreeObserver = view.viewTreeObserver

            // 创建全局布局监听器，当布局发生变化时检测键盘状态
            val listener = ViewTreeObserver.OnGlobalLayoutListener {
                // 获取窗口插入信息，包含输入法(IME)窗口的可见性
                val insets = ViewCompat.getRootWindowInsets(view)

                // 检查输入法窗口是否可见
                // WindowInsetsCompat.Type.ime() 表示输入法窗口
                val isKeyboardVisible = insets?.isVisible(WindowInsetsCompat.Type.ime()) ?: false

                // 通过回调函数通知键盘状态变化
                onKeyboardStateChanged(isKeyboardVisible)
            }

            // 注册全局布局监听器
            viewTreeObserver.addOnGlobalLayoutListener(listener)

            // 定义清理操作，当视图销毁时执行
            onDispose {
                // 移除全局布局监听器，防止内存泄漏
                viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
        }

        // 返回原始 Modifier，确保链式调用的连续性
        this
    }
}

