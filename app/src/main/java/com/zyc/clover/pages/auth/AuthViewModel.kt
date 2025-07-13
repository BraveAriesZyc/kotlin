package com.zyc.clover.pages.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyc.clover.api.LoginApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _phone = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    val phone: StateFlow<String> = _phone
    val password: StateFlow<String> = _password


    fun setPhone(userName: String) {
        _phone.value = userName
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun loginSubmit() {
        viewModelScope.launch {
            LoginApi.login(phone.value, password.value)
        }
    }
    fun registerSubmit() {
        viewModelScope.launch {
            LoginApi.register(phone.value, password.value)
        }
    }
}
