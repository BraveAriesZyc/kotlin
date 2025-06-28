package com.zyc.clover.models.enums

enum class MessageType(val value: String) {
    TEXT("text"), // 文本
    IMAGE("image"), // 图片
    VIDEO("video"), // 视频
    AUDIO("audio"), // 音频
    FILE("file"), // 文件
    UNKNOWN("unknown") // 未知
}
