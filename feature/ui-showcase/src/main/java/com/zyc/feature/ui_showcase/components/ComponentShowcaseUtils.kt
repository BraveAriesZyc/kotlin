package com.zyc.feature.ui_showcase.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 组件展示区域的通用容器
 * @param title 区域标题
 * @param description 区域描述
 * @param content 区域内容
 */
@Composable
fun ComponentSection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题和描述
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // 分隔线
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            thickness = 1.dp
        )
        
        // 内容区域
        content()
    }
}

/**
 * 单个组件演示的容器
 * @param title 演示标题
 * @param description 演示描述
 * @param content 演示内容
 */
@Composable
fun ComponentDemo(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 演示标题和描述
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // 演示内容
        content()
    }
}

/**
 * 代码展示卡片
 * @param title 代码标题
 * @param code 代码内容
 */
@Composable
fun CodeShowcase(
    title: String,
    code: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                text = code,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 功能特性列表
 * @param title 列表标题
 * @param features 特性列表
 */
@Composable
fun FeatureList(
    title: String,
    features: List<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        features.forEach { feature ->
            Text(
                text = "• $feature",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 参数说明卡片
 * @param parameters 参数列表，格式为 "参数名: 类型 - 说明"
 */
@Composable
fun ParameterCard(
    parameters: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "参数说明：",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            parameters.forEach { param ->
                Text(
                    text = "• $param",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 使用建议卡片
 * @param suggestions 建议列表
 */
@Composable
fun SuggestionCard(
    suggestions: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "使用建议：",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
            suggestions.forEach { suggestion ->
                Text(
                    text = "• $suggestion",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 注意事项卡片
 * @param notes 注意事项列表
 */
@Composable
fun NoteCard(
    notes: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "注意事项：",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.error
            )
            notes.forEach { note ->
                Text(
                    text = "• $note",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 示例数据生成器
 */
object ShowcaseDataGenerator {
    
    /**
     * 生成示例列表数据
     */
    fun generateSampleList(count: Int, prefix: String = "项目"): List<String> {
        return (1..count).map { "$prefix $it" }
    }
    
    /**
     * 生成示例用户数据
     */
    data class SampleUser(
        val name: String,
        val avatar: String,
        val status: String
    )
    
    fun generateSampleUsers(count: Int): List<SampleUser> {
        val names = listOf("张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十")
        val statuses = listOf("在线", "离线", "忙碌", "离开")
        
        return (1..count).map { index ->
            SampleUser(
                name = names[index % names.size],
                avatar = "头像${index}",
                status = statuses[index % statuses.size]
            )
        }
    }
    
    /**
     * 生成示例消息数据
     */
    data class SampleMessage(
        val content: String,
        val time: String,
        val isFromMe: Boolean
    )
    
    fun generateSampleMessages(count: Int): List<SampleMessage> {
        val messages = listOf(
            "你好！",
            "最近怎么样？",
            "今天天气不错",
            "有空一起吃饭吗？",
            "好的，没问题",
            "谢谢你的帮助",
            "明天见！",
            "晚安"
        )
        
        return (1..count).map { index ->
            SampleMessage(
                content = messages[index % messages.size],
                time = "${10 + index % 12}:${(index * 5) % 60}",
                isFromMe = index % 2 == 0
            )
        }
    }
}