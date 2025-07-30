package com.zyc.feature.ui_showcase.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.core.ui.R
import com.zyc.core.ui.components.common.ZAppBar
import com.zyc.core.ui.components.layout.refreshview.BounceListView
import com.zyc.core.ui.utils.event.GlobalAntiShake.debounceClick
import com.zyc.feature.ui_showcase.model.ComponentCategory


@Composable
fun UIShowcaseScreen(
    onNavigateToCategory: (Any) -> Unit = {},
    viewModel: UIShowcaseHomeViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    Log.d("UIShowcaseScreen", "UIShowcaseScreen composable started")
    val uiState by viewModel.uiState.collectAsState()
    Log.d("UIShowcaseScreen", "UI state collected: isLoading=${uiState.isLoading}, error=${uiState.error}, categories size=${uiState.categories.size}")

    when {
        uiState.isLoading -> {
            Log.d("UIShowcaseScreen", "Showing loading state")
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            Log.e("UIShowcaseScreen", "Showing error state: ${uiState.error}")
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "加载失败: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { 
                            Log.d("UIShowcaseScreen", "Retry button clicked")
                            viewModel.clearError() 
                        }
                    ) {
                        Text("重试")
                    }
                }
            }
        }
        else -> {
            Log.d("UIShowcaseScreen", "Showing content with ${uiState.categories.size} categories")
            UIShowcaseHomeContent(
                categories = uiState.categories,
                onNavigateToCategory = { route ->
                    Log.d("UIShowcaseScreen", "Navigating to category: $route")
                    viewModel.navigateToCategory(route)
                    onNavigateToCategory(route)
                },
                onBack = onBack
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UIShowcaseHomeContent(
    categories: List<ComponentCategory>,
    onNavigateToCategory: (Any) -> Unit,
    onBack: () -> Unit
) {


    Scaffold(
        topBar = {
            ZAppBar(
                title = "UI组件展示",
                onBack = onBack
            )
        },
        modifier = Modifier.fillMaxSize(),
        content = { pd ->
            BounceListView(
                modifier = Modifier.fillMaxSize().padding(top = pd.calculateTopPadding()),
                contentPadding = PaddingValues(8.dp),
                content = {
                    item {
                        // 顶部说明卡片
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
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
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    category: ComponentCategory,
    onClick: () -> Unit
) {
    Box(

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(4.dp))
            .debounceClick { onClick() }
            .background(
                MaterialTheme.colorScheme.surfaceBright
            ),

        content = {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Text(
                        text = category.icon,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily(
                            Font(R.font.icons)
                        )
                    )
                    Spacer(modifier = Modifier.width(16.dp))
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
                    Text(
                        text = "\uEB3C",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily(
                            Font(R.font.icons)
                        )
                    )
                }
            )
        }
    )
}
