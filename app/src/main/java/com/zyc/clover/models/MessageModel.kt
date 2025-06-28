package com.zyc.clover.models

import com.zyc.clover.models.enums.MessageType
import com.zyc.clover.models.enums.Role



data class MessageModel(
    val id: Long = 0,
    val uid: String,
    val type: String = MessageType.TEXT.value,
    val content: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val audioUrl: String = "",
    val fileUrl: String = "",
    val role: String = Role.USER.value,
    val sessionId : String = "",
    val timestamp: Long = System.currentTimeMillis()
)
