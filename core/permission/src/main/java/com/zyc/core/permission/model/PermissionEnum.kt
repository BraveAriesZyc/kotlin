package com.zyc.core.permission.model

import android.Manifest
import android.os.Build

/**
 * Android权限枚举
 * 包含所有常见的Android权限
 */
enum class PermissionEnum(
    val manifestPermission: String,
    val minSdkVersion: Int = 1
) {
    // ==================== 媒体权限 ====================
    CAMERA(Manifest.permission.CAMERA),
    RECORD_AUDIO(Manifest.permission.RECORD_AUDIO),
    
    // ==================== 位置权限 ====================
    ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    ACCESS_COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),
    ACCESS_BACKGROUND_LOCATION(Manifest.permission.ACCESS_BACKGROUND_LOCATION, Build.VERSION_CODES.Q),
    
    // ==================== 存储权限 ====================
    READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE),
    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
    MANAGE_EXTERNAL_STORAGE(Manifest.permission.MANAGE_EXTERNAL_STORAGE, Build.VERSION_CODES.R),
    
    // Android 13+ 媒体权限
    READ_MEDIA_IMAGES(Manifest.permission.READ_MEDIA_IMAGES, Build.VERSION_CODES.TIRAMISU),
    READ_MEDIA_VIDEO(Manifest.permission.READ_MEDIA_VIDEO, Build.VERSION_CODES.TIRAMISU),
    READ_MEDIA_AUDIO(Manifest.permission.READ_MEDIA_AUDIO, Build.VERSION_CODES.TIRAMISU),
    ACCESS_MEDIA_LOCATION(Manifest.permission.ACCESS_MEDIA_LOCATION, Build.VERSION_CODES.Q),
    
    // ==================== 通讯权限 ====================
    READ_CONTACTS(Manifest.permission.READ_CONTACTS),
    WRITE_CONTACTS(Manifest.permission.WRITE_CONTACTS),
    GET_ACCOUNTS(Manifest.permission.GET_ACCOUNTS),
    
    // 电话权限
    CALL_PHONE(Manifest.permission.CALL_PHONE),
    READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE),
    READ_PHONE_NUMBERS(Manifest.permission.READ_PHONE_NUMBERS, Build.VERSION_CODES.O),
    ANSWER_PHONE_CALLS(Manifest.permission.ANSWER_PHONE_CALLS, Build.VERSION_CODES.O),
    ADD_VOICEMAIL(Manifest.permission.ADD_VOICEMAIL, Build.VERSION_CODES.ICE_CREAM_SANDWICH),
    USE_SIP(Manifest.permission.USE_SIP, Build.VERSION_CODES.GINGERBREAD),
    
    // 短信权限
    SEND_SMS(Manifest.permission.SEND_SMS),
    READ_SMS(Manifest.permission.READ_SMS),
    RECEIVE_SMS(Manifest.permission.RECEIVE_SMS),
    RECEIVE_MMS(Manifest.permission.RECEIVE_MMS),
    RECEIVE_WAP_PUSH(Manifest.permission.RECEIVE_WAP_PUSH),
    
    // ==================== 日历权限 ====================
    READ_CALENDAR(Manifest.permission.READ_CALENDAR),
    WRITE_CALENDAR(Manifest.permission.WRITE_CALENDAR),
    
    // ==================== 传感器权限 ====================
    BODY_SENSORS(Manifest.permission.BODY_SENSORS, Build.VERSION_CODES.KITKAT_WATCH),
    BODY_SENSORS_BACKGROUND(Manifest.permission.BODY_SENSORS_BACKGROUND, Build.VERSION_CODES.TIRAMISU),
    ACTIVITY_RECOGNITION(Manifest.permission.ACTIVITY_RECOGNITION, Build.VERSION_CODES.Q),
    
    // ==================== 蓝牙权限 ====================
    BLUETOOTH(Manifest.permission.BLUETOOTH),
    BLUETOOTH_ADMIN(Manifest.permission.BLUETOOTH_ADMIN),
    BLUETOOTH_SCAN(Manifest.permission.BLUETOOTH_SCAN, Build.VERSION_CODES.S),
    BLUETOOTH_ADVERTISE(Manifest.permission.BLUETOOTH_ADVERTISE, Build.VERSION_CODES.S),
    BLUETOOTH_CONNECT(Manifest.permission.BLUETOOTH_CONNECT, Build.VERSION_CODES.S),
    
    // ==================== 网络权限 ====================
    INTERNET(Manifest.permission.INTERNET),
    ACCESS_NETWORK_STATE(Manifest.permission.ACCESS_NETWORK_STATE),
    ACCESS_WIFI_STATE(Manifest.permission.ACCESS_WIFI_STATE),
    CHANGE_WIFI_STATE(Manifest.permission.CHANGE_WIFI_STATE),
    CHANGE_NETWORK_STATE(Manifest.permission.CHANGE_NETWORK_STATE),
    NEARBY_WIFI_DEVICES(Manifest.permission.NEARBY_WIFI_DEVICES, Build.VERSION_CODES.TIRAMISU),
    
    // ==================== 系统权限 ====================
    VIBRATE(Manifest.permission.VIBRATE),
    WAKE_LOCK(Manifest.permission.WAKE_LOCK),
    SYSTEM_ALERT_WINDOW(Manifest.permission.SYSTEM_ALERT_WINDOW),
    WRITE_SETTINGS(Manifest.permission.WRITE_SETTINGS),
    REQUEST_IGNORE_BATTERY_OPTIMIZATIONS(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Build.VERSION_CODES.M),
    SCHEDULE_EXACT_ALARM(Manifest.permission.SCHEDULE_EXACT_ALARM, Build.VERSION_CODES.S),
    USE_EXACT_ALARM(Manifest.permission.USE_EXACT_ALARM, Build.VERSION_CODES.TIRAMISU),
    
    // ==================== 通知权限 ====================
    POST_NOTIFICATIONS(Manifest.permission.POST_NOTIFICATIONS, Build.VERSION_CODES.TIRAMISU),
    
    // ==================== 其他权限 ====================
    FOREGROUND_SERVICE(Manifest.permission.FOREGROUND_SERVICE, Build.VERSION_CODES.P),
    REQUEST_INSTALL_PACKAGES(Manifest.permission.REQUEST_INSTALL_PACKAGES, Build.VERSION_CODES.O),
    REQUEST_DELETE_PACKAGES(Manifest.permission.REQUEST_DELETE_PACKAGES, Build.VERSION_CODES.O),
    PACKAGE_USAGE_STATS(Manifest.permission.PACKAGE_USAGE_STATS, Build.VERSION_CODES.LOLLIPOP);
    
    /**
     * 检查当前权限是否在当前Android版本中可用
     */
    fun isAvailableOnCurrentVersion(): Boolean {
        return Build.VERSION.SDK_INT >= minSdkVersion
    }
    
    /**
     * 获取权限的简化显示名称
     */
    fun getDisplayName(): String {
        return manifestPermission.substringAfterLast(".")
            .replace("_", " ")
            .lowercase()
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
    
    companion object {
        /**
         * 根据manifest权限字符串查找对应的枚举
         */
        fun fromManifestPermission(permission: String): PermissionEnum? {
            return values().find { it.manifestPermission == permission }
        }
        
        /**
         * 获取当前Android版本可用的权限
         */
        fun getAvailablePermissions(): List<PermissionEnum> {
            return values().filter { it.isAvailableOnCurrentVersion() }
        }
        
        /**
         * 获取所有权限的manifest字符串列表
         */
        fun getAllManifestPermissions(): List<String> {
            return values().map { it.manifestPermission }
        }
    }
}
