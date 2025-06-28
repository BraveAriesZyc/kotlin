package com.zyc.clover.components.menu

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.unit.sp
import com.zyc.clover.utils.event.GlobalAntiShake

@Composable
fun WeChatPopupMenu(

) {
    Button(
        onClick = {
            GlobalAntiShake.runWithDebounce {
              println("点击")
            }
        },
        content = {
            Text(
                text = "点我",
                fontSize = 16.sp
            )
        }
    )
}
