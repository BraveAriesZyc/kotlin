package com.zyc.clover

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zyc.clover.repository.ChatRepository
import com.zyc.clover.repository.MessageRepository
import com.zyc.clover.repository.UserRepository

class InitAppViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {
    fun initApp() {
        chatRepository.initApp()
    }
}
