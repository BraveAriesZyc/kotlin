package com.zyc.clover.api

import com.zyc.clover.utils.network.RequestHttp
import com.zyc.clover.utils.network.ResponseData


object LoginApi {
     suspend fun login(phone: String, password: String): ResponseData<String> {
        return RequestHttp.post("/auth/login", mapOf("phone" to phone, "password" to password))
    }
     suspend fun  register(phone: String, password: String): ResponseData<String>{
        return RequestHttp.post("/auth/register", mapOf("phone" to phone, "password" to password))
    }
}
