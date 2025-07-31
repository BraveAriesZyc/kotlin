package com.zyc.core.common

/**
 * 应用全局配置
 * 仅包含通用配置常量
 */
data object AppConfig {
    // 数据库配置
    const val SQL_NAME = "flower.db"

    // 通用延迟配置
    const val DEFAULT_DELAY = 300L
    const val FAST_DELAY = 150L
    const val SLOW_DELAY = 500L

    // 通用超时配置
    const val DEFAULT_TIMEOUT = 30_000L
    const val SHORT_TIMEOUT = 10_000L
    const val LONG_TIMEOUT = 60_000L

    // 通用大小配置
    const val DEFAULT_PADDING = 16
    const val SMALL_PADDING = 8
    const val LARGE_PADDING = 24

    // 通用圆角
    const val DEFAULT_ROUND = 16
    const val SMALL_ROUND = 8
    const val LARGE_ROUND = 24

    // 通用开关配置
    const val ENABLE_DEBUG = true
    const val ENABLE_LOGGING = true
}
