package com.zyc.db.database.entity

import kotlinx.serialization.Serializable

@Serializable
data class FriendEntity(
    val id: Int,
    val userId: Int,
    val userName: String,
    val isChat: Long = 0,
    val avatar: String,
)
