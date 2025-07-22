package com.zyc.clover.api


import com.zyc.clover.utils.network.RequestHttp
import com.zyc.clover.utils.network.ResponseData
import com.zyc.data.models.UserModel
import com.zyc.data.models.toMap


object LoginApi {
     suspend fun login(user: UserModel): ResponseData<String> {
        return RequestHttp.post("/auth/login", user)
    }
     suspend fun  register(user: UserModel): ResponseData<String>{
        return RequestHttp.post("/auth/register",user.toMap())
    }
}
