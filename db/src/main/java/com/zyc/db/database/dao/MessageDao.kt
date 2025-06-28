package com.zyc.db.database.dao

import com.zyc.db.database.DatabaseInitialize
import com.zyc.db.database.entity.MessageEntity

class MessageDao(
    private val database: DatabaseInitialize
) {
    val query = database.messageQuery

    fun selectMessage(): List<MessageEntity> {
        return query.selectAllMessages().executeAsList().map {
            MessageEntity(
                id = it.id,
                uid = it.uid,
                type = it.type,
                content = it.content,
                imageUrl = it.imageUrl,
                videoUrl = it.videoUrl,
                audioUrl = it.audioUrl,
                fileUrl = it.fileUrl,
                role = it.role,
                sessionId = it.sessionId,
            )
        }
    }
    fun insertMessage(message: MessageEntity): Long {
        return query.insertMessage(
            uid = message.uid,
            type = message.type,
            content = message.content,
            imageUrl = message.imageUrl,
            videoUrl = message.videoUrl,
            audioUrl = message.audioUrl,
            fileUrl = message.fileUrl,
            role = message.role,
            sessionId = message.sessionId,
            timestamp = message.timestamp
        ).value
    }
}
