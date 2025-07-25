package com.zyc.db.database.entity

import kotlinx.serialization.Serializable
import com.zyc.core.model.entity.User
import com.zyc.core.model.entity.Gender
import com.zyc.core.model.entity.UserStatus

@Serializable
data class UserEntity(
    val id: Long = 0,
    val userId: String,
    val username: String,
    val email: String?,
    val phone: String?,
    val avatar: String?,
    val nickname: String?,
    val gender: Int = 0, // 对应 Gender.value
    val birthday: String?,
    val signature: String?,
    val background: String?,
    val status: Int = 0, // 对应 UserStatus.value
    val isOnline: Boolean = false,
    val lastLoginTime: Long?,
    val lastLoginIp: String?,
    val password: String?, // 本地可能不存储密码
    val createTime: Long,
    val updateTime: Long
) {
    fun toUser(): User {
        return User(
            id = id,
            userId = userId,
            username = username,
            email = email,
            phone = phone,
            avatar = avatar,
            nickname = nickname,
            gender = Gender.values().find { it.value == gender } ?: Gender.UNKNOWN,
            birthday = birthday,
            signature = signature,
            background = background,
            status = UserStatus.values().find { it.value == status } ?: UserStatus.NORMAL,
            isOnline = isOnline,
            lastLoginTime = lastLoginTime,
            lastLoginIp = lastLoginIp,
            createTime = createTime,
            updateTime = updateTime
        )
    }

    companion object {
        fun fromUser(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                userId = user.userId,
                username = user.username,
                email = user.email,
                phone = user.phone,
                avatar = user.avatar,
                nickname = user.nickname,
                gender = user.gender.value,
                birthday = user.birthday,
                signature = user.signature,
                background = user.background,
                status = user.status.value,
                isOnline = user.isOnline,
                lastLoginTime = user.lastLoginTime,
                lastLoginIp = user.lastLoginIp,
                password = null, // 不存储密码
                createTime = user.createTime,
                updateTime = user.updateTime
            )
        }
    }
}
