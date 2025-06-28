package com.zyc.clover.models

import com.zyc.clover.models.enums.Role


data class SessionMemberModel(
    val uid: String,
    val sessionId: String,
    val userName: String,
    val avatar: String,
    val role: String,
    val timestamp: Long = System.currentTimeMillis()
)
