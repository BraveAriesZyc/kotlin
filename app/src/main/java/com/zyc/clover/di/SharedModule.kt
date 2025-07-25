package com.zyc.clover.di

import com.zyc.clover.viewmodel.InitAppViewModel
import com.zyc.clover.manager.AppInitializationManager
import com.zyc.feature.message.MessageViewModel
import com.zyc.feature.message.SendMessageViewModel
import com.zyc.data.repository.ChatRepository
import com.zyc.data.repository.MessageRepository
import com.zyc.data.repository.UserRepository
import com.zyc.data.repository.impl.ChatRepositoryImpl
import com.zyc.data.repository.impl.MessageRepositoryImpl
import com.zyc.data.repository.impl.UserRepositoryImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// 共享模块定义所有依赖
val sharedModule = module {
    // 定义 UserRepository 为单例
    single<UserRepository> { UserRepositoryImpl(
        databaseRepository = get()
    ) }

    // 定义 MessageRepository 为单例
    single<MessageRepository> { MessageRepositoryImpl(
        databaseRepository = get(),
    ) }

    // 定义 ChatRepository 为单例，注入 UserRepository 和 MessageRepository
    single<ChatRepository> {
        ChatRepositoryImpl(
            messageRepository = get(),  // 获取 MessageRepository 实例
            userRepository = get()     // 获取 UserRepository 实例

        )
    }
    
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
            chatRepository = get(),
        )
    }
    viewModel {
        SendMessageViewModel(
            chatRepository = get(),
        )
    }
    viewModel {
        InitAppViewModel(
            initializationManager = get(),
        )
    }
}
