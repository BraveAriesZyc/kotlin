package com.zyc.clover.utils.network

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * HTTP请求工具类
 */
object RequestHttp {
    const val BASE_URL = "http://192.168.10.2:9000/api"

    // 初始化HTTP客户端
    val httpClient: HttpClient = createHttpClient()

    private fun createHttpClient(engine: HttpClientEngine? = null): HttpClient {
        val httpClientConfig = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                level = LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HttpClient: $message")
                    }
                }
            }

            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
                // 可添加通用请求头
                // header("Authorization", "Bearer token")
            }

            expectSuccess = false // 允许处理非200状态码

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 30000
            }
        }

        return httpClientConfig
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
        httpClient.get("$BASE_URL$path"){
            params?.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }

    /**
     * 发送POST请求
     * @param path 接口路径
     * @param data 请求体数据
     * @return 响应数据
     */
    suspend inline fun <reified T> post(
        path: String,
        data: Any? = null
    ): ResponseData<T> = executeRequest {
        Log.i("RequestHttp", "POST请求: $path")
        httpClient.post("$BASE_URL$path"){
            data?.let { setBody(it) }
        }
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
        httpClient.put("$BASE_URL$path"){
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
        httpClient.delete("$BASE_URL$path"){
            params?.forEach { (key, value) ->
                parameter(key, value)
            }
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

            logRequestInfo(response)

            // 处理HTTP状态码
            val httpStatusCode = response.status.value
            if (httpStatusCode !in 200..299) {
                return ResponseData(
                    httpStatusCode,
                    "HTTP请求失败: ${response.status.description}",
                    data = null
                )
            }

            // 解析响应数据
            val responseData: ResponseData<T> = response.body()
            responseData
        } catch (e: Exception) {
            Log.e("RequestHttp", "HTTP请求异常: ${e.message}")
            handleRequestException(e)
        }
    }

    /**
     * 记录请求信息
     */
    fun logRequestInfo(response: HttpResponse) {
        val request = response.request
        println("HTTP请求: ${request.method} ${request.url.fullPath}")
        println("HTTP状态码: ${response.status.value} ${response.status.description}")
    }

    /**
     * 处理请求异常
     */
    inline fun <reified T> handleRequestException(e: Exception): ResponseData<T> {
        val apiException = when (e) {
            is SocketTimeoutException -> ApiException(408, "连接超时")
            is ConnectTimeoutException -> ApiException(408, "连接超时")
            is HttpRequestTimeoutException -> ApiException(408, "请求超时")
            is ClientRequestException -> ApiException(e.response.status.value, "客户端请求错误")
            is ServerResponseException -> ApiException(e.response.status.value, "服务器响应错误")
            is JsonConvertException -> ApiException(500, "JSON解析错误: ${e.message}")
            else -> ApiException(500, "网络请求异常: ${e.message}")
        }

        println("HTTP请求异常: ${apiException.code} ${apiException.message}")
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
    val msg: String,
    val data: T?
)


