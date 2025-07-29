package com.zyc.core.ui.components.form.input

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

    @Composable
    fun Input(
        text: String,
        onChange: (String) -> Unit,
        brush: SolidColor,
        focusRequester: FocusRequester
    ) {
        BasicTextField(
            value = text,
            onValueChange = onChange,
            textStyle = TextStyle(brush = brush),
            modifier = Modifier.focusRequester(focusRequester)
        )
    }
