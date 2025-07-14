package com.zyc.clover.models

data class UserModel(
    var id : Long? = null,
    /**
     * 用户唯一标识
     */
    var userId: String? = null,

    /**
     * 用户昵称
     */
    var nickname: String? = null,

    /**
     * 手机号码
     */
    var phone: String? = null,

    /**
     * 邮箱地址
     */
    var email: String? = null,

    /**
     * 用户头像URL
     */
    var avatar: String? = null,

    /**
     * 背景图URL
     */
    var background: String? = null,

    /**
     * 性别：0-未知，1-男，2-女
     */
    var gender: Int? = null,

    /**
     * 生日
     */
    var birthday: String? = null,

    /**
     * 个人简介
     */
    var bio: String? = null,

    /**
     * 用户状态：0-正常，1-禁用，2-锁定
     */
    var status: Int? = null,

    /**
     * 最后登录时间
     */
    var lastLoginTime: String? = null,

    /**
     * 最后登录IP
     */
    var lastLoginIp: String? = null,

    /**
     * 用户密码（加密存储）
     * 使用加密算法存储的用户登录密码，不会在JSON序列化时返回
     */
    var password: String? = null,
    /**
     * 创建时间
     * 记录数据创建的时间，在插入时自动填充
     */
    var createTime: String? = null,
    /**
     * 更新时间
     * 记录数据最后更新的时间，在插入和更新时自动填充
     */

    var updateTime: String? = null
)

fun UserModel.toMap(): Map<String, String> {
    return mapOf(
        "id" to id.toString(),
        "userId" to userId.toString(),
        "nickname" to nickname.toString(),
        "password" to password.toString(),
        "phone" to phone.toString(),
        "email" to email.toString(),
        "avatar" to avatar.toString(),
        "background" to background.toString(),
        "gender" to gender.toString(),
        "birthday" to birthday.toString(),
        "bio" to bio.toString(),
        "status" to status.toString(),
        "lastLoginTime" to lastLoginTime.toString(),
    )
}
