package com.zyc.clover.pages.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyc.clover.api.LoginApi
import com.zyc.clover.models.UserModel
import com.zyc.clover.utils.network.ResponseData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
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
            LoginApi.login(
                UserModel(
                    phone = phone.value,
                    password = password.value
                )
            )
        }
    }

    fun registerSubmit() {
        viewModelScope.launch {
        val res:  ResponseData<String> = LoginApi.register(
                UserModel(
                    phone = phone.value,
                    password = password.value
                )
            )
            Log.d("register", res.toString())
        }
    }
}
