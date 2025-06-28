package com.zyc.db.database.dao

import com.zyc.db.database.DatabaseInitialize
import com.zyc.db.database.entity.UserEntity

class UserDao(
    private val database: DatabaseInitialize
) {
  private  val query = database.userQuery


    @Throws(Exception::class)
    fun selectUser(): List<UserEntity> {
      return  query.selectUser().executeAsList().map {
          UserEntity(
              uid = it.uid,
              userName = it.userName,
              avatar = it.avatar,
              age = it.age.toInt(),
              sex = it.sex
          )
        }
    }

    @Throws(Exception::class)
    fun insertUser() {
        query.insertUser(
            uid = "1",
            userName = "不思梦",
            avatar = "https://zhoudaxian.oss-rg-china-mainland.aliyuncs.com/avatar.jpg",
            age = 18,
            sex = "男"
        ).value
    }

    @Throws(Exception::class)
    fun updateUser(user: UserEntity): Long {
        return query.updateUser(
            uid = user.uid,
            userName = user.userName,
            avatar = user.avatar,
            age = user.age.toLong(),
            sex = user.sex
        ).value
    }

    @Throws(Exception::class)
    fun removeUser(user: UserEntity): Long {
        return query.removeUser(
            uid = user.uid
        ).value
    }

}
