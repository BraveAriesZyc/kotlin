package com.zyc.clover.api

import com.zyc.clover.utils.network.RequestHttp
import com.zyc.clover.utils.network.ResponseData

class LoginApi {
    suspend fun login(username: String, password: String): ResponseData<String> {
        return RequestHttp.post("login")
    }
}
