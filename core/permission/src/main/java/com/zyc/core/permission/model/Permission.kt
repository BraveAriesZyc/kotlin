package com.zyc.core.permission.model

import android.Manifest

enum class Permission(
    val manifestPermission: String
) {
    // 相机
    CAMERA(Manifest.permission.CAMERA),

    // 麦克风
    MICROPHONE(Manifest.permission.RECORD_AUDIO),

    // 粗略位置
    COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),

    // 精确位置
    FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),

    // 后台位置（Android 10+）
    BACKGROUND_LOCATION(Manifest.permission.ACCESS_BACKGROUND_LOCATION),

    // 存储 - 读取外部存储
    READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE),

    // 存储 - 写入外部存储（不同 Android 版本权限情况有变化）
    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),

    // 电话 - 读取电话状态
    READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE),

    // 电话 - 拨打电话
    CALL_PHONE(Manifest.permission.CALL_PHONE),

    // 通讯录 - 读取联系人
    READ_CONTACTS(Manifest.permission.READ_CONTACTS),

    // 通讯录 - 写入联系人
    WRITE_CONTACTS(Manifest.permission.WRITE_CONTACTS),

    // 短信 - 发送短信
    SEND_SMS(Manifest.permission.SEND_SMS),

    // 短信 - 接收短信
    RECEIVE_SMS(Manifest.permission.RECEIVE_SMS),

    // 日历 - 读取日历
    READ_CALENDAR(Manifest.permission.READ_CALENDAR),

    // 日历 - 写入日历
    WRITE_CALENDAR(Manifest.permission.WRITE_CALENDAR),

    // 传感器 - 身体传感器（Android 4.4W+）
    BODY_SENSORS(Manifest.permission.BODY_SENSORS),

    // 通知（Android 13+）
    POST_NOTIFICATIONS(Manifest.permission.POST_NOTIFICATIONS),
    //振动

    VIBRATE(Manifest.permission.VIBRATE)
    // 其他
}
