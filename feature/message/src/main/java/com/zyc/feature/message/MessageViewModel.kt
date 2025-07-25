package com.zyc.feature.message

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.zyc.core.data.repository.impl.ChatRepositoryImpl
import com.zyc.core.model.entity.SessionMember
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessageViewModel(
    private val chatRepository: ChatRepositoryImpl
) : ViewModel() {
    val conversationList: StateFlow<List<SessionMember>> = chatRepository.conversationList
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    fun getUser(userId: String): SessionMember? {
        Log.d("getUser", "userId: $userId")
        return chatRepository.getUser(userId)
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                // 模拟网络请求
                delay(1500)


            } catch (e: Exception) {
                // 处理错误
                Log.e("错误", "刷新消息列表失败")
            } finally {
                // 无论成功失败，都要结束刷新状态
                _isRefreshing.value = false
            }
        }
    }
}