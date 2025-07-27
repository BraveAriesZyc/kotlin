package com.zyc.feature.friend.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.zyc.core.common.utils.event.GlobalAntiShake.debounceClick
import com.zyc.core.model.entity.Friend
import com.zyc.core.model.entity.User
import com.zyc.core.ui.R

/**
 * 朋友列表项组件
 */
@SuppressLint("ModifierParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendItem(
    friend: Friend,
    user: User,
    onItemClick: () -> Unit = {},
    onStarClick: (Boolean) -> Unit = {},
    onMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()

            .padding(bottom = 8.dp)
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.White)
            .padding(16.dp)

            .debounceClick {
                onItemClick()
            },
        content = {
            Box {
                AsyncImage(
                    model = user.avatar ?: "https://picsum.photos/200/200?random=${user.id}",
                    contentDescription = "头像",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )

                // 在线状态指示器
                if (user.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            // 用户信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 显示名称（备注名或昵称）
                    Text(
                        text = friend.nickname ?: user.nickname ?: user.username,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // 特别关注标识
                    if (friend.isStarred) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "特别关注",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 个性签名或分组信息
                Text(
                    text = user.signature?.takeIf { it.isNotBlank() }
                        ?: friend.groupName?.let { "分组: $it" }
                        ?: "暂无签名",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 操作按钮
            Row {
                // 特别关注按钮
                IconButton(
                    onClick = { onStarClick(!friend.isStarred) }
                ) {
                    Icon(
                        imageVector = if (friend.isStarred) Icons.Default.Star else Icons.Default.Done,
                        contentDescription = if (friend.isStarred) "取消特别关注" else "特别关注",
                        tint = if (friend.isStarred) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 更多操作按钮
                IconButton(
                    onClick = onMoreClick
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "更多操作",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
}

/**
 * 空状态组件
 */
@SuppressLint("ModifierParameter")
@Composable
fun EmptyFriendsState(
    message: String = "暂无朋友",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "\uEC96",
            fontSize = 100.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            fontWeight  = FontWeight(600),
            fontFamily = FontFamily(Font(R.font.icons)),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}
