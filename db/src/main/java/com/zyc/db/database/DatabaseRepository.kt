package com.zyc.db.database

import com.zyc.db.database.dao.FriendDao
import com.zyc.db.database.dao.MessageDao
import com.zyc.db.database.dao.UserDao

interface DatabaseRepository {
    val userDao: UserDao
    val friendDao: FriendDao
    val messageDao: MessageDao
}


class DatabaseRepositoryImpl (
    private val databaseQuery: DatabaseInitialize
) : DatabaseRepository{
    override val userDao = UserDao(databaseQuery)
    override val friendDao = FriendDao(databaseQuery)
    override val messageDao = MessageDao(databaseQuery)

}
