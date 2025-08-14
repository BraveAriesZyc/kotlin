# Video Module

视频播放模块，基于ExoPlayer和Jetpack Compose构建的现代化视频播放解决方案。

## 功能特性

### 🎬 核心功能
- **多格式支持**: 支持MP4、HLS、DASH等多种视频格式
- **自定义控制器**: 完全自定义的播放控制界面
- **播放状态管理**: 智能的播放状态跟踪和管理
- **响应式设计**: 适配不同屏幕尺寸和方向

### 🎮 播放控制
- 播放/暂停控制
- 进度条拖拽
- 音量控制和静音
- 播放速度调节（0.25x - 2.0x）
- 自动隐藏控制栏

### 🎨 UI特性
- Material Design 3 设计风格
- 流畅的动画效果
- 自定义圆角和样式
- 响应式布局

### 📱 页面组件
- **垂直视频页面**: 类似抖音、快手的上下滑动切换视频效果
- **页面指示器**: 显示当前视频位置
- **视频信息覆盖层**: 显示视频标题和描述
- **自动播放**: 智能的视频自动播放控制

## 组件介绍

### 1. VideoPlayer
基础视频播放器组件，提供核心播放功能。

```kotlin
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    showControls: Boolean = true,
    aspectRatio: Float? = 16f / 9f,
    onPlayerReady: ((ExoPlayer) -> Unit)? = null,
    onPlaybackStateChanged: ((Int) -> Unit)? = null
)
```

### 2. VideoPlayerCompose
完整功能的视频播放器，集成自定义控制栏。

```kotlin
@Composable
fun VideoPlayerCompose(
    videoUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    showSystemControls: Boolean = false,
    showCustomControls: Boolean = true,
    aspectRatio: Float? = 16f / 9f,
    cornerRadius: Float = 8f
)
```

### 3. SimpleVideoPlayer
简化版播放器，适用于快速集成场景。

```kotlin
@Composable
fun SimpleVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = false
)
```

### 4. VideoPlayerController
播放器状态管理和控制类。

```kotlin
class VideoPlayerController {
    val state: VideoPlayerState
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun setVolume(volume: Float)
    fun setPlaybackSpeed(speed: Float)
}
```

### 4. VerticalVideoPage
垂直滚动视频页面组件，类似抖音、快手等短视频应用的上下滑动切换视频效果。

```kotlin
@Composable
fun VerticalVideoPage(
    data: VerticalVideoPageData,
    modifier: Modifier = Modifier
)

data class VerticalVideoPageData(
    val videos: List<VideoData>,
    val onPageChange: (Int) -> Unit = {},
    val pagerState: PagerState,
    val autoPlay: Boolean = true,
    val showSystemControls: Boolean = false
)

data class VideoData(
    val url: String,
    val title: String = "",
    val description: String = "",
    val thumbnail: String = ""
)
```

### 5. SimpleVerticalVideoPage
简化版垂直视频页面组件，快速创建上下滑动的视频页面。

```kotlin
@Composable
fun SimpleVerticalVideoPage(
    videoUrls: List<String>,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    showSystemControls: Boolean = false,
    onPageChange: (Int) -> Unit = {}
)
```

## 使用示例

### 基础使用

```kotlin
@Composable
fun MyVideoScreen() {
    VideoPlayerCompose(
        videoUrl = "https://example.com/video.mp4",
        modifier = Modifier.fillMaxWidth(),
        autoPlay = true
    )
}
```

### 自定义控制

```kotlin
@Composable
fun CustomVideoPlayer() {
    val controller = rememberVideoPlayerController()
    
    VideoPlayerCompose(
        videoUrl = "https://example.com/video.mp4",
        showCustomControls = true,
        onPlayerReady = { player ->
            // 播放器准备就绪
            controller.attachPlayer(player)
        },
        onPlaybackStateChanged = { state ->
            // 处理播放状态变化
            when (state) {
                Player.STATE_READY -> {
                    // 准备就绪
                }
                Player.STATE_ENDED -> {
                    // 播放结束
                }
            }
        }
    )
}
```

### 全屏播放

```kotlin
@Composable
fun FullscreenVideo() {
    FullscreenVideoPlayer(
        videoUrl = "https://example.com/video.mp4",
        autoPlay = true
    )
}
```

### 垂直视频页面

```kotlin
@Composable
fun ShortVideoScreen() {
    val videos = listOf(
        VideoData(
            url = "https://example.com/video1.mp4",
            title = "视频标题1",
            description = "这是第一个视频的描述"
        ),
        VideoData(
            url = "https://example.com/video2.mp4",
            title = "视频标题2",
            description = "这是第二个视频的描述"
        )
    )
    
    val pageData = rememberVerticalVideoPageState(
        videos = videos,
        initialPage = 0
    )
    
    VerticalVideoPage(
        data = pageData.copy(
            autoPlay = true,
            showSystemControls = false,
            onPageChange = { page ->
                println("切换到视频: ${videos.getOrNull(page)?.title}")
            }
        )
    )
}
```

### 简化版垂直视频页面

```kotlin
@Composable
fun SimpleShortVideoScreen() {
    val videoUrls = listOf(
        "https://example.com/video1.mp4",
        "https://example.com/video2.mp4",
        "https://example.com/video3.mp4"
    )
    
    SimpleVerticalVideoPage(
        videoUrls = videoUrls,
        autoPlay = true,
        onPageChange = { page ->
            println("当前页面: $page")
        }
    )
}
```

## 工具类

### VideoUtils
提供视频相关的实用工具函数：

```kotlin
// 创建MediaItem
val mediaItem = VideoUtils.createMediaItem(
    videoUrl = "https://example.com/video.mp4",
    title = "视频标题",
    description = "视频描述"
)

// 格式化时间
val formattedTime = VideoUtils.formatTime(125000) // "02:05"

// 格式化文件大小
val fileSize = VideoUtils.formatFileSize(1024 * 1024 * 50) // "50.0 MB"

// 验证视频URL
val isValid = VideoUtils.isValidVideoUrl("https://example.com/video.mp4")

// 获取播放速度选项
val speedOptions = VideoUtils.getSpeedOptions()
```

## 依赖配置

模块已包含以下依赖：

```kotlin
// ExoPlayer
api(libs.androidx.media3.exoplayer)
api(libs.androidx.media3.exoplayer.dash)
api(libs.androidx.media3.ui)
api(libs.androidx.media3.ui.compose)
api(libs.androidx.media3.common)
api(libs.androidx.media3.session)

// Compose
api(libs.androidx.ui)
api(libs.androidx.material3)
api(libs.androidx.activity.compose)
```

## 权限要求

在应用的 `AndroidManifest.xml` 中添加网络权限：

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## 最佳实践

### 1. 内存管理
- 组件会自动管理ExoPlayer的生命周期
- 在组件销毁时自动释放播放器资源
- 避免在循环中创建多个播放器实例

### 2. 网络优化
- 使用适当的缓存策略
- 考虑预加载机制
- 监听网络状态变化

### 3. 用户体验
- 提供加载状态指示
- 处理播放错误
- 支持后台播放控制

### 4. 性能优化
- 使用合适的视频分辨率
- 实现懒加载
- 优化UI渲染性能

## 注意事项

1. **网络权限**: 确保应用具有网络访问权限
2. **HTTPS**: 建议使用HTTPS协议的视频URL
3. **格式支持**: 不同设备对视频格式的支持可能不同
4. **内存使用**: 大视频文件可能占用较多内存
5. **电池优化**: 长时间播放会消耗电池

## 故障排除

### 常见问题

**Q: 视频无法播放**
- 检查网络连接
- 验证视频URL是否有效
- 确认视频格式是否支持

**Q: 控制栏不显示**
- 检查 `showCustomControls` 参数
- 确认触摸事件是否被拦截

**Q: 播放卡顿**
- 检查网络速度
- 降低视频质量
- 优化缓存设置

## 更新日志

### v1.0.0
- 初始版本发布
- 基础播放功能
- 自定义控制栏
- 播放状态管理
- 工具类支持