package com.zyc.core.data.repository.impl

import com.zyc.core.data.repository.AuthRepository
import com.zyc.core.model.entity.User
import com.zyc.core.network.ResponseData
import com.zyc.core.network.api.LoginApi

/**
 * 认证数据仓库实现类
 */
class AuthRepositoryImpl : AuthRepository {

    override suspend fun login(phone: String, password: String): ResponseData<String> {
        val user = User(
            id = 0,
            userId = "",
            username = phone,
            phone = phone,
            createTime = System.currentTimeMillis(),
            updateTime = System.currentTimeMillis()
        )
        return LoginApi.login(user)
    }

    override suspend fun register(phone: String, password: String): ResponseData<String> {
        val user = User(
            id = 0,
            userId = "",
            username = phone,
            phone = phone,
            createTime = System.currentTimeMillis(),
            updateTime = System.currentTimeMillis()
        )
        return LoginApi.register(user)
    }

    override suspend fun logout() {
        // 实现退出登录逻辑
        // 清除本地存储的token等
    }

    override suspend fun refreshToken(): ResponseData<String> {
        // 实现token刷新逻辑
        return ResponseData(
            code = 200,
            message = "Token refreshed",
            data = "new_token"
        )
    }

    override suspend fun isLoggedIn(): Boolean {
        // 检查本地是否有有效的token
        return false
    }
}
