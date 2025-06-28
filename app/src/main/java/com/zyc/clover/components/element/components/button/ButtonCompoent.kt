package com.zyc.clover.components.element.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ElButton {
    @Composable
    fun Button(
        text: String,
        onClick: () -> Unit,
    ){














//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//                .background(
//                    color = Color.White,
//                    shape = RoundedCornerShape(8.dp)
//                )
//                .clickable {
//                    onClick()
//                },
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = text,
//                color = Color.Black,
//                fontSize = 16.sp,
//                fontWeight= FontWeight.Bold
//            )
//        }
    }
    @Composable
    fun IconButton(){

    }
}
