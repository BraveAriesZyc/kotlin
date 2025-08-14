package com.zyc.core.video.controls

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zyc.core.video.player.VideoPlayerController
import com.zyc.core.video.player.VideoPlayerState
import kotlinx.coroutines.delay

/**
 * 视频控制栏
 * @param controller 视频播放器控制器
 * @param modifier 修饰符
 * @param autoHide 是否自动隐藏控制栏
 * @param autoHideDelay 自动隐藏延迟时间（毫秒）
 */
@Composable
fun VideoControls(
    controller: VideoPlayerController,
    modifier: Modifier = Modifier,
    autoHide: Boolean = true,
    autoHideDelay: Long = 3000L
) {
    val state = controller.state
    var isVisible by remember { mutableStateOf(true) }
    
    // 自动隐藏逻辑
    LaunchedEffect(state.isPlaying, isVisible) {
        if (autoHide && state.isPlaying && isVisible) {
            delay(autoHideDelay)
            isVisible = false
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { isVisible = !isVisible }
    ) {
        // 播放/暂停按钮（中央）
        AnimatedVisibility(
            visible = isVisible || !state.isPlaying,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            PlayPauseButton(
                isPlaying = state.isPlaying,
                isLoading = state.isLoading,
                onClick = {
                    if (state.isPlaying) {
                        controller.pause()
                    } else {
                        controller.play()
                    }
                    isVisible = true
                }
            )
        }
        
        // 底部控制栏
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomControls(
                state = state,
                controller = controller,
                onInteraction = { isVisible = true }
            )
        }
    }
}

/**
 * 播放/暂停按钮
 */
@Composable
private fun PlayPauseButton(
    isPlaying: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(
                color = Color.Black.copy(alpha = 0.6f),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = Color.White,
                    strokeWidth = 3.dp
                )
            }
            isPlaying -> {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "暂停",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "播放",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

/**
 * 底部控制栏
 */
@Composable
private fun BottomControls(
    state: VideoPlayerState,
    controller: VideoPlayerController,
    onInteraction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.8f)
                    )
                )
            )
            .padding(16.dp)
    ) {
        // 进度条
        VideoProgressBar(
            progress = state.progress,
            onProgressChange = { progress ->
                controller.seekToProgress(progress)
                onInteraction()
            }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 控制按钮行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 时间显示
            Text(
                text = "${formatTime(state.currentPosition)} / ${formatTime(state.duration)}",
                color = Color.White,
                fontSize = 12.sp
            )
            
            Row {
                // 静音按钮
                ControlButton(
                    icon = if (state.isMuted) Icons.Default.ThumbUp else Icons.Default.Star,
                    contentDescription = if (state.isMuted) "取消静音" else "静音",
                    onClick = {
                        controller.setMuted(!state.isMuted)
                        onInteraction()
                    }
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // 倍速按钮
                SpeedButton(
                    currentSpeed = state.playbackSpeed,
                    onSpeedChange = { speed ->
                        controller.setPlaybackSpeed(speed)
                        onInteraction()
                    }
                )
            }
        }
    }
}

/**
 * 视频进度条
 */
@Composable
private fun VideoProgressBar(
    progress: Float,
    onProgressChange: (Float) -> Unit
) {
    Slider(
        value = progress,
        onValueChange = onProgressChange,
        modifier = Modifier.fillMaxWidth(),
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
        )
    )
}

/**
 * 控制按钮
 */
@Composable
private fun ControlButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(32.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * 倍速按钮
 */
@Composable
private fun SpeedButton(
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val speeds = listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f)
    
    Box {
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier
                .background(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${currentSpeed}x",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            speeds.forEach { speed ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${speed}x",
                            fontWeight = if (speed == currentSpeed) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onSpeedChange(speed)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * 格式化时间
 */
private fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}