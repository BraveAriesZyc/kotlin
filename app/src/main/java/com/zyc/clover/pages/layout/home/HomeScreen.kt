package com.zyc.clover.pages.layout.children.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.zyc.clover.components.ZAppBar


val PADDING = 8.dp
@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    // 获取屏幕宽度
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scrollState = rememberLazyListState()
    val homeViewModel: HomeViewModel = viewModel()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val topicList by homeViewModel.topicList.collectAsState()
    Scaffold(
        topBar = {
            ZAppBar(
                title = "首页",
                navigationIcon = Icons.Filled.KeyboardArrowLeft,
                actions = {

                }
            )
        },
        content = {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
                    .padding(horizontal = PADDING)
                    .background(color = MaterialTheme.colorScheme.surface),
                columns = StaggeredGridCells.Adaptive(screenWidth.dp / 2 - PADDING * 2),
                verticalItemSpacing = PADDING,
                horizontalArrangement = Arrangement.spacedBy(PADDING),
                content = {
                    items(topicList.size) { photo ->
                        TopicItem(item = topicList[photo])
                    }
                },
            )
        }
    )
}


@Composable
fun TopicItem(item: String) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .clip(RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.background)

    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .wrapContentHeight(),
            model = item,
            contentDescription = item,
        )
        Row(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                text = item,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp
                )
            )
        }
    }
}
