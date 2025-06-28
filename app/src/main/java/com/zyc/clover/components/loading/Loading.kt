package com.zyc.clover.components.loading

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


object Loading {
    @Composable
    fun DouyinBounceLoader() = DouyinBounceLoaderImp()

    @Composable
    fun HorizontalBounceLoader() = HorizontalLoaderImp()

    @Composable
    fun AnimatedBallLoader() = AnimatedBallLoaderImp()

    @Composable
    fun TextLoader(isRotating: Boolean) = TextLoaderImp(size = 16.dp, color = Color.Unspecified, isRotating = true)
}
