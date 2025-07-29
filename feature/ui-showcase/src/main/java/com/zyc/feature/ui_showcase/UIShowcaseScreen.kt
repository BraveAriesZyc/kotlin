package com.zyc.feature.ui_showcase

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zyc.core.router.Routes
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView
import com.zyc.feature.ui_showcase.animation.AnimationComponentsScreen
import com.zyc.feature.ui_showcase.common.CommonComponentsScreen
import com.zyc.feature.ui_showcase.feedback.FeedbackComponentsScreen
import com.zyc.feature.ui_showcase.form.FormComponentsScreen
import com.zyc.feature.ui_showcase.hardware.HardwareComponentsScreen
import com.zyc.feature.ui_showcase.interaction.InteractionComponentsScreen
import com.zyc.feature.ui_showcase.layout.LayoutComponentsScreen
import com.zyc.feature.ui_showcase.navigation.NavigationComponentsScreen

data class ComponentCategory(
    val title: String,
    val description: String,
    val route: Any,
    val icon: ImageVector? = null,
    val items: List<String> = emptyList()
)



@Composable
fun UIShowcaseScreen(
    onBack: () -> Unit = {}
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.UIShowcase.UIShowcaseHome
    ) {
        composable<Routes.UIShowcase.UIShowcaseHome> {
            UIShowcaseHomeScreen(
                onBack = onBack,
                onNavigateToCategory = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable<Routes.UIShowcase.CommonComponents> {
            CommonComponentsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Routes.UIShowcase.FormComponents> {
            FormComponentsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Routes.UIShowcase.FeedbackComponents> {
            FeedbackComponentsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Routes.UIShowcase.LayoutComponents> {
            LayoutComponentsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Routes.UIShowcase.NavigationComponents> {
            NavigationComponentsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Routes.UIShowcase.InteractionComponents> {
            InteractionComponentsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Routes.UIShowcase.AnimationComponents> {
            AnimationComponentsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Routes.UIShowcase.HardwareComponents> {
            HardwareComponentsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UIShowcaseHomeScreen(
    onNavigateToCategory: (Any) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val categories = listOf(
        ComponentCategory(
            title = "通用组件",
            description = "基础的通用UI组件",
            route = Routes.UIShowcase.CommonComponents,
            icon = Icons.Default.ThumbUp,
            items = listOf(
                "ZAppBar - 统一的应用顶部导航栏",
                "通用按钮组件",
                "通用卡片组件"
            )
        ),
        ComponentCategory(
            title = "表单组件",
            description = "用于用户输入的表单相关组件",
            route = Routes.UIShowcase.FormComponents,
            icon = Icons.Default.Edit,
            items = listOf(
                "ButtonComponent - 基础按钮组件",
                "FormButton - 表单专用按钮",
                "FormInput - 表单输入框",
                "InputComponent - 通用输入组件",
                "NoBorderFormInput - 无边框输入框"
            )
        ),
        ComponentCategory(
            title = "反馈组件",
            description = "用于用户反馈和状态显示的组件",
            route = Routes.UIShowcase.FeedbackComponents,
            icon = Icons.Default.ThumbUp,
            items = listOf(
                "Loading - 加载动画组件",
                "AnimatedBallLoaderImp - 球形加载动画",
                "DouyinBounceLoaderImp - 抖音风格弹跳加载",
                "HorizontalBounceLoaderImp - 水平弹跳加载",
                "TextLoaderImp - 文字加载动画",
                "CustomProgressBar - 自定义进度条"
            )
        ),
        ComponentCategory(
            title = "布局组件",
            description = "用于页面布局和容器的组件",
            route = Routes.UIShowcase.LayoutComponents,
            icon = Icons.Default.ThumbUp,
            items = listOf(
                "PageScreen - 统一页面布局",
                "PageScreenData - 页面状态数据",
                "BounceListView - 弹性列表视图",
                "ZRefreshView - 下拉刷新容器"
            )
        ),
        ComponentCategory(
            title = "导航组件",
            description = "用于应用导航的组件",
            route = Routes.UIShowcase.NavigationComponents,
            icon = Icons.Default.ThumbUp,
            items = listOf(
                "WeChatPopupMenu - 微信风格弹出菜单",
                "MenuAction - 菜单操作数据类"
            )
        ),
        ComponentCategory(
            title = "交互组件",
            description = "用于用户交互的组件",
            route = Routes.UIShowcase.InteractionComponents,
            icon = Icons.Default.ThumbUp,
            items = listOf(
                "InputArea - 智能输入区域",
                "键盘相关组件",
                "手势识别组件"
            )
        ),
        ComponentCategory(
            title = "动画组件",
            description = "各种动画效果组件",
            route = Routes.UIShowcase.AnimationComponents,
            icon = Icons.Default.ThumbUp,
            items = listOf(
                "组合动画效果",
                "过渡动画组件",
                "自定义动画效果"
            )
        ),
        ComponentCategory(
            title = "硬件接口组件",
            description = "用于硬件设备交互的组件",
            route = Routes.UIShowcase.HardwareComponents,
            icon = Icons.Default.Settings,
            items = listOf(
                "相机组件 - 相机拍照和录像",
                "传感器组件 - 重力感应、陀螺仪等",
                "蓝牙组件 - 蓝牙设备连接和通信",
                "GPS定位组件 - 位置获取和地图显示",
                "NFC组件 - 近场通信功能",
                "指纹识别组件 - 生物识别认证",
                "振动反馈组件 - 触觉反馈",
                "音频组件 - 录音和播放"
            )
        )
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ZAppBar(
            title = "UI组件展示",
            onBack = onBack
        )

        BounceListView(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {
            item {
                // 顶部说明卡片
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    content = {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "UI组件库展示",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "这里展示了项目中所有UI组件的使用方法和示例，点击下方分类查看具体组件的详细用法。",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                )
            }

            items(categories) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onNavigateToCategory(category.route) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    category: ComponentCategory,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标
            category.icon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = category.title,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            // 内容
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (category.items.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "包含 ${category.items.size} 个组件",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 箭头图标
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = "进入",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
