package com.zyc.clover.repository


import com.zyc.data.models.UserModel
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val user : StateFlow<UserModel> // 用户信息
    fun getFriend(userId: String): UserModel?  // 获取 好友信息
    fun initApp()
}
