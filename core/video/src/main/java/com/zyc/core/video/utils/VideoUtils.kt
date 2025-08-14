package com.zyc.core.video.utils

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource

/**
 * 视频工具类
 */
object VideoUtils {
    
    /**
     * 创建MediaItem
     * @param videoUrl 视频URL
     * @param title 视频标题
     * @param description 视频描述
     * @param artworkUri 封面图片URI
     */
    fun createMediaItem(
        videoUrl: String,
        title: String? = null,
        description: String? = null,
        artworkUri: Uri? = null
    ): MediaItem {
        val mediaMetadata = MediaMetadata.Builder()
            .apply {
                title?.let { setTitle(it) }
                description?.let { setDescription(it) }
                artworkUri?.let { setArtworkUri(it) }
            }
            .build()
            
        return MediaItem.Builder()
            .setUri(videoUrl)
            .setMediaMetadata(mediaMetadata)
            .build()
    }
    
    /**
     * 创建MediaSource
     * @param context 上下文
     * @param videoUrl 视频URL
     */
    @UnstableApi
    fun createMediaSource(
        context: Context,
        videoUrl: String
    ): MediaSource {
        val dataSourceFactory = DefaultDataSource.Factory(
            context,
            DefaultHttpDataSource.Factory()
        )
        
        val uri = Uri.parse(videoUrl)
        
        return when {
            videoUrl.contains(".mpd") -> {
                // DASH
                DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))
            }
            videoUrl.contains(".m3u8") -> {
                // HLS
                HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))
            }
            else -> {
                // Progressive (MP4, etc.)
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))
            }
        }
    }
    
    /**
     * 格式化时间
     * @param timeMs 时间（毫秒）
     * @return 格式化后的时间字符串 (mm:ss 或 hh:mm:ss)
     */
    fun formatTime(timeMs: Long): String {
        if (timeMs <= 0) return "00:00"
        
        val totalSeconds = timeMs / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
    
    /**
     * 格式化文件大小
     * @param bytes 字节数
     * @return 格式化后的文件大小字符串
     */
    fun formatFileSize(bytes: Long): String {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = bytes.toDouble()
        var unitIndex = 0
        
        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024
            unitIndex++
        }
        
        return String.format("%.1f %s", size, units[unitIndex])
    }
    
    /**
     * 检查是否为有效的视频URL
     * @param url 视频URL
     * @return 是否为有效的视频URL
     */
    fun isValidVideoUrl(url: String): Boolean {
        if (url.isBlank()) return false
        
        val videoExtensions = listOf(
            ".mp4", ".avi", ".mov", ".wmv", ".flv", ".webm", ".mkv",
            ".m4v", ".3gp", ".ts", ".m3u8", ".mpd"
        )
        
        return try {
            val uri = Uri.parse(url)
            uri.scheme in listOf("http", "https", "file", "content") &&
                    (videoExtensions.any { url.contains(it, ignoreCase = true) } ||
                            url.contains("youtube", ignoreCase = true) ||
                            url.contains("vimeo", ignoreCase = true))
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 获取视频质量选项
     */
    fun getQualityOptions(): List<Pair<String, String>> {
        return listOf(
            "auto" to "自动",
            "1080p" to "1080P",
            "720p" to "720P",
            "480p" to "480P",
            "360p" to "360P",
            "240p" to "240P"
        )
    }
    
    /**
     * 获取播放速度选项
     */
    fun getSpeedOptions(): List<Pair<Float, String>> {
        return listOf(
            0.25f to "0.25x",
            0.5f to "0.5x",
            0.75f to "0.75x",
            1.0f to "正常",
            1.25f to "1.25x",
            1.5f to "1.5x",
            1.75f to "1.75x",
            2.0f to "2.0x"
        )
    }
    
    /**
     * 创建ExoPlayer实例
     * @param context 上下文
     * @return ExoPlayer实例
     */
    fun createExoPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context)
            .build()
    }
    
    /**
     * 设置播放器的基本配置
     * @param player ExoPlayer实例
     * @param autoPlay 是否自动播放
     * @param repeatMode 重复模式
     */
    fun configurePlayer(
        player: ExoPlayer,
        autoPlay: Boolean = true,
        repeatMode: Int = ExoPlayer.REPEAT_MODE_OFF
    ) {
        player.playWhenReady = autoPlay
        player.repeatMode = repeatMode
    }
}