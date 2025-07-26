package com.zyc.core.ui.components.loading

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


object Loading {
    @Composable
    fun DouyinBounceLoader() = DouyinBounceLoaderImp()

    @Composable
    fun HorizontalBounceLoader() = HorizontalLoaderImp()

    @Composable
    fun AnimatedBallLoader() = AnimatedBallLoaderImp()

    @Composable
    fun TextLoader(isRotating: Boolean) = TextLoaderImp(size = 16.sp, color = Color.Unspecified, isRotating = true)
}
