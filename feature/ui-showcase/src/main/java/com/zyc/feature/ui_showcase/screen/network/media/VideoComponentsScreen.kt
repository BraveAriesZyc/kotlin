package com.zyc.feature.ui_showcase.screen.network.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zyc.core.ui.components.media.ZVideo

@Composable
fun VideoComponentsScreen(
    onBack: () -> Unit = {}
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
            ZVideo(videoUrl = "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover_oss/1383efe0e5924feb98f1d6334df919ec_share_dadcc43dee633c78cb597fa308c9a57d1755097038756.mp4")
        }
    )
}