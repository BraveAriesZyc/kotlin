package com.zyc.core.ui.components.refreshview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun BounceScrollExample() {
    var isRefreshing by remember { mutableStateOf(false) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var items by remember { mutableStateOf((1..20).map { "Item $it" }) }

    ZRefreshView(
        modifier = Modifier.fillMaxSize(),
        onRefresh = {
            isRefreshing = true
            delay(2000) // 模拟网络请求
            items = (1..20).map { "刷新后的 Item $it" }
            isRefreshing = false
        },
        onLoadMore = {
            isLoadingMore = true
            delay(1500) // 模拟加载更多
            val currentSize = items.size
            items = items + ((currentSize + 1)..(currentSize + 10)).map { "Item $it" }
            isLoadingMore = false
        },
        isRefreshing = isRefreshing,
        isLoadingMore = isLoadingMore,
        enableLoadMore = true
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "弹性滚动示例",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "向下拉动可以刷新，向上滚动到底部可以加载更多",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "滚动到边界时会有弹性效果",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
        }
    }
}