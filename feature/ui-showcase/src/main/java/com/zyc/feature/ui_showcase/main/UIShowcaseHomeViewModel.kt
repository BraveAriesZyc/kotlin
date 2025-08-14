package com.zyc.feature.ui_showcase.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zyc.core.router.Routes
import com.zyc.feature.ui_showcase.model.ComponentCategory
import com.zyc.feature.ui_showcase.model.UIShowcaseHomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI展示主页面的ViewModel
 */
class UIShowcaseHomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UIShowcaseHomeUiState())
    val uiState: StateFlow<UIShowcaseHomeUiState> = _uiState.asStateFlow()

    init {
        Log.d("UIShowcaseHomeViewModel", "ViewModel initialized")
        loadComponentCategories()
    }

    /**
     * 加载组件分类数据
     */
    private fun loadComponentCategories() {
        Log.d("UIShowcaseHomeViewModel", "Starting to load component categories")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            Log.d("UIShowcaseHomeViewModel", "Set loading state to true")

            try {
                val categories = getComponentCategories()
                Log.d("UIShowcaseHomeViewModel", "Got ${categories.size} categories")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    categories = categories,
                    error = null
                )
                Log.d("UIShowcaseHomeViewModel", "Successfully loaded categories")
            } catch (e: Exception) {
                Log.e("UIShowcaseHomeViewModel", "Error loading categories", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "未知错误"
                )
            }
        }
    }


    /**
     * 清除错误状态
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * 获取组件分类数据
     */
    private fun getComponentCategories(): List<ComponentCategory> = listOf(
        ComponentCategory(
            title = "通用组件",
            description = "基础的通用UI组件",
            route = Routes.UIShowcase.CommonComponents,
            icon = "\uEBE5",
            items = listOf(
                "ZAppBar - 统一的应用顶部导航栏",
                "通用按钮组件",
                "通用卡片组件"
            )
        ),
        ComponentCategory(
            title = "表单组件",
            description = "用于用户输入的表单相关组件",
            route = Routes.UIShowcase.FormComponents,
            icon = "\uEC57",
            items = listOf(
                "ButtonComponent - 基础按钮组件",
                "FormButton - 表单专用按钮",
                "FormInput - 表单输入框",
                "InputComponent - 通用输入组件",
                "NoBorderFormInput - 无边框输入框"
            )
        ),
        ComponentCategory(
            title = "反馈组件",
            description = "用于用户反馈和状态显示的组件",
            route = Routes.UIShowcase.FeedbackComponents,
            icon = "\uEC60",
            items = listOf(
                "Loading - 加载动画组件",
                "AnimatedBallLoaderImp - 球形加载动画",
                "DouyinBounceLoaderImp - 抖音风格弹跳加载",
                "HorizontalBounceLoaderImp - 水平弹跳加载",
                "TextLoaderImp - 文字加载动画",
                "CustomProgressBar - 自定义进度条"
            )
        ),
        ComponentCategory(
            title = "布局组件",
            description = "用于页面布局和容器的组件",
            route = Routes.UIShowcase.LayoutComponents,
            icon = "\uECB1",
            items = listOf(
                "PageScreen - 统一页面布局",
                "PageScreenData - 页面状态数据",
                "BounceListView - 弹性列表视图",
                "ZRefreshView - 下拉刷新容器"
            )
        ),
        ComponentCategory(
            title = "导航组件",
            description = "用于应用导航的组件",
            route = Routes.UIShowcase.NavigationComponents,
            icon = "\uEC57",
            items = listOf(
                "WeChatPopupMenu - 微信风格弹出菜单",
                "MenuAction - 菜单操作数据类"
            )
        ),
        ComponentCategory(
            title = "交互组件",
            description = "用于用户交互的组件",
            route = Routes.UIShowcase.InteractionComponents,
            icon = "\uED9B",
            items = listOf(
                "InputArea - 智能输入区域",
                "键盘相关组件",
                "手势识别组件"
            )
        ),
        ComponentCategory(
            title = "动画组件",
            description = "各种动画效果组件",
            route = Routes.UIShowcase.AnimationComponents,
            icon = "\uEEA3",
            items = listOf(
                "组合动画效果",
                "过渡动画组件",
                "自定义动画效果"
            )
        ),
        ComponentCategory(
            title = "硬件组件",
            description = "用于硬件设备交互的组件",
            route = Routes.UIShowcase.HardwareComponents,
            icon = "\uEEDC",
            items = listOf(
                "相机组件 - 相机拍照和录像",
                "传感器组件 - 重力感应、陀螺仪等",
                "蓝牙组件 - 蓝牙设备连接和通信",
                "GPS定位组件 - 位置获取和地图显示",
                "NFC组件 - 近场通信功能",
                "指纹识别组件 - 生物识别认证",
                "振动反馈组件 - 触觉反馈",
                "音频组件 - 录音和播放"
            )
        ),
        ComponentCategory(
            title = "权限组件",
            description = "权限管理和检查相关的组件演示",
            route = Routes.UIShowcase.PermissionComponents,
            icon = "\uEA13",
            items = listOf(
                "权限状态检查 - 检查单个权限授权状态",
                "多权限检查 - 批量检查多个权限状态",
                "危险权限识别 - 识别和标记危险权限",
                "权限信息获取 - 获取权限详细信息",
                "权限管理器演示 - PermissionManager核心功能"
            )
        ),
        ComponentCategory(
            title = "系统",
            description = "权限管理和检查相关的组件演示",
            route = Routes.UIShowcase.SystemComponents,
            icon = "\uEA56",
            items = listOf(
                "ZAppBar - 统一的应用顶部导航栏",
                "通用按钮组件",
                "通用卡片组件"
            )
        ),
        ComponentCategory(
            title = "网络组件",
            description = "网络组件相关的组件演示",
            route = Routes.UIShowcase.NetworkComponents.FileUploadComponents,
            icon = "\uEA58",
            items = listOf(
                ""
            )
        )
    )
}
