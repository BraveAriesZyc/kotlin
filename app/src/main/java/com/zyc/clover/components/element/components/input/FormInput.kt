package com.zyc.clover.components.element.components.input

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



/**
 * 自定义文本输入框组件，结合了 [BasicTextField] 的灵活性和 [TextField] 的丰富功能。
 *
 * 这个组件提供了与标准 TextField 类似的 API，但允许更细粒度的自定义，特别是在文本样式和光标颜色方面。
 *
 * @param value 当前文本框中的文本内容
 * @param onValueChange 文本内容变化时的回调函数
 * @param modifier 应用于文本框的修饰符
 * @param enabled 是否启用文本框
 * @param readOnly 是否为只读模式
 * @param textStyle 文本的样式
 * @param label 文本框的标签，当文本框为空或未获得焦点时显示
 * @param placeholder 占位符文本，当文本框为空时显示
 * @param leadingIcon 文本框左侧的图标
 * @param trailingIcon 文本框右侧的图标
 * @param prefix 文本框内文本的前缀
 * @param suffix 文本框内文本的后缀
 * @param supportingText 支持文本，显示在文本框下方
 * @param isError 是否显示错误状态
 * @param visualTransformation 文本的视觉转换，如密码隐藏
 * @param keyboardOptions 键盘的选项，如键盘类型、输入法选项
 * @param keyboardActions 键盘动作的回调，如按下回车键时的操作
 * @param singleLine 是否为单行文本框
 * @param maxLines 文本框的最大行数
 * @param minLines 文本框的最小行数
 * @param interactionSource 用于追踪和发射交互事件的源
 * @param shape 文本框的形状
 * @param colors 文本框的颜色配置
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: String  = "请输入",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = RoundedCornerShape(4.dp),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 4.dp,
        vertical = 8.dp
    ),
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedIndicatorColor = Color.Transparent,
    )
) {
    // 如果没有提供 interactionSource，则创建一个新的
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }
    // 设置文本选择颜色，根据是否处于错误状态使用不同的颜色
    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        // 使用 BasicTextField 作为基础组件，它提供了最小化的文本编辑功能
        BasicTextField(
            value = value,
            modifier =
                modifier
                    .fillMaxWidth()
                    .border(
                        shape = shape,
                        width = 1.dp,
                        color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
                    )
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }, // 使文本框宽度填满父容器
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = TextStyle(), // 注意：这里覆盖了传入的 textStyle 参数，使用了空的 TextStyle
            cursorBrush = SolidColor(if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox =
                @Composable { innerTextField ->
                    // 使用 TextFieldDefaults.DecorationBox 添加标签、图标、支持文本等装饰元素
                    // 这使得 BasicTextField 具有与标准 TextField 类似的外观和行为
                    TextFieldDefaults.DecorationBox(
                        // 当前文本框中的文本内容，用于确定是否显示占位符和如何转换文本
                        value = value,
                        // 文本的视觉转换，如密码隐藏、数字格式化等
                        // 此参数会影响文本的显示方式，但不会改变实际的文本内容
                        visualTransformation = visualTransformation,
                        // 内部文本编辑区域，由 BasicTextField 提供
                        // 这是实际的文本输入区域，DecorationBox 会围绕它添加各种装饰元素
                        innerTextField = innerTextField,
                        // 占位符文本，当文本框为空时显示在文本框内
                        // 通常用于提供关于预期输入的提示
                        placeholder = { Text(text = placeholder, style = TextStyle(fontSize = 14.sp)) },
                        // 文本框的标签，当文本框为空或未获得焦点时显示在文本框上方
                        // 当文本框获得焦点或有内容时，标签会浮动到文本框上方
                        label = label,
                        // 文本框左侧的图标，通常用于提供上下文信息或功能指示
                        // 例如，邮箱输入框可能使用邮件图标作为 leadingIcon
                        leadingIcon = leadingIcon,
                        // 文本框右侧的图标，通常用于提供额外功能或操作
                        // 例如，密码输入框可能使用眼睛图标作为 trailingIcon，用于切换密码可见性
                        trailingIcon = trailingIcon,
                        // 文本框内文本的前缀，显示在文本前面但在光标范围内
                        // 例如，货币输入框可能使用 "$" 作为前缀
                        prefix = prefix,
                        // 文本框内文本的后缀，显示在文本后面但在光标范围内
                        // 例如，百分比输入框可能使用 "%" 作为后缀
                        suffix = suffix,
                        // 支持文本，显示在文本框下方，通常用于提供额外信息或错误提示
                        // 当 isError 为 true 时，支持文本通常会显示为错误信息
                        supportingText = supportingText,
                        // 文本框的形状，决定了边框的圆角等属性
                        // 通常使用 RoundedCornerShape 或 RectangleShape
                        shape = shape,
                        // 是否为单行文本框
                        // 为 true 时，文本框不会换行，高度固定
                        // 为 false 时，文本框会根据内容自动换行
                        singleLine = singleLine,
                        // 是否启用文本框
                        // 为 false 时，文本框不可编辑，通常会显示为灰色
                        enabled = enabled,
                        // 是否显示错误状态
                        // 为 true 时，文本框边框、标签和支持文本会显示为错误颜色
                        isError = isError,
                        // 文本框内边距

                        contentPadding = contentPadding,
                        // 用于追踪和发射交互事件的源
                        // 此参数用于处理焦点状态、点击事件等交互行为
                        interactionSource = interactionSource,
                        // 文本框的颜色配置，包括背景色、文本颜色、边框颜色等
                        // 可以使用 TextFieldDefaults.colors() 创建自定义颜色配置
                        colors = colors
                    )

                }
        )
    }


}

