package com.zyc.clover.components.keyboard

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class InputAreaViewModel : ViewModel() {
   private var _text  =   mutableStateOf("")
    val text: State<String> = _text

    fun setText(text: String){
        _text.value = text
    }

}
