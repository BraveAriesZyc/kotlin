package com.zyc.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.zyc.core.ui.theme.config.Typography

val LocalTheme = staticCompositionLocalOf<ThemeManager> {
    error("No LocalTheme provided")
}

@Composable
fun CloverAppTheme(
    content: @Composable () -> Unit
) {
    val themeModel = ThemeManager.getInstance()
    CompositionLocalProvider(
        LocalTheme provides themeModel){
        MaterialTheme(
            colorScheme = themeModel.currentTheme.value,
            typography = Typography,
            content = content
        )
    }
}