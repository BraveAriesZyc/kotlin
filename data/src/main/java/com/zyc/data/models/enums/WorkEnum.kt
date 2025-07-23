package com.zyc.data.models.enums

enum class WorkStatusEnum(val value: Int) {
    //已发布
    RELEASED(0),

    //草稿
    DRAFT(1),

    //删除
    DELETED(2),

    //禁用
    DISABLED(3),
}

enum class WorkTypeEnum(val value: Int) {
    //图片
    IMAGE(0),

    //视频
    VIDEO(1),
}
