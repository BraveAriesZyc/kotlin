package com.zyc.db.database.entity

import kotlinx.serialization.Serializable

// 实体
@Serializable
data class UserEntity(
    val uid: String,
    val userName: String,
    val avatar: String,
    val age: Int,
    val sex: String,
)
