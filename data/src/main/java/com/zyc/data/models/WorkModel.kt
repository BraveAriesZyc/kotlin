package com.zyc.data.models

import com.zyc.data.models.enums.WorkStatusEnum
import com.zyc.data.models.enums.WorkTypeEnum

data class WorkModel(
    // 作品唯一标识（如数据库ID）
    val id: String,
    // 作品标题
    val title: String,
    // 作品简介（简短描述）
    val description: String? = null,
    // 作品内容（根据类型存储不同数据，如文本、图片URL、视频地址等）
    val content: String,
    // 作品类型（如"article"、"design"、"video"、"painting"等）
    val type: Int = WorkTypeEnum.VIDEO.value,
    // 作者信息（可关联用户对象，此处简化为ID和名称）
    val author: UserModel,
    // 作品分类标签（如"科技"、"插画"、"短视频"）
    val tags: List<String> = emptyList(),
    // 作品封面图URL（可选）
    val coverImageUrl: String? = null,
    // 作品连接
    val workList: List<String> = emptyList(),
    // 创作时间
    val createTime: Long = System.currentTimeMillis(),
    // 最后修改时间（默认与创建时间一致）
    val updateTime:  Long = System.currentTimeMillis(),
    // 作品状态（如"draft"草稿、"published"已发布、"hidden"隐藏）
    val status: Int = WorkStatusEnum.DRAFT.value,
    // 阅读/浏览量
    val viewCount: Int = 0,
    // 点赞数
    val likeCount: Int = 0,
    // 评论数
    val commentCount: Int = 0,
    // 收藏数
    val collectCount: Int = 0
)
