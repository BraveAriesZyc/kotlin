package com.zyc.clover.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.mutableStateOf
import com.zyc.clover.ui.theme.config.antDesign
import com.zyc.clover.ui.theme.config.element
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class AppTheme {
    ANT_DESIGN,
    ELEMENT_UI
}

class ThemeManager private constructor() {

    // 使用StateFlow确保Compose能够观察并响应颜色变化
    private val _themeMap = MutableStateFlow(
        mapOf(
            AppTheme.ANT_DESIGN to antDesign,
            AppTheme.ELEMENT_UI to element
        )
    )
    val themeMap: StateFlow<Map<AppTheme, ColorScheme>> = _themeMap

    val currentTheme = mutableStateOf(element)

    fun updateTheme(theme: AppTheme) {
        currentTheme.value = _themeMap.value[theme] ?: antDesign
    }

    companion object {
        // 单例实例的延迟初始化
        @Volatile
        private var instance: ThemeManager? = null

        // 双重检查锁定获取单例实例
        fun getInstance(): ThemeManager {
            return instance ?: synchronized(this) {
                instance ?: ThemeManager().also { instance = it }
            }
        }
    }
}
