package com.zyc.clover.manager

import com.zyc.core.common.manager.InitializationState
import com.zyc.data.repository.ChatRepository
import com.zyc.data.repository.MessageRepository
import com.zyc.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 应用初始化管理器
 * 负责协调和优化应用启动时的数据初始化流程
 */
class AppInitializationManager(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository
) {
    private val initScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _initializationState = MutableStateFlow(InitializationState.IDLE)
    val initializationState: StateFlow<InitializationState> = _initializationState

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    /**
     * 异步初始化应用数据
     * 使用并发执行来提高初始化速度
     */
    suspend fun initializeApp() {
        if (_initializationState.value == InitializationState.INITIALIZING) {
            return // 防止重复初始化
        }

        _initializationState.value = InitializationState.INITIALIZING
        _progress.value = 0f

        try {
            withContext(Dispatchers.IO) {
                // 并发执行独立的初始化任务
                val tasks = listOf(
                    async {
                        userRepository.initApp()
                        _progress.value = 0.33f
                    },
                    async {
                        messageRepository.initApp()
                        _progress.value = 0.66f
                    }
                )

                // 等待所有任务完成
                tasks.awaitAll()

                // 执行依赖于前面任务的初始化
                chatRepository.initApp()
                _progress.value = 1f
            }

            _initializationState.value = InitializationState.COMPLETED
        } catch (e: Exception) {
            _initializationState.value = InitializationState.ERROR
            throw e
        }
    }

    /**
     * 预加载关键数据
     * 在应用启动时提前加载用户最常访问的数据
     */
    fun preloadCriticalData() {
        initScope.launch {
            try {
                // 预加载用户信息（同步执行，因为其他功能依赖用户信息）
                userRepository.initApp()

                // 异步预加载消息数据
                launch {
                    messageRepository.initApp()
                }
            } catch (e: Exception) {
                // 记录错误但不阻塞应用启动
                println("预加载数据时发生错误: ${e.message}")
            }
        }
    }

    /**
     * 重置初始化状态
     * 用于重新初始化或错误恢复
     */
    fun reset() {
        _initializationState.value = InitializationState.IDLE
        _progress.value = 0f
    }

    /**
     * 检查是否已完成初始化
     */
    fun isInitialized(): Boolean {
        return _initializationState.value == InitializationState.COMPLETED
    }

    /**
     * 检查是否正在初始化
     */
    fun isInitializing(): Boolean {
        return _initializationState.value == InitializationState.INITIALIZING
    }

    /**
     * 检查是否有初始化错误
     */
    fun hasError(): Boolean {
        return _initializationState.value == InitializationState.ERROR
    }
}