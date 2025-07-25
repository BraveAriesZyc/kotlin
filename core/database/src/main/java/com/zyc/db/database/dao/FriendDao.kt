package com.zyc.db.database.dao

import com.zyc.db.database.DatabaseInitialize
import com.zyc.db.database.entity.FriendEntity

class FriendDao(
    private val database: DatabaseInitialize
) {
    val query = database.friendsQuery

    fun selectFriend(): List<FriendEntity> {
        return query.selectFrinds().executeAsList().map {it->
            FriendEntity(
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
}
