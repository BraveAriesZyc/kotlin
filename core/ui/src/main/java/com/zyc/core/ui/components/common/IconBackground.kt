package com.zyc.core.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.R

/**
 * 图标背景组件的默认配置常量
 */
private object IconBackgroundDefaults {
    /** 默认组件大小 */
    val DefaultSize: Dp = 60.dp

    /** 默认内边距 */
    val DefaultPadding: Dp = 16.dp

    /** 默认图标字体大小 */
    val DefaultIconSize: TextUnit = 24.sp

    /** 默认模糊半径 */
    val DefaultBlurRadius: Float = 60f

    /** 默认背景透明度 */
    val DefaultBackgroundAlpha: Float = 0.3f

    /** 默认文本模糊效果 */
    val DefaultTextBlur: Dp = 0.2.dp

    /** 默认圆形半径比例 */
    val DefaultRadiusRatio: Float = 2.5f

    /** 默认返回图标 */
    const val DefaultBackIcon: String = "\uEB2E"
}

/**
 * 带模糊背景效果的图标组件
 *
 * 该组件创建一个具有模糊背景效果的图标，由两层Box组成：
 * - 下层：绘制模糊的圆形背景
 * - 上层：显示字体图标文本
 *
 * @param color 图标和背景的主色调
 * @param icon 要显示的字体图标字符，默认为返回箭头图标
 * @param modifier 修饰符，用于自定义组件的外观和行为
 * @param size 组件的整体大小
 * @param padding 内边距
 * @param iconSize 图标字体大小
 * @param blurRadius 背景模糊效果的半径
 * @param backgroundAlpha 背景透明度（0.0-1.0）
 * @param textBlur 文本的轻微模糊效果
 * @param radiusRatio 圆形背景半径与组件尺寸的比例
 */
@Composable
fun IconBackground(
    color: Color,
    icon: String = IconBackgroundDefaults.DefaultBackIcon,
    modifier: Modifier = Modifier,
    size: Dp = IconBackgroundDefaults.DefaultSize,
    padding: Dp = IconBackgroundDefaults.DefaultPadding,
    iconSize: TextUnit = IconBackgroundDefaults.DefaultIconSize,
    blurRadius: Float = IconBackgroundDefaults.DefaultBlurRadius,
    backgroundAlpha: Float = IconBackgroundDefaults.DefaultBackgroundAlpha,
    textBlur: Dp = IconBackgroundDefaults.DefaultTextBlur,
    radiusRatio: Float = IconBackgroundDefaults.DefaultRadiusRatio
) {
    Box(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .size(size)
                    .padding(padding)
                    .drawBehind {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                this.color = color.copy(alpha = backgroundAlpha)
                                asFrameworkPaint().maskFilter =
                                    android.graphics.BlurMaskFilter(
                                        blurRadius,
                                        android.graphics.BlurMaskFilter.Blur.NORMAL
                                    )
                            }
                            canvas.drawCircle(
                                center = Offset(
                                    this.size.width / 2,
                                    this.size.height / 2
                                ),
                                radius = this.size.minDimension / radiusRatio,
                                paint
                            )
                        }
                    },
                contentAlignment = Alignment.Center,
                content = {}
            )

            // 上层：显示字体图标文本（重叠在背景上）
            Box(
                modifier = Modifier
                    .size(size) // 与下层保持相同大小，确保居中重叠
                    .padding(padding) // 保持一致的内边距，对齐位置
                    .blur(textBlur), // 添加轻微模糊效果
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    color = color,
                    fontSize = iconSize,
                    fontFamily = FontFamily(Font(R.font.icons))
                )
            }
        }
    )
}
