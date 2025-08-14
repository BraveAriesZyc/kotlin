import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.TrackSelector

@OptIn(UnstableApi::class)
class Media3PlayerManager(context: Context) {
    private val trackSelector: TrackSelector = DefaultTrackSelector(context).apply {
        setParameters(buildUponParameters().setMaxVideoSizeSd())
    }

    // 使用最新的ExoPlayer.Builder (Media3 1.8.0)
    private val player: ExoPlayer = ExoPlayer.Builder(context)
        .setTrackSelector(trackSelector)
        .setHandleAudioBecomingNoisy(true)
        .build()

    private val context = context

    // 基础数据源工厂 (Media3 1.8.0 推荐用法)
    private val defaultDataSourceFactory: DefaultDataSource.Factory
        get() {
            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setUserAgent("VideoManager/1.0")
                .setConnectTimeoutMs(8000)
                .setReadTimeoutMs(8000)
                .setAllowCrossProtocolRedirects(true)

            return DefaultDataSource.Factory(context, httpDataSourceFactory)
        }

    // 带缓存的数据源工厂 - 适配Media3 1.8.0的正确实现
    fun createCacheDataSourceFactory(cache: Cache): CacheDataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(defaultDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            // 移除对WriteAheadCache的依赖，使用默认缓存写入配置
            .setCacheWriteDataSinkFactory(null)
    }

    // 设置媒体资源 (支持本地文件和网络URL)
    fun setMediaSource(uri: String) {
        val mediaItem = MediaItem.fromUri(uri).apply {
            mediaMetadata = mediaMetadata.buildUpon()
                .setTitle("正在播放")
                .build()
        }

        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(defaultDataSourceFactory)
            .createMediaSource(mediaItem)

        player.setMediaSource(mediaSource)
        player.prepare()
    }

    // 获取播放器实例
    fun getPlayer() = player

    // 播放控制
    fun play() {
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    // 释放资源
    fun release() {
        player.release()
    }
}
