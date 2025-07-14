package com.zyc.clover.api

import com.zyc.clover.models.UserModel
import com.zyc.clover.models.toMap
import com.zyc.clover.utils.network.RequestHttp
import com.zyc.clover.utils.network.ResponseData


object LoginApi {
     suspend fun login(user: UserModel): ResponseData<String> {
        return RequestHttp.post("/auth/login", user)
    }
     suspend fun  register(user: UserModel): ResponseData<String>{
        return RequestHttp.post("/auth/register",user.toMap())
    }
}
