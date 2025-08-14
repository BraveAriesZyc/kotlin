package com.zyc.core.ui.components.media

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext

// 从本地媒体库获取视频数据
@Composable
fun getLocalVideos(): State<List<VideoItem>> {
    val context = LocalContext.current
    return produceState(initialValue = emptyList()) {
        value = loadLocalVideos(context.contentResolver)
    }
}

// 加载本地视频
private fun loadLocalVideos(contentResolver: ContentResolver): List<VideoItem> {
    val videos = mutableListOf<VideoItem>()

    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DESCRIPTION,
        MediaStore.Video.Media.DATA
    )

    val cursor = contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        "${MediaStore.Video.Media.DATE_ADDED} DESC"
    )

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val descColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION)
        val pathColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

        while (it.moveToNext()) {
            val id = it.getLong(idColumn).toInt()
            val name = it.getString(nameColumn)
            val description = it.getString(descColumn) ?: "本地视频"
            val uri = Uri.parse(it.getString(pathColumn))

            videos.add(
                VideoItem(
                    id = id,
                    uri = uri,
                    title = name,
                    description = description
                )
            )
        }
    }

    return videos
}
