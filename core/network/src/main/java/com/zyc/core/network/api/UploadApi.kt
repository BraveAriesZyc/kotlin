package com.zyc.core.network.api


import com.zyc.core.network.RequestHttp
import com.zyc.core.network.ResponseData
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.File

object UploadApi {
    suspend fun uploadFile(file: File): ResponseData<String?> {
        val formData = formData {
            // 添加文件
            append("file", file.readBytes(), Headers.build {
                append(HttpHeaders.ContentType, ContentType.Application.OctetStream)
                append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
            })

        }
        return RequestHttp.uploadFile(path = "/file/upload", formData)
    }

    suspend fun uploadFileChunk(
        fileChunk: File,
        chunkIndex: Int,
        totalChunks: Int,
        fileName: String,
        onProgress: (
            bytesSent: Long,         // 当前分片已发送字节
            chunkTotal: Long,        // 当前分片总大小
            globalSent: Long,        // 全局已发送字节（累计）
            globalTotal: Long        // 全局总大小
        ) -> Unit = { _, _, _, _ -> }
    ): ResponseData<String?> {
        return RequestHttp.uploadFileChunk(
            path = "/file/file-chunk",
            fileChunk,
            chunkIndex,
            totalChunks,
            fileName,
            onProgress,
        )
    }

    suspend fun mergeFileChunks(
        filePrefix: String,
        totalChunks: Int,
        fileName: String,

        ): ResponseData<String?> {

        val formData = Parameters.build {
            append("filePrefix", filePrefix)
            append("totalChunks", totalChunks.toString())
            append("fileName", fileName)
        }
        return RequestHttp.postForm(path = "/file/merge", formData)
    }
}