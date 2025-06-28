package com.zyc.db.database.entity


import kotlinx.serialization.Serializable


@Serializable
data class MessageEntity(
    val id: Long = 0,
    val uid: String,
    val type: String,
    val content: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val audioUrl: String = "",
    val fileUrl: String = "",
    val role: String = "user",
    val sessionId : String = "",
    val timestamp: Long = System.currentTimeMillis()
)
