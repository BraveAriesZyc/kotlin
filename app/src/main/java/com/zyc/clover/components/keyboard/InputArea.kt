package com.zyc.clover.components.keyboard

import android.view.ViewTreeObserver
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.zyc.clover.utils.event.GlobalAntiShake.debounceClick
import com.zyc.clover.utils.event.keyboardStateListener

@Composable
fun InputArea(
    onSend: (String) -> Unit,
    focusRequester: FocusRequester = FocusRequester()
) {
    var text by remember { mutableStateOf("") }

    // 计算底部内边距
    val insets = WindowInsets.systemBars
    val bottomInset = with(LocalDensity.current) {
        insets.getBottom(this).toDp()
    }

    // 键盘状态
    val keyboardState = remember { mutableStateOf(false) }

    // 键盘状态变化时的动画高度
    val bottomPadding by animateDpAsState(
        targetValue = if (keyboardState.value) 0.dp else bottomInset,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    )

    Row(
        modifier = Modifier
            .keyboardStateListener(onKeyboardStateChanged = { keyboardState.value = it })
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = bottomPadding)
            .padding(vertical = 2.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 返回按钮
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 4.dp)
                .align(Alignment.Top),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                if (keyboardState.value) Icons.Default.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                "返回",
                modifier = Modifier.size(30.dp)
            )
        }

        // 输入框
        ZAInput(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.background),
            cursorColor = MaterialTheme.colorScheme.primary,
            text = text,
            focusRequester = focusRequester,
            onChange = { text = it }
        )

        // 发送区域
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(top = 6.dp, end = 4.dp)
                .align(Alignment.Top),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 添加按钮
            Box(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .clickable { /* 添加按钮点击事件 */ },
                content = {
                    Icon(Icons.Default.Add, "添加")
                }
            )

            // 发送按钮
            Column(
                modifier = Modifier
                    .debounceClick {
                        onSend.invoke(text)
                        text = ""
                    }
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "发送",
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(
                        fontSize = 12.sp,
                    )
                )
            }
        }
    }
}

@Composable
fun ZAInput(
    modifier: Modifier = Modifier,
    cursorColor: Color = Color.Black,
    text: String,
    maxLines: Int = 5,
    focusRequester: FocusRequester = FocusRequester(),
    onChange: (String) -> Unit
) {
    val brush = remember {
        Brush.linearGradient(
            colors = listOf(Color.Red, Color.Blue)
        )
    }

    BasicTextField(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .focusRequester(focusRequester),
        maxLines = maxLines,
        cursorBrush = SolidColor(cursorColor),
        decorationBox = { innerTextField -> innerTextField() },
        value = text,
        onValueChange = onChange,
        textStyle = TextStyle(
            brush = brush,
            fontSize = 14.sp,
            lineHeight = 18.sp
        )
    )
}
