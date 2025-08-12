package com.zyc.core.permission.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * 权限请求跟踪器
 * 用于跟踪权限请求历史，帮助更准确地判断权限是否被永久拒绝
 */
object PermissionTracker {
    private const val PREF_NAME = "permission_tracker"
    private const val KEY_PERMISSION_REQUESTED = "permission_requested_"
    private const val KEY_PERMISSION_DENIED_COUNT = "permission_denied_count_"
    
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * 标记权限已被请求过
     */
    fun markPermissionRequested(context: Context, permission: String) {
        getPreferences(context)
            .edit()
            .putBoolean(KEY_PERMISSION_REQUESTED + permission, true)
            .apply()
    }
    
    /**
     * 检查权限是否已被请求过
     */
    fun hasPermissionBeenRequested(context: Context, permission: String): Boolean {
        return getPreferences(context)
            .getBoolean(KEY_PERMISSION_REQUESTED + permission, false)
    }
    
    /**
     * 增加权限拒绝次数
     */
    fun incrementDeniedCount(context: Context, permission: String) {
        val currentCount = getDeniedCount(context, permission)
        getPreferences(context)
            .edit()
            .putInt(KEY_PERMISSION_DENIED_COUNT + permission, currentCount + 1)
            .apply()
    }
    
    /**
     * 获取权限拒绝次数
     */
    fun getDeniedCount(context: Context, permission: String): Int {
        return getPreferences(context)
            .getInt(KEY_PERMISSION_DENIED_COUNT + permission, 0)
    }
    
    /**
     * 清除权限跟踪记录（当权限被授予时调用）
     */
    fun clearPermissionRecord(context: Context, permission: String) {
        getPreferences(context)
            .edit()
            .remove(KEY_PERMISSION_REQUESTED + permission)
            .remove(KEY_PERMISSION_DENIED_COUNT + permission)
            .apply()
    }
    
    /**
     * 判断权限是否可能被永久拒绝
     * 基于请求历史和拒绝次数进行判断
     */
    fun isPossiblyPermanentlyDenied(context: Context, permission: String, shouldShowRationale: Boolean): Boolean {
        val hasBeenRequested = hasPermissionBeenRequested(context, permission)
        val deniedCount = getDeniedCount(context, permission)
        
        // 如果从未请求过，肯定不是永久拒绝
        if (!hasBeenRequested) {
            return false
        }
        
        // 如果shouldShowRationale为true，说明是临时拒绝
        if (shouldShowRationale) {
            return false
        }
        
        // 如果已经请求过且shouldShowRationale为false，很可能是永久拒绝
        // 特别是如果拒绝次数大于等于1次
        return deniedCount >= 1
    }
}