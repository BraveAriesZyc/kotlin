package com.zyc.core.ui.components.drawer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DrawerViewModel : ViewModel() {
   private val _drawerState = MutableStateFlow(false)
    val drawerState: StateFlow<Boolean> = _drawerState
    fun  toggleDrawer(){
        _drawerState.value = !_drawerState.value
    }
 }