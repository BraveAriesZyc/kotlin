package com.zyc.core.ui.components.layout.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

data class PageScreenData(
    val pageContents: List<@Composable () -> Unit>,
    val onPageChange: (Int) -> Unit = {},
    val pagerState: PagerState,
)

@Composable
fun PageScreen(data: PageScreenData) {

    // 页面变化回调
    LaunchedEffect(data.pagerState.currentPage) {
        data.onPageChange(data.pagerState.currentPage)
    }


    HorizontalPager(
        state = data.pagerState,
        modifier = Modifier
            .fillMaxSize()
    ) { page ->
        data.pageContents[page]()
    }
}
