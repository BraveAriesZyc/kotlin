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
    private val _user = MutableStateFlow(
        UserModel(
            userName = "不思梦",
            uid = "99999999",
            avatar = "https://zhoudaxian.oss-rg-china-mainland.aliyuncs.com/avatar.jpg",
            age = 18,
            sex = "男"
        )
    )


    private val _friendList = MutableStateFlow(
        listOf(
            UserModel(
                userName = "⭐残乡碎梦🍀",
                uid = "1",
                avatar = "https://zhoudaxian.oss-rg-china-mainland.aliyuncs.com/avatar1.jpg",
                age = 18,
                sex = "male"
            ),
            UserModel(
                userName = "🍀不思梦❀",
                uid = "2",
                avatar = "https://zhoudaxian.oss-rg-china-mainland.aliyuncs.com/avatar1.jpg",
                age = 18,
                sex = "male"
            ),
        )
    )


    override val user: StateFlow<UserModel> = _user
    override fun getFriend(uid: String): UserModel {
        databaseRepository.userDao.insertUser( )


        val userList = databaseRepository.userDao.selectUser().map {
            UserModel(
                userName = it.userName,
                uid = it.uid,
                avatar = it.avatar,
                age = it.age,
                sex = it.sex
            )
        }
        Log.e("database", "$userList")

        return _friendList.value.first {
            it.uid == uid
        }
    }
    override fun initApp() {

    }
}
