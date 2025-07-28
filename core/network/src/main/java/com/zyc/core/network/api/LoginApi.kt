package com.zyc.core.network.api


import com.zyc.core.network.RequestHttp
import com.zyc.core.network.ResponseData

import com.zyc.core.model.entity.User
import com.zyc.core.model.entity.UserModel
import kotlinx.serialization.json.Json


object LoginApi {
     suspend fun login(user: UserModel): ResponseData<String> {
        return RequestHttp.post("/auth/login", Json.encodeToString(user))
    }
     suspend fun  register(user: User): ResponseData<String>{
        return RequestHttp.post("/auth/register",Json.encodeToString(user))
    }
}
