package com.zyc.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val themeList by themeModel.themeList.collectAsState()
    CompositionLocalProvider(
        LocalTheme provides themeModel){
        MaterialTheme(
            colorScheme = themeList[themeModel.currentTheme.intValue].theme,
            typography = Typography,
            content = content
        )
    }
}