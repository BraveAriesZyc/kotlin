package com.zyc.clover.utils.event

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

import kotlinx.coroutines.delay

import kotlinx.coroutines.launch

object GlobalAntiShake {
    var globalConfig: Config = Config()
    private val clickRecords = mutableMapOf<String, Long>()
    private var isHandlingEvent = false
    private var lastClickTime: Long = 0
    private val mainScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    data class Config(
        val defaultDelay: Long = 300L, // 降低默认延迟，300ms更合理
        val enableGlobalCheck: Boolean = true
    )

    fun Modifier.debounceClick(
        key: String = "default",
        delay: Long = globalConfig.defaultDelay,
        enabled: Boolean = true,
        indication : Indication? = null,
        interactionSource: MutableInteractionSource? = null,
        onClick: () -> Unit,

    ): Modifier = composed {
        var lastClickTime by remember { mutableStateOf(0L) }
        var isDebouncing by remember { mutableStateOf(false) }

        clickable(
            indication =  indication,
            interactionSource = interactionSource,
            enabled = enabled && !isDebouncing
        ) {
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastClickTime >= delay &&
                (!globalConfig.enableGlobalCheck || canClickGlobally(currentTime))) {

                lastClickTime = currentTime
                clickRecords[key] = currentTime
                isDebouncing = true

                onClick() // 立即执行点击逻辑

                // 使用统一的协程作用域管理延迟
                mainScope.launch {
                    delay(delay)
                    isDebouncing = false
                    if (globalConfig.enableGlobalCheck) {
                        isHandlingEvent = false
                    }
                }
            }
        }
    }

    private fun canClickGlobally(currentTime: Long): Boolean {
        if (isHandlingEvent || currentTime - lastClickTime < globalConfig.defaultDelay) {
            return false
        }

        isHandlingEvent = true
        lastClickTime = currentTime
        return true
    }

    fun runWithDebounce(
        key: String = "default",
        delay: Long = globalConfig.defaultDelay,
        block: () -> Unit
    ): Boolean {
        val currentTime = System.currentTimeMillis()

        if (currentTime - (clickRecords[key] ?: 0) < delay ||
            (globalConfig.enableGlobalCheck && !canClickGlobally(currentTime))) {
            return false
        }

        clickRecords[key] = currentTime
        block() // 立即执行逻辑

        mainScope.launch {
            delay(delay)
            if (globalConfig.enableGlobalCheck) {
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

    @Composable
    fun SetupGlobalConfig(config: Config) {
        DisposableEffect(Unit) {
            globalConfig = config
            onDispose {
                // 清理资源
            }
        }
    }
}
