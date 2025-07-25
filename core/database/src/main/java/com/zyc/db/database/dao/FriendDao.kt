package com.zyc.db.database.dao

import com.zyc.db.database.DatabaseInitialize
import com.zyc.db.database.entity.FriendEntity

class FriendDao(
    private val database: DatabaseInitialize
) {
    val query = database.friendsQuery


}
