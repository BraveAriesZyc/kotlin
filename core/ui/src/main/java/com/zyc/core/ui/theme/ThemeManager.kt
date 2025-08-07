package com.zyc.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.mutableIntStateOf
import com.zyc.core.ui.theme.config.antDesign
import com.zyc.core.ui.theme.config.element
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class AppTheme(val title: String) {
    ANT_DESIGN("Ant Design"),
    ELEMENT_UI("Element UI")
}

class ThemeManager private constructor() {


    private val _themeList = MutableStateFlow(
        listOf(
            ThemeType(AppTheme.ELEMENT_UI.title, element),
            ThemeType(AppTheme.ANT_DESIGN.title, antDesign),
        )
    )


    val themeList: StateFlow<List<ThemeType>> = _themeList

    val currentTheme = mutableIntStateOf(0)

    fun updateTheme(index: Int) {
        currentTheme.intValue = index
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

class ThemeType(
    val title: String,
    val theme: ColorScheme
)