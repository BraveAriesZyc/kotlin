package com.zyc.core.common.extensions

import java.util.regex.Pattern

/**
 * String 扩展函数
 */

/**
 * 检查字符串是否为有效的邮箱地址
 */
fun String.isValidEmail(): Boolean {
    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    return Pattern.matches(emailPattern, this)
}

/**
 * 检查字符串是否为有效的手机号码
 */
fun String.isValidPhone(): Boolean {
    val phonePattern = "^1[3-9]\\d{9}$"
    return Pattern.matches(phonePattern, this)
}

/**
 * 检查密码强度
 * @return true 如果密码包含至少8个字符，包含大小写字母和数字
 */
fun String.isStrongPassword(): Boolean {
    if (length < 8) return false
    
    val hasUpperCase = any { it.isUpperCase() }
    val hasLowerCase = any { it.isLowerCase() }
    val hasDigit = any { it.isDigit() }
    
    return hasUpperCase && hasLowerCase && hasDigit
}

/**
 * 脱敏手机号码
 * 例如：13812345678 -> 138****5678
 */
fun String.maskPhone(): String {
    return if (length == 11 && startsWith("1")) {
        "${substring(0, 3)}****${substring(7)}"
    } else {
        this
    }
}

/**
 * 脱敏邮箱地址
 * 例如：test@example.com -> t***@example.com
 */
fun String.maskEmail(): String {
    val atIndex = indexOf('@')
    return if (atIndex > 0) {
        val username = substring(0, atIndex)
        val domain = substring(atIndex)
        if (username.length <= 1) {
            this
        } else {
            "${username.first()}${'*'.toString().repeat(username.length - 1)}$domain"
        }
    } else {
        this
    }
}

/**
 * 安全地转换为 Int，失败时返回默认值
 */
fun String.toIntOrDefault(defaultValue: Int = 0): Int {
    return toIntOrNull() ?: defaultValue
}

/**
 * 安全地转换为 Long，失败时返回默认值
 */
fun String.toLongOrDefault(defaultValue: Long = 0L): Long {
    return toLongOrNull() ?: defaultValue
}

/**
 * 检查字符串是否不为空且不为空白
 */
fun String?.isNotNullOrBlank(): Boolean {
    return !isNullOrBlank()
}