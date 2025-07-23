package com.zyc.data.models

import com.zyc.data.models.enums.MessageType
import com.zyc.data.models.enums.Role


data class MessageModel(
    val id: Long = 0,
    val userId: String,
    val type: String = MessageType.TEXT.value,
    val content: String? = "",
    val imageUrl: String? = "",
    val videoUrl: String? = "",
    val audioUrl: String? = "",
    val fileUrl: String? = "",
    val role: String = Role.USER.value,
    val sessionId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

fun MessageModel.toMap(): Map<String, String> {
    return mapOf(
        "id" to id.toString(),
        "userId" to userId,
        "type" to type,
        "content" to content.toString(),
        "imageUrl" to imageUrl.toString(),
        "videoUrl" to videoUrl.toString(),
        "audioUrl" to audioUrl.toString(),
        "fileUrl" to fileUrl.toString(),
        "role" to role,
        "sessionId" to sessionId,
        "timestamp" to timestamp.toString()
    )
}
