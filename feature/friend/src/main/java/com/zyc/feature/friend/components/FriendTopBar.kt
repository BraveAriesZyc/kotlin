package com.zyc.feature.friend.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.zyc.core.ui.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendTopBar(
    friendRequestCount: Int,
    onAdd: () -> Unit,
    onShowFriendRequests: () -> Unit
) {
    TopAppBar(
        title = {
            Text("朋友")
        },
        actions = {
            Box {
                IconButton(onClick = onShowFriendRequests) {
                    Text(
                        text = "\uEDA8",
                        fontSize = 20.sp,
                        fontWeight  = FontWeight(600),
                        fontFamily = FontFamily(Font(R.font.icons)),
                    )
                }
                if (friendRequestCount > 0) {
                    Badge(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = if (friendRequestCount > 99) "99+" else friendRequestCount.toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            // 搜索按钮
            IconButton(onClick = onAdd) {
                Text(
                    text = "\uEA04",
                    fontSize = 26.sp,
                    fontWeight  = FontWeight(600),
                    fontFamily = FontFamily(Font(R.font.icons)),
                )
            }
        }
    )
}
