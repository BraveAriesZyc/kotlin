package com.zyc.clover.pages.layout.message

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.zyc.clover.models.SessionMemberModel
import com.zyc.clover.models.UserModel
import com.zyc.clover.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessageViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {
    val conversationList: StateFlow<List<SessionMemberModel>> = chatRepository.conversationList
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    fun getUser(uid: String): UserModel {
        Log.d("getUser", "uid: $uid")
        return chatRepository.getUser(uid)
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
