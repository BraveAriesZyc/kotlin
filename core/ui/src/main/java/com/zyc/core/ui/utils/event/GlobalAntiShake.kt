package com.zyc.core.ui.utils.event

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.zyc.core.common.AppConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object GlobalAntiShake {
    private val clickRecords = mutableMapOf<String, Long>()
    private var isHandlingEvent = false
    private var lastClickTime: Long = 0
    private val mainScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    /**
     * 事件配置类
     * @param key 事件唯一标识
     * @param delay 防抖延迟时间
     * @param enabled 是否启用
     * @param useGlobalCheck 是否使用全局检查
     */
    data class EventConfig(
        val key: String = "default",
        val delay: Long = AppConfig.DEFAULT_DELAY,
        val enabled: Boolean = true,
        val useGlobalCheck: Boolean = true
    )



    /**
     * 修饰符扩展防抖函数
     * @param config 事件配置
     * @param indication 点击指示器
     * @param interactionSource 交互源
     * @param onClick 点击回调
     */
    fun Modifier.debounceClick(
        config: EventConfig = EventConfig(),
        indication: Indication? = null,
        interactionSource: MutableInteractionSource? = null,
        onClick: () -> Unit
    ): Modifier = composed {
        var lastClickTime by remember { mutableStateOf(0L) }
        var isDebouncing by remember { mutableStateOf(false) }
        clickable(
            indication = indication,
            interactionSource = interactionSource,
            enabled = config.enabled && !isDebouncing
        ) {
            val currentTime = System.currentTimeMillis()
            
            if (AppConfig.ENABLE_LOGGING) {
                println("DebounceClick[${config.key}]: Attempting click at $currentTime")
            }

            if (canExecuteEvent(config, currentTime, lastClickTime)) {
                lastClickTime = currentTime
                clickRecords[config.key] = currentTime
                isDebouncing = true

                if (AppConfig.ENABLE_LOGGING) {
                    println("DebounceClick[${config.key}]: Executing click")
                }

                onClick()

                mainScope.launch {
                    delay(config.delay)
                    isDebouncing = false
                    if (config.useGlobalCheck) {
                        isHandlingEvent = false
                    }
                }
            } else if (AppConfig.ENABLE_LOGGING) {
                println("DebounceClick[${config.key}]: Click blocked by debounce")
            }
        }
    }

    /**
     * 检查事件是否可以执行
     */
    private fun canExecuteEvent(
        config: EventConfig,
        currentTime: Long,
        lastLocalClickTime: Long
    ): Boolean {
        // 检查本地防抖
        if (currentTime - lastLocalClickTime < config.delay) {
            return false
        }

        // 检查全局防抖
        if (config.useGlobalCheck && !canClickGlobally(currentTime)) {
            return false
        }

        // 检查键值防抖
        val lastKeyClickTime = clickRecords[config.key] ?: 0
        if (currentTime - lastKeyClickTime < config.delay) {
            return false
        }

        return true
    }

    private fun canClickGlobally(currentTime: Long): Boolean {
        if (isHandlingEvent || currentTime - lastClickTime < AppConfig.DEFAULT_DELAY) {
            return false
        }

        isHandlingEvent = true
        lastClickTime = currentTime
        return true
    }

    /**
     * 使用防抖执行代码块
     * @param config 事件配置
     * @param block 要执行的代码块
     * @return 是否成功执行
     */
    fun runWithDebounce(
        config: EventConfig = EventConfig(),
        block: () -> Unit
    ): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastTime = clickRecords[config.key] ?: 0
        if (AppConfig.ENABLE_LOGGING) {
            println("RunWithDebounce[${config.key}]: Attempting execution at $currentTime")
        }

        if (!canExecuteEvent(config, currentTime, lastTime)) {
            if (AppConfig.ENABLE_LOGGING) {
                println("RunWithDebounce[${config.key}]: Execution blocked by debounce")
            }
            return false
        }

        clickRecords[config.key] = currentTime
        
        if (AppConfig.ENABLE_LOGGING) {
            println("RunWithDebounce[${config.key}]: Executing block")
        }
        
        block()

        mainScope.launch {
            delay(config.delay)
            if (config.useGlobalCheck) {
                isHandlingEvent = false
            }
        }

        return true
    }

    fun reset() {
        isHandlingEvent = false
        lastClickTime = 0
        clickRecords.clear()
    }




}