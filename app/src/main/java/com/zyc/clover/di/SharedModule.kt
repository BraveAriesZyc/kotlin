package com.zyc.clover.di

import com.zyc.clover.viewmodel.InitAppViewModel
import com.zyc.clover.manager.AppInitializationManager
import com.zyc.feature.auth.AuthViewModel
import com.zyc.feature.message.MessageViewModel
import com.zyc.feature.message.SendMessageViewModel
import com.zyc.core.data.repository.AuthRepository
import com.zyc.core.data.repository.impl.AuthRepositoryImpl
import com.zyc.core.data.repository.ChatRepository
import com.zyc.core.data.repository.MessageRepository
import com.zyc.core.data.repository.UserRepository
import com.zyc.core.data.repository.impl.ChatRepositoryImpl
import com.zyc.core.data.repository.impl.MessageRepositoryImpl
import com.zyc.core.data.repository.impl.UserRepositoryImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// 共享模块定义所有依赖
val sharedModule = module {
    // 定义 AuthRepository 为单例
    single<AuthRepository> { AuthRepositoryImpl() }
    
    // 定义 UserRepository 为单例
    single<UserRepository> { UserRepositoryImpl() }

    // 定义 MessageRepository 为单例
    single<MessageRepository> { MessageRepositoryImpl() }

    // 定义 ChatRepository 为单例
    single<ChatRepository> { ChatRepositoryImpl() }
    
    // 定义 AppInitializationManager 为单例
    single<AppInitializationManager> {
        AppInitializationManager(
            userRepository = get(),
            messageRepository = get(),
            chatRepository = get()
        )
    }
    viewModel {
        MessageViewModel(
            chatRepository = get<ChatRepository>() as ChatRepositoryImpl,
        )
    }
    viewModel {
        SendMessageViewModel(
            chatRepository = get<ChatRepository>() as ChatRepositoryImpl,
        )
    }
    viewModel {
        InitAppViewModel(
            initializationManager = get(),
        )
    }
    viewModel {
        AuthViewModel(
            authRepository = get()
        )
    }
}
