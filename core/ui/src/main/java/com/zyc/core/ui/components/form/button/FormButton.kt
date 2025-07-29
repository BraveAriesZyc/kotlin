package com.zyc.core.ui.components.form.button

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(4.dp),
) {
    val localIndication = LocalIndication.current
    val interactionSource =
        if (localIndication is IndicationNodeFactory) {
            null
        } else {
            remember { MutableInteractionSource() }
        }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = localIndication,
                interactionSource = interactionSource,
                onClick = {
                    onClick()
                }
            )
            .border(
                width = 1.dp,
                color = color,
                shape = shape
            )
            .clip(shape)
            .background(
                color = color.copy(
                    alpha = 0.3f
                )
            )
            .padding(vertical = 10.dp),

        contentAlignment = Alignment.Center,
        content = {
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    )
}
