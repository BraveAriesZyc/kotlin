package com.zyc.clover.components.page_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// 定义页面数据结构
data class PageScreenData(
    val pageContents: List<@Composable () -> Unit>,
    val onPageChange: (Int) -> Unit = {},
    val pagerState : PagerState
)

@Composable
fun PageScreen(data: PageScreenData) {
    HorizontalPager(
        modifier = Modifier
            .fillMaxSize(),
        state = data.pagerState,

    ) { page ->
        data.pageContents[page]()
    }
}
