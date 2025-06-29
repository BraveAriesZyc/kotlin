package com.zyc.clover.pages.layout.children.friend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import com.zyc.clover.components.loading.WeLoadMore
import com.zyc.clover.components.refreshview.ZRefreshView
import com.zyc.clover.components.refreshview.rememberLoadMoreState
import kotlinx.coroutines.delay

@Composable
fun FriendScreen(navController: NavController) {

    val listState = rememberLazyListState()
    val loadMoreState = rememberLoadMoreState {
        delay(2000)

    }

    Scaffold {

        ZRefreshView(
            modifier = Modifier.padding(top = it.calculateTopPadding())
                .nestedScroll(loadMoreState.nestedScrollConnection),
            onRefresh = {
                delay(2000)
            },
            bgColor = Color.White,
            content = {
                LazyColumn(
                    state = listState, modifier = Modifier.fillMaxSize().background(Color.White),
                    content = {

                        items(3) {
                            Text(text = "item $it")
                        }
                        item {
                            if (loadMoreState.isLoadingMore) {
                                WeLoadMore(listState = listState)
                            }
                        }

                    }
                )
            }
        )
    }
}
