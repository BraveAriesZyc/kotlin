package com.zyc.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.R
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick

/**
 * 简洁的顶部导航栏组件
 * @param title 标题文本
 * @param onBack 返回按钮点击事件
 * @param actions 右侧操作按钮组合
 * @param backgroundColor 背景颜色
 */


/**
 * 默认返回图标
 */
@Composable
fun CreateBackIcon(icon: String = "\uEB2E") {
    Text(
        icon,
        fontSize = 24.sp,
        color = LocalContentColor.current,
        fontFamily = FontFamily(Font(R.font.icons))
    )
}

@Composable
fun ZAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧按钮区域 - 固定最小宽度
            Box(
                modifier = Modifier
                    .widthIn(min = 48.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (onBack != null) {
                    Box(
                        modifier = Modifier.debounceClick { onBack() },
                        content = {
                            CreateBackIcon()
                        }
                    )
                }
            }

            // 中间标题
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 右侧按钮区域 - 固定最小宽度
            Box(
                modifier = Modifier
                    .widthIn(min = 48.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions()
                }
            }
        }
    }
}
