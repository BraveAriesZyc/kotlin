package com.zyc.db.database

import com.zyc.common.AppConfig
import com.zyc.db.Database
import com.zyc.db.utils.db.DatabaseDriverFactory


class DatabaseInitialize(private val database: DatabaseDriverFactory) {
    private val db = Database(database.create(AppConfig.SQL_NAME))
    //    user
    val userQuery = db.userDbQueries
    val friendsQuery = db.friendDbQueries
    val messageQuery = db.messageDbQueries
}
