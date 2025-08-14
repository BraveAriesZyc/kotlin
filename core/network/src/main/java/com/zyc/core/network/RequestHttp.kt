package com.zyc.core.network

import android.util.Log
import com.zyc.core.common.AppConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.net.SocketException

/**
 * HTTP请求工具类
 */
object RequestHttp {
    // 基础URL配置，可根据环境切换
    const val BASE_URL = "http://192.168.100.100:9000/api"

    // 初始化HTTP客户端
    val httpClient: HttpClient by lazy { createHttpClient() }

    /**
     * 创建HTTP客户端配置
     */
    private fun createHttpClient(engine: HttpClientEngine? = null): HttpClient {
        val client = if (engine != null) HttpClient(engine) else HttpClient()

        return client.config {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true // 忽略JSON中未知的字段
                })
            }

            install(Logging) {
                level = LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("HttpClient", message) // 使用Android的Log系统
                    }
                }
            }

            defaultRequest {

                contentType(ContentType.Application.Json)
            }

            expectSuccess = false // 允许处理非200状态码

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 30000
            }

// 重试策略
            install(HttpRequestRetry) {
                maxRetries = 2
                // 方案 1.1：指定单个状态码（如果只需重试特定码）
                // retryOnServerErrors(500, 502, 503, 504) // 列举需要重试的5xx状态码

                // 方案 1.2：通过 lambda 判断状态码范围（更灵活，推荐）
                retryIf { request, response ->
                    response.status.value in 500..599 // 对所有5xx服务器错误重试
                }

                retryOnExceptionIf { _, exception ->
                    exception is SocketException
                }
                delayMillis { retry -> retry * 1000L } // 指数退避策略（第1次重试延迟1s，第2次2s）
            }
        }
    }

    /**
     * 发送GET请求
     * @param path 接口路径
     * @param params 查询参数
     * @return 响应数据
     */
    suspend inline fun <reified T> get(
        path: String,
        params: Map<String, Any>? = null
    ): ResponseData<T> = executeRequest {
        httpClient.get(BASE_URL + path) {
            params?.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }

    /**
     * 发送POST请求（JSON格式）
     * @param path 接口路径
     * @param data 请求体数据
     * @return 响应数据
     */
    suspend inline fun <reified T> post(
        path: String,
        data: Any? = null
    ): ResponseData<T> = executeRequest {
        httpClient.post(BASE_URL + path) {
            data?.let { setBody(it) }
        }
    }

    /**
     * 发送POST请求（表单格式）
     * @param path 接口路径
     * @param formData 表单数据
     * @return 响应数据
     */
    suspend inline fun <reified T> postForm(
        path: String,
        formData: Parameters
    ): ResponseData<T> = executeRequest {
        httpClient.post(BASE_URL + path) {
            // 1. 明确表单内容类型（必须）
            contentType(ContentType.Application.FormUrlEncoded)
            // 2. 关键修复：用FormDataContent包装Parameters
            setBody(FormDataContent(formData))
        }
    }


    /**
     * 上传文件
     * @param path 接口路径
     * @return 响应数据
     */
    suspend inline fun <reified T> uploadFile(
        path: String,
        formData: List<PartData>
    ): ResponseData<T> = executeRequest {

        httpClient.post(BASE_URL + path) {

            // 核心修正：使用 multipart/form-data 类型（支持文件和文本混合）
            contentType(ContentType.MultiPart.FormData)
            // 正确设置多部分表单数据
            setBody(MultiPartFormDataContent(formData))
        }

    }

    /**
     * 上传文件分片到服务器
     *
     * @param T 响应数据的类型，通过 reified 关键字支持泛型类型推断
     * @param path 上传接口的路径（BASE_URL 之后的部分）
     * @param fileChunk 当前要上传的文件分片
     * @param chunkIndex 当前分片的索引（从 0 开始）
     * @param totalChunks 文件总分片数量
     * @param fileName 原始文件的名称
     * @param onProgress 上传进度回调函数
     *                   - bytesSent: 当前分片已发送的字节数
     *                   - chunkTotal: 当前分片的总大小（字节）
     *                   - globalSent: 所有分片累计已发送的字节数
     *                   - globalTotal: 文件的总大小（字节）
     * @return 服务器返回的响应数据，类型为 ResponseData<T>
     */
    suspend inline fun <reified T> uploadFileChunk(
        path: String,
        fileChunk: File,
        chunkIndex: Int,
        totalChunks: Int,
        fileName: String,
        crossinline onProgress: (
            bytesSent: Long,
            chunkTotal: Long,
            globalSent: Long,
            globalTotal: Long
        ) -> Unit = { _, _, _, _ -> }  // 默认空实现，避免必须传参
    ): ResponseData<T> = executeRequest {
        // 创建表单数据列表，用于存放所有上传参数
        val formData = mutableListOf<PartData>()

        // 获取当前分片的大小（字节）
        val currentChunkSize = fileChunk.length()

        // 计算当前分片之前所有分片的总大小（用于计算全局进度）
        val bytesSentBeforeThisChunk = chunkIndex * AppConfig.CHUNK_SIZE

        // 计算文件的总大小
        val globalTotal = if (chunkIndex == totalChunks - 1) {
            // 最后一个分片：总大小 = 前面所有标准分片大小 + 当前分片实际大小
            bytesSentBeforeThisChunk + currentChunkSize
        } else {
            // 非最后一个分片：总大小 = 总分片数 * 标准分片大小（估算值）
            totalChunks * AppConfig.CHUNK_SIZE
        }

        // 添加文件分片到表单数据
        formData.add(
            PartData.FileItem(
                // 提供文件分片的输入流，转换为 Ktor 所需的 ByteReadChannel
                provider = {
                    fileChunk.inputStream().buffered().toByteReadChannel()
                },
                // 清理资源的回调（Ktor 会自动处理流关闭，这里留空即可）
                dispose = {},
                // 设置文件分片的请求头
                partHeaders = Headers.build {
                    // 指定内容类型为二进制流
                    append(HttpHeaders.ContentType, ContentType.Application.OctetStream)
                    // 设置表单字段名和文件名
                    append(
                        HttpHeaders.ContentDisposition,
                        "form-data; name=\"file\"; filename=\"${fileChunk.name}\""
                    )
                    // 设置当前分片的大小
                    append(HttpHeaders.ContentLength, currentChunkSize.toString())
                }
            )
        )

        // 添加当前分片索引到表单数据
        formData.add(
            PartData.FormItem(
                value = chunkIndex.toString(),
                dispose = {},
                partHeaders = Headers.build {
                    append(HttpHeaders.ContentDisposition, "form-data; name=\"chunkIndex\"")
                }
            )
        )

        // 添加总分片数量到表单数据
        formData.add(
            PartData.FormItem(
                value = totalChunks.toString(),
                dispose = {},
                partHeaders = Headers.build {
                    append(HttpHeaders.ContentDisposition, "form-data; name=\"totalChunks\"")
                }
            )
        )

        // 添加原始文件名到表单数据
        formData.add(
            PartData.FormItem(
                value = fileName,
                dispose = {},
                partHeaders = Headers.build {
                    append(HttpHeaders.ContentDisposition, "form-data; name=\"fileName\"")
                }
            )
        )

        // 发送 POST 请求上传分片
        httpClient.post(BASE_URL + path) {
            // 设置内容类型为多表单数据
            contentType(ContentType.MultiPart.FormData)
            // 设置请求体为多表单数据
            setBody(MultiPartFormDataContent(formData))
            // 监听上传进度
            onUpload { bytesSent, _ ->
                // 确保已发送字节数不超过当前分片总大小（防止进度超过100%）
                val safeBytesSent = bytesSent.coerceAtMost(currentChunkSize)

                // 计算全局已发送字节数（之前分片总大小 + 当前分片已发送大小）
                val globalSent = bytesSentBeforeThisChunk + safeBytesSent

                // 触发进度回调，通知外部进度更新
                onProgress(safeBytesSent, currentChunkSize, globalSent, globalTotal)
                //  当前分片文件上传进度
                val chunkProgress = (safeBytesSent.toFloat() / currentChunkSize * 100).toInt()
                //  整个文件上传进度
                val globalProgress = (globalSent.toFloat() / globalTotal * 100).toInt()
            }
        }
    }

    /**
     * 上传多个文件
     * @param path 接口路径
     * @param files 文件列表
     * @param paramName 服务器接收文件的参数名
     * @param extraParams 额外的表单参数
     * @return 响应数据
     */
    suspend inline fun <reified T> uploadFiles(
        path: String,
        files: List<File>,
        paramName: String = "files",
        extraParams: Map<String, String>? = null
    ): ResponseData<T> = executeRequest {
        httpClient.submitFormWithBinaryData(
            url = BASE_URL + path,
            formData = formData {
                // 添加多个文件
                files.forEachIndexed { index, file ->
                    val fileName = "${file.nameWithoutExtension}_$index.${file.extension}"
                    append(paramName, file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Application.OctetStream)
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                }

                // 添加额外参数
                extraParams?.forEach { (key, value) ->
                    append(key, value)
                }
            }
        )
    }

    /**
     * 发送PUT请求
     * @param path 接口路径
     * @param data 请求体数据
     * @return 响应数据
     */
    suspend inline fun <reified T> put(
        path: String,
        data: Any? = null
    ): ResponseData<T> = executeRequest {
        httpClient.put(BASE_URL + path) {
            data?.let { setBody(it) }
        }
    }

    /**
     * 发送DELETE请求
     * @param path 接口路径
     * @param params 查询参数
     * @return 响应数据
     */
    suspend inline fun <reified T> delete(
        path: String,
        params: Map<String, Any>? = null
    ): ResponseData<T> = executeRequest {
        httpClient.delete(BASE_URL + path) {
            params?.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }

    /**
     * 下载文件
     * @param path 接口路径
     * @param destination 目标文件
     * @return 是否下载成功
     */
    suspend fun downloadFile(
        path: String,
        destination: File
    ): Boolean {
        return try {
            val response = httpClient.get(BASE_URL + path)
            if (response.status.isSuccess()) {
                // 将ByteReadChannel转换为InputStream，再写入到文件输出流
                response.body<ByteReadChannel>().toInputStream().use { inputStream ->
                    destination.outputStream().buffered().use { outputStream ->
                        inputStream.copyTo(outputStream) // 直接复制流内容
                    }
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("Download", "下载失败: ${e.message}", e)
            false
        }
    }

    /**
     * 统一处理HTTP请求
     */
    suspend inline fun <reified T> executeRequest(
        crossinline block: suspend () -> HttpResponse
    ): ResponseData<T> {
        return try {

            val response = block()
            Log.d("executeRequest", "Response: ${response.request.url}")
            Log.d("executeRequest", "Response: ${response.request.headers}")
            logRequestInfo(response)

            // 处理HTTP状态码
            when (response.status.value) {
                in 200..299 -> {
                    // 尝试解析响应体
                    try {
                        response.body<ResponseData<T>>()
                    } catch (e: Exception) {
                        // 如果解析失败，返回自定义成功响应
                        ResponseData(
                            code = response.status.value,
                            message = "请求成功",
                            data = null
                        )
                    }
                }

                in 300..399 -> throw ApiException(
                    response.status.value,
                    "重定向: ${response.status.description}"
                )

                in 400..499 -> throw ApiException(
                    response.status.value,
                    "客户端错误: ${response.status.description}"
                )

                in 500..599 -> throw ApiException(
                    response.status.value,
                    "服务器错误: ${response.status.description}"
                )

                else -> throw ApiException(
                    response.status.value,
                    "未知状态码: ${response.status.description}"
                )
            }
        } catch (e: Exception) {
            Log.d("executeRequest", "Response: ${e.message}")
            handleRequestException(e)
        }
    }

    /**
     * 记录请求信息
     */
    fun logRequestInfo(response: HttpResponse) {
        val request = response.request
        Log.d("HTTP_REQUEST", "${request.method} ${request.url}")
        Log.d("HTTP_RESPONSE", "状态码: ${response.status.value} ${response.status.description}")
    }

    /**
     * 处理请求异常
     */
    inline fun <reified T> handleRequestException(e: Exception): ResponseData<T> {
        val apiException = when (e) {
            is SocketTimeoutException,
            is ConnectTimeoutException,
            is HttpRequestTimeoutException -> ApiException(408, "请求超时，请稍后重试")

            is ClientRequestException -> ApiException(
                e.response.status.value,
                "请求错误: ${e.response.status.description}"
            )

            is ServerResponseException -> ApiException(
                e.response.status.value,
                "服务器错误: ${e.response.status.description}"
            )

            is JsonConvertException -> ApiException(500, "数据解析错误")

            is NoTransformationFoundException -> ApiException(500, "不支持的数据格式")

            is ApiException -> e // 直接使用已有的ApiException

            else -> ApiException(500, "网络异常: ${e.message ?: "未知错误"}")
        }

        Log.e("HTTP_ERROR", "(${apiException.code}) ${apiException.message}", e)
        return ResponseData(apiException.code, apiException.message, null)
    }
}

/**
 * 自定义API异常类
 */
class ApiException(val code: Int, override val message: String) : Exception(message)

@Serializable
data class ResponseData<T>(
    val code: Int,
    val message: String,
    val data: T?
)
