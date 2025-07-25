package com.zyc.db.database.dao

import com.zyc.db.database.DatabaseInitialize
import com.zyc.db.database.entity.UserEntity

class UserDao(
    private val database: DatabaseInitialize
) {
    private val query = database.userQuery


    @Throws(Exception::class)
    fun selectUser(): List<UserEntity> {
        return query.selectUser().executeAsList().map {
            UserEntity(
                userId = it.user_id,
                avatar = it.avatar,
                nickname = it.nickname,
                phone = it.phone,
                email = it.email,
                gender = it.gender?.toInt(),
                birthday = it.birthday,
                status = it.status?.toInt(),
                bio = it.bio,
                background = it.background,
                lastLoginIp = it.last_login_ip,
                lastLoginTime = it.last_login_time,
                createTime = it.create_time,
                updateTime = it.update_time,
            )
        }
    }

    @Throws(Exception::class)
    fun insertUser(user: UserEntity) {
        query.insertUser(
            user_id = user.userId,
            avatar = user.avatar,
            nickname = user.nickname,
            phone = user.phone,
            email = user.email,
            gender = user.gender?.toLong(),
            birthday = user.birthday,
            status = user.status?.toLong(),
            bio = user.bio,
            background = user.background,
            last_login_ip = user.lastLoginIp,
            last_login_time = user.lastLoginTime,
            create_time = user.createTime,
            update_time = user.updateTime,
        ).value
    }

    @Throws(Exception::class)
    fun updateUser(user: UserEntity): Long {
        return query.updateUser(
            user_id = user.userId,
            avatar = user.avatar,
            nickname = user.nickname,
            phone = user.phone,
            email = user.email,
            gender = user.gender?.toLong(),
            birthday = user.birthday,
            status = user.status?.toLong(),
            bio = user.bio,
            background = user.background,
            last_login_ip = user.lastLoginIp,
            last_login_time = user.lastLoginTime,
            update_time = user.updateTime,
        ).value
    }

    @Throws(Exception::class)
    fun removeUser(user: UserEntity): Long {
        return query.removeUser(
            user_id = user.userId
        ).value
    }

}
