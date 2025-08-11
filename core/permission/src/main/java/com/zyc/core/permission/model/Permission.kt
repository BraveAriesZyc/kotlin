package com.zyc.core.permission.model

import android.Manifest

// 权限枚举
enum class Permission(
    val manifestPermission: String,
    val description: String
) {
    // 相机
    CAMERA(Manifest.permission.CAMERA, "相机权限"),

    // 麦克风
    MICROPHONE(Manifest.permission.RECORD_AUDIO, "麦克风权限"),

    // 粗略位置
    COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION, "粗略位置权限"),

    // 精确位置
    FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION, "精确位置权限"),

    // 后台位置（Android 10+）
    BACKGROUND_LOCATION(Manifest.permission.ACCESS_BACKGROUND_LOCATION, "后台位置权限"),

    // 存储 - 读取外部存储
    READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE, "读取外部存储权限"),

    // 存储 - 写入外部存储（不同 Android 版本权限情况有变化）
    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入外部存储权限"),

    // 电话 - 读取电话状态
    READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE, "读取电话状态权限"),

    // 电话 - 拨打电话
    CALL_PHONE(Manifest.permission.CALL_PHONE, "拨打电话权限"),

    // 通讯录 - 读取联系人
    READ_CONTACTS(Manifest.permission.READ_CONTACTS, "读取通讯录权限"),

    // 通讯录 - 写入联系人
    WRITE_CONTACTS(Manifest.permission.WRITE_CONTACTS, "写入通讯录权限"),

    // 短信 - 发送短信
    SEND_SMS(Manifest.permission.SEND_SMS, "发送短信权限"),

    // 短信 - 接收短信
    RECEIVE_SMS(Manifest.permission.RECEIVE_SMS, "接收短信权限"),

    // 日历 - 读取日历
    READ_CALENDAR(Manifest.permission.READ_CALENDAR, "读取日历权限"),

    // 日历 - 写入日历
    WRITE_CALENDAR(Manifest.permission.WRITE_CALENDAR, "写入日历权限"),

    // 传感器 - 身体传感器（Android 4.4W+）
    BODY_SENSORS(Manifest.permission.BODY_SENSORS, "身体传感器权限"),

    // 通知（Android 13+）
    POST_NOTIFICATIONS(Manifest.permission.POST_NOTIFICATIONS, "通知权限"),
    //振动

    VIBRATE(Manifest.permission.VIBRATE, "振动权限")
    // 其他
}



// 定义权限状态枚举
enum class PermissionStatus(
    val description: String
) {
    GRANTED("同意"),         // 同意
    TEMPORARILY_DENIED("临时拒绝"),  // 临时拒绝
    PERMANENTLY_DENIED("永久拒绝")   // 永久拒绝
}
