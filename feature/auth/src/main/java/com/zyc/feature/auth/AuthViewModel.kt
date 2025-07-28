package com.zyc.feature.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyc.core.model.entity.User
import com.zyc.core.model.entity.UserModel
import com.zyc.core.network.api.LoginApi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val _phone = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    // 确认密码
    private val _confirmPassword = MutableStateFlow("")
    val phone: StateFlow<String> = _phone
    val password: StateFlow<String> = _password
    val confirmPassword: StateFlow<String> = _confirmPassword


    fun setPhone(userName: String) {
        _phone.value = userName
    }

    fun setPassword(password: String) {
        _password.value = password
    }
    fun setConfirmPassword(password: String) {
        _confirmPassword.value = password
    }

    fun loginSubmit() {
        viewModelScope.launch {
//            try {
//                val result =  LoginApi.login(
//                    User(
//                        username = phone.value,
//                        password = password.value,
//                        id = null,
//                        userId = null,
//                        createTime = TODO(),
//                        updateTime = TODO()
//                    )
//                )
//                Log.d("login", result.toString())
//            } catch (e: Exception) {
//                Log.e("login", "Login failed", e)
//            }
        }
    }

    fun registerSubmit() {
        viewModelScope.launch {
//            try {
//                val result = LoginApi.register(
//                    phone = phone.value,
//                    password = password.value
//                )
//                Log.d("register", result.toString())
//            } catch (e: Exception) {
//                Log.e("register", "Register failed", e)
//            }
        }
    }
}
