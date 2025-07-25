package com.zyc.core.model.entity

import kotlinx.serialization.Serializable

/**
 * 作品模型
 * 用于表示用户发布的作品内容
 */
@Serializable
data class WorkModel(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val type: Int, // 作品类型：1-穿搭教程，2-科技评测等
    val author: UserBrief, // 使用UserBrief作为作者信息
    val tags: List<String>,
    val coverImageUrl: String,
    val workList: List<String>, // 作品资源列表（图片、视频等）
    val createTime: Long,
    val updateTime: Long,
    val status: Int, // 状态：1-已发布，0-草稿等
    val viewCount: Long,
    val likeCount: Long,
    val commentCount: Long,
    val collectCount: Long
)

/**
 * 用户模型
 * 简化版本，用于向后兼容
 */
@Serializable
data class UserModel(
    val id: String = "",
    val username: String = "",
    val nickname: String = "",
    val avatar: String = ""
) {
    companion object {
        fun fromUserBrief(userBrief: UserBrief): UserModel {
            return UserModel(
                id = userBrief.userId,
                username = userBrief.username,
                nickname = userBrief.nickname ?: "",
                avatar = userBrief.avatar ?: ""
            )
        }
    }
}