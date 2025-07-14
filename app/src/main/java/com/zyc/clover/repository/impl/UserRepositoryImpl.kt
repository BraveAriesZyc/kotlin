package com.zyc.clover.repository.impl

import android.util.Log
import com.zyc.clover.models.UserModel
import com.zyc.clover.repository.UserRepository
import com.zyc.db.database.DatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// 用户仓库实现类
class UserRepositoryImpl(
    private val databaseRepository: DatabaseRepository
) : UserRepository {
    private val _user = MutableStateFlow<UserModel>(UserModel())


    private val _friendList = MutableStateFlow<List<UserModel>>(emptyList())


    override val user: StateFlow<UserModel> = _user
    override fun getFriend(userId: String): UserModel {
        val userList = databaseRepository.userDao.selectUser().map {
            UserModel(
                userId = it.userId,
                avatar = it.avatar,
                nickname = it.nickname,
                phone = it.phone,
                email = it.email,
                gender = it.gender,
                birthday = it.birthday,
                status = it.status,
                bio = it.bio,
                background = it.background,
                lastLoginIp = it.lastLoginIp,
                lastLoginTime = it.lastLoginTime,
                createTime = it.createTime,
                updateTime = it.updateTime,
            )
        }
        return _friendList.value.first {
            it.userId == userId
        }
    }

    override fun initApp() {

    }
}
