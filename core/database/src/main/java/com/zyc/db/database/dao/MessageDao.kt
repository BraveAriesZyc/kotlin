package com.zyc.db.database.dao

import com.zyc.db.database.DatabaseInitialize
import com.zyc.db.database.entity.MessageEntity

class MessageDao(
    private val database: DatabaseInitialize
) {
    val query = database.messageQuery
}
