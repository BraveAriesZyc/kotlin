package com.zyc.core.ui.utils.fileUtil

import com.zyc.core.common.AppConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File

data object FileUtil {

    private val uploadScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun uploadWholeFile(
        file: File,
        api: suspend (
            index: Int,
            totalChunks: Int,
            fileName: String,
            chunk: File
        ) -> Unit
    ) {
        uploadScope.launch {
            // 1. 切割文件（5MB 分片）
            val chunkSize = AppConfig.CHUNK_SIZE
            val chunks = fileChunk(file, chunkSize) // 切割文件
            val totalChunks = chunks.size // 总分片数

            // 2. 逐个上传分片
            chunks.forEachIndexed { index, chunkFile ->
                api(index, totalChunks, file.name, chunkFile)
            }
        }
    }


    private fun fileChunk(
        file: File,
        chunkSize: Long
    ): MutableList<File> {
        val chunks = mutableListOf<File>()
        val totalSize = file.length()
        var offset = 0L
        var index = 0

        while (offset < totalSize) {
            val currentChunkSize = minOf(chunkSize, totalSize - offset)
            val chunkFile = File(file.parent, "${file.name}.chunk_$index")

            // 写入分片数据
            file.inputStream().use { input ->
                input.skip(offset)
                chunkFile.outputStream().use { output ->
                    val buffer = ByteArray(currentChunkSize.toInt())
                    input.read(buffer)
                    output.write(buffer)
                }
            }

            chunks.add(chunkFile)
            offset += currentChunkSize
            index++
        }
        return chunks

    }

}