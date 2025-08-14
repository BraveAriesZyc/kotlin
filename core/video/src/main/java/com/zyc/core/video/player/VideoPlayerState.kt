package com.zyc.core.video.player

import androidx.compose.runtime.*
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

/**
 * 视频播放器状态
 */
data class VideoPlayerState(
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val playbackState: Int = Player.STATE_IDLE,
    val volume: Float = 1f,
    val isMuted: Boolean = false,
    val playbackSpeed: Float = 1f
) {
    val progress: Float
        get() = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f
        
    val isEnded: Boolean
        get() = playbackState == Player.STATE_ENDED
        
    val isReady: Boolean
        get() = playbackState == Player.STATE_READY
        
    val isBuffering: Boolean
        get() = playbackState == Player.STATE_BUFFERING
}

/**
 * 视频播放器控制器
 */
class VideoPlayerController {
    private var _player: ExoPlayer? = null
    private var _state by mutableStateOf(VideoPlayerState())
    
    val state: VideoPlayerState get() = _state
    
    fun attachPlayer(player: ExoPlayer) {
        _player = player
        
        // 添加播放器监听器
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                _state = _state.copy(
                    playbackState = playbackState,
                    isLoading = playbackState == Player.STATE_BUFFERING
                )
            }
            
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state = _state.copy(isPlaying = isPlaying)
            }
        })
        
        // 启动位置更新
        startPositionUpdates()
    }
    
    fun detachPlayer() {
        _player = null
        _state = VideoPlayerState()
    }
    
    fun play() {
        _player?.play()
    }
    
    fun pause() {
        _player?.pause()
    }
    
    fun seekTo(positionMs: Long) {
        _player?.seekTo(positionMs)
    }
    
    fun seekToProgress(progress: Float) {
        val duration = _player?.duration ?: 0L
        if (duration > 0) {
            val position = (duration * progress).toLong()
            seekTo(position)
        }
    }
    
    fun setVolume(volume: Float) {
        _player?.volume = volume.coerceIn(0f, 1f)
        _state = _state.copy(volume = volume)
    }
    
    fun setMuted(muted: Boolean) {
        _player?.volume = if (muted) 0f else _state.volume
        _state = _state.copy(isMuted = muted)
    }
    
    fun setPlaybackSpeed(speed: Float) {
        _player?.setPlaybackSpeed(speed)
        _state = _state.copy(playbackSpeed = speed)
    }
    
    private fun startPositionUpdates() {
        // 这里可以使用协程定期更新播放位置
        // 为了简化，这里只在状态变化时更新
    }
    
    fun updatePosition() {
        val player = _player ?: return
        _state = _state.copy(
            currentPosition = player.currentPosition,
            duration = player.duration.takeIf { it > 0 } ?: 0L
        )
    }
}

/**
 * 记住视频播放器控制器
 */
@Composable
fun rememberVideoPlayerController(): VideoPlayerController {
    return remember { VideoPlayerController() }
}