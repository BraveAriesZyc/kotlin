package com.zyc.db.database.dao

import com.zyc.db.database.DatabaseInitialize

class FriendDao(
    private val database: DatabaseInitialize
) {
    val query = database.friendsQuery
}
