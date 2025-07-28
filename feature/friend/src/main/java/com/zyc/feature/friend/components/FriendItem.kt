package com.zyc.feature.friend.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.zyc.core.ui.utils.dateUtil.DateUtil

/**
 * 朋友列表项组件
 */
@SuppressLint("ModifierParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendItem(
    friend: Friend,
    user: User,
    onClick: (() -> Unit)? = null,
    color: Color
) {
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color)
            .let { modifier ->
                if (onClick != null) {
                    modifier.debounceClick { onClick() }
                } else {
                    modifier
                }
            }
            .padding(8.dp),
        content = {
            Box {
                AsyncImage(
                    model = user.avatar ?: "https://picsum.photos/200/200?random=${user.id}",
                    contentDescription = "头像",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp)),
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
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
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
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterEnd,
                                content = {
                                    Text(
                                        text = DateUtil.formatTimestamp(friend.updateTime, DateUtil.FORMAT_MM_DD),
                                    )
                                }
                            )
                        }
                    )
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
            )
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
            fontWeight = FontWeight(600),
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
