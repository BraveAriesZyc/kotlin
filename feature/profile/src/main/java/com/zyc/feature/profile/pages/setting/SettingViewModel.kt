package com.zyc.feature.profile.pages.setting


import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.zyc.core.router.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingViewModel(
    navController: NavController,
) : ViewModel() {
    private val _settingList = MutableStateFlow(
        listOf(
            SettingItemType(
                title = "主题",
                icon = "\uEB7F",
                color = Color.Companion.Red,
                onClick = {
                    navController.navigate(Routes.Profile.ThemeSetting)
                }
            ),
            SettingItemType(
                title = "语言",
                icon = "\uEECC",
                color = Color.Companion.Blue,
                onClick = {
//                    navController.navigate(Routes.Profile.LanguageSetting)
                }
            )
        )
    )

    val settingList: StateFlow<List<SettingItemType>> = _settingList
}


class SettingItemType(
    val title: String,
    val icon: String,
    val color: Color,
    val onClick: () -> Unit
)