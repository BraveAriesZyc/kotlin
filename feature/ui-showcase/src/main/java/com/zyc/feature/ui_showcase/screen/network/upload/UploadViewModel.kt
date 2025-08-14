package com.zyc.feature.ui_showcase.screen.network.upload

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.zyc.core.network.api.UploadApi
import com.zyc.core.ui.utils.fileUtil.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.File

class UploadViewModel(private val application: Application) : AndroidViewModel(application) {
    // 上传进度
    private val _uploadProgress = MutableStateFlow(0)
    val uploadProgress: StateFlow<Int> = _uploadProgress

    // 是否处于上传状态
   private var _uploading = MutableStateFlow(false)
    val uploading: StateFlow<Boolean> = _uploading

    suspend fun uploadFile(uri: Uri) {
        _uploading.value = true
        return withContext(Dispatchers.IO) {
            // 查询文件元数据（包含原文件名和后缀）
            val deferredMetadata = async<Pair<String, String>?> {
                val projection = arrayOf(
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.MIME_TYPE
                )
                application.contentResolver.query(uri, projection, null, null)?.use { cursor ->
                    val nameColumn = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                    val mimeTypeColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)

                    if (cursor.moveToFirst()) {
                        val originalFileName = cursor.getString(nameColumn) // 原文件名称（带正确后缀）
                        val mimeType = cursor.getString(mimeTypeColumn)
                        return@async originalFileName to mimeType
                    }
                }
                null
            }

            // 构建临时文件（仅用于读取内容，名称不影响最终上传结果）
            val deferredFile = async {
                application.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val tempFile = File.createTempFile("uploadFile", null).apply {
                        deleteOnExit()
                    }
                    inputStream.copyTo(tempFile.outputStream())
                    return@async tempFile
                }
                null
            }

            val (originalFileName, mimeType) = deferredMetadata.await() ?: return@withContext
            val tempFile = deferredFile.await() ?: return@withContext
            // 上传文件（使用原文件名而非临时文件名）
            FileUtil.uploadWholeFile(
                tempFile,
                api = { chunkIndex, totalChunks, _, chunk ->
                        UploadApi.uploadFileChunk(
                            fileChunk = chunk,
                            chunkIndex,
                            totalChunks = totalChunks,
                            fileName = originalFileName, // 关键修改：使用原文件名称（带正确后缀）
                            onProgress = { safeBytesSent, currentChunkSize, globalSent, globalTotal ->
                                val globalProgress = (globalSent.toFloat() / globalTotal * 100).toInt()
                                _uploadProgress.value = globalProgress
                            }
                        )
                        // 合并分片时同样使用原文件名
                        if (chunkIndex == totalChunks - 1) {
                            _uploading.value = false
                            UploadApi.mergeFileChunks(
                                filePrefix = "clover_oss/",
                                totalChunks = totalChunks,
                                fileName = originalFileName, // 关键修改：使用原文件名称
                            )
                        }
                }
            )
        }
    }
}
