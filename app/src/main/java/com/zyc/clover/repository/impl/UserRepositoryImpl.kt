package com.zyc.clover.repository.impl

import com.zyc.clover.repository.UserRepository
import com.zyc.data.models.UserModel
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
    override fun getFriend(userId: String): UserModel? {
//        val userList = databaseRepository.userDao.selectUser().map {
//            UserModel(
//                userId = it.userId,
//                avatar = it.avatar,
//                nickname = it.nickname,
//                phone = it.phone,
//                email = it.email,
//                gender = it.gender,
//                birthday = it.birthday,
//                status = it.status,
//                bio = it.bio,
//                background = it.background,
//                lastLoginIp = it.lastLoginIp,
//                lastLoginTime = it.lastLoginTime,
//                createTime = it.createTime,
//                updateTime = it.updateTime,
//            )
//        }
//        Log.d("UserRepositoryImpl", "getFriend: $userList")
        return _friendList.value.firstOrNull {
            it.userId == userId
        }
    }

    override fun initApp() {
        _user.value = UserModel(
            userId = "1",
            avatar = "https://clover-blessing.oss-cn-beijing.aliyuncs.com/default/avatar2.jpg",
            nickname = "不思梦",
            phone = "19157972348",
            email = "1324853580@qq.com",
            gender = 0,
            birthday = "3.25",
            status = 0,
            bio = "暂无描述",
            background = "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover/mp4/d8bad1ad6e7b46ebba70e747d84db58b_Image_348220399764755.jpg",
            lastLoginIp = "it.lastLoginIp",
            lastLoginTime = "it.lastLoginTime",
            createTime = "it.createTime",
            updateTime = "it.updateTime",
        )

    }
}
