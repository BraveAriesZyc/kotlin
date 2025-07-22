package com.zyc.data.models


data class SessionMemberModel(
    val uid: String,
    val sessionId: String,
    val userName: String,
    val avatar: String,
    val role: String,
    val timestamp: Long = System.currentTimeMillis()
)
