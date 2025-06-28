package com.zyc.db.utils.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.zyc.db.Database


class DatabaseDriverFactory(
    private val context: Context
) {
    fun create(dbName: String): SqlDriver {
        return AndroidSqliteDriver(
            schema = Database.Schema,
            context = context,
            name = dbName
        )
    }

}
