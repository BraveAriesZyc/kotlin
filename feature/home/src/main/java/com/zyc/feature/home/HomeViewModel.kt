package com.zyc.feature.home

import androidx.lifecycle.ViewModel
import com.zyc.core.model.entity.UserBrief
import com.zyc.core.model.entity.WorkModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _topicList = MutableStateFlow(

        listOf(
            WorkModel(
                id = "work_001", // 唯一ID，避免重复
                title = "夏日清新穿搭指南",
                description = "分享5套适合25-30岁女性的夏日通勤穿搭，舒适又时尚",
                content = "炎热的夏天如何穿得既凉爽又不失专业感？本文将为你推荐5套...", // 内容摘要
                type = 1, // 假设1代表"穿搭教程"类型
                author = UserBrief(
                    id = 1,
                    userId = "user_001",
                    username = "时尚达人",
                    nickname = "小美",
                    avatar = "https://picsum.photos/id/64/100/100"
                ),
                tags = listOf("穿搭", "夏日", "女性", "通勤"),
                coverImageUrl = "https://picsum.photos/id/91/800/450", // 封面图
                workList = listOf(
                    "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover/mp4/68df0d831136424d9edc29d35af4b18a_share_f15cefc2f4044dbff41cee8b5ae05f901737388709083.mp4"
                ),
                createTime = 1688121600000, // 2023-07-01 00:00:00的时间戳
                updateTime = 1688125200000, // 发布后30分钟更新过
                status = 1, // 假设1代表"已发布"状态
                viewCount = 12563,
                likeCount = 892,
                commentCount = 156,
                collectCount = 328
            ),
            WorkModel(
                id = "work_002",
                title = "2023年最值得入手的5款科技产品",
                description = "从智能手机到智能家居设备，盘点今年性价比最高的科技产品",
                content = "随着科技的快速发展，市场上涌现出许多创新产品...", // 内容摘要
                type = 2, // 假设2代表"科技评测"类型
                workList = listOf(
                    "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover/mp4/99ab0d5f93084bfba331e154237a1028_share_c496caba37fb9ec14cab7bea1ef07ac51737333831803.mp4"
                ),
                author = UserBrief(
                    id = 2,
                    userId = "user_002",
                    username = "科技评测师",
                    nickname = "数码小王",
                    avatar = "https://picsum.photos/id/65/100/100"
                ),
                tags = listOf("科技", "产品评测", "2023", "数码"),
                coverImageUrl = "https://picsum.photos/id/1/800/450", // 封面图
                createTime = 1688553600000, // 2023-07-06 00:00:00的时间戳
                updateTime = 1688553600000, // 未更新过
                status = 1, // 已发布
                viewCount = 28745,
                likeCount = 2156,
                commentCount = 432,
                collectCount = 1254
            ),
            WorkModel(
                id = "work_003",
                title = "2023年最值得入手的5款科技产品",
                description = "从智能手机到智能家居设备，盘点今年性价比最高的科技产品",
                content = "随着科技的快速发展，市场上涌现出许多创新产品...", // 内容摘要
                type = 2, // 假设2代表"科技评测"类型
                workList = listOf(
                    "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover/mp4/eab6d98c001847279f279b80051f92fb_share_7a37e06a964640c3c5402de5ba3150921737351199641.mp4"
                ),
                author = UserBrief(
                    id = 3,
                    userId = "user_003",
                    username = "科技爱好者",
                    nickname = "小李",
                    avatar = "https://picsum.photos/id/66/100/100"
                ),
                tags = listOf("科技", "产品评测", "2023", "数码"),
                coverImageUrl = "https://picsum.photos/id/1/800/450", // 封面图
                createTime = 1688553600000, // 2023-07-06 00:00:00的时间戳
                updateTime = 1688553600000, // 未更新过
                status = 1, // 已发布
                viewCount = 28745,
                likeCount = 2156,
                commentCount = 432,
                collectCount = 1254
            ),
            WorkModel(
                id = "work_004",
                title = "2023年最值得入手的5款科技产品",
                description = "从智能手机到智能家居设备，盘点今年性价比最高的科技产品",
                content = "随着科技的快速发展，市场上涌现出许多创新产品...", // 内容摘要
                type = 2, // 假设2代表"科技评测"类型
                workList = listOf(
                    "https://clover-blessing.oss-cn-beijing.aliyuncs.com/clover/mp4/WeChat_20250709135840.mp4"
                ),
                author = UserBrief(
                    id = 4,
                    userId = "user_004",
                    username = "数码达人",
                    nickname = "小张",
                    avatar = "https://picsum.photos/id/67/100/100"
                ),
                tags = listOf("科技", "产品评测", "2023", "数码"),
                coverImageUrl = "https://picsum.photos/id/1/800/450", // 封面图
                createTime = 1688553600000, // 2023-07-06 00:00:00的时间戳
                updateTime = 1688553600000, // 未更新过
                status = 1, // 已发布
                viewCount = 28745,
                likeCount = 2156,
                commentCount = 432,
                collectCount = 1254
            )
        )

    )
    val topicList: StateFlow<List<WorkModel>> = _topicList


    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

}