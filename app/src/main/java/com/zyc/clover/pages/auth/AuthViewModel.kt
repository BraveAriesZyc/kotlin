package com.zyc.clover.pages.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val _userName = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    val userName: StateFlow<String> = _userName
    val password: StateFlow<String> = _password


    fun setUserName(userName: String) {
        _userName.value = userName
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun loginSubmit() {


    }
}
