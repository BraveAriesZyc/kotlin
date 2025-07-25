package com.zyc.core.data.repository

import com.zyc.core.network.ResponseData

/**
 * 认证数据仓库接口
 */
interface AuthRepository {

    /**
     * 用户登录
     */
    suspend fun login(phone: String, password: String): ResponseData<String>

    /**
     * 用户注册
     */
    suspend fun register(phone: String, password: String): ResponseData<String>

    /**
     * 退出登录
     */
    suspend fun logout()

    /**
     * 刷新token
     */
    suspend fun refreshToken(): ResponseData<String>

    /**
     * 检查登录状态
     */
    suspend fun isLoggedIn(): Boolean
}
