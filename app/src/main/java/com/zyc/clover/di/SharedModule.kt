package com.zyc.clover.di

import com.zyc.clover.InitAppViewModel
import com.zyc.clover.pages.layout.message.MessageViewModel
import com.zyc.clover.pages.layout.children.message.children.SendMessageViewModel
import com.zyc.clover.repository.ChatRepository
import com.zyc.clover.repository.MessageRepository
import com.zyc.clover.repository.UserRepository
import com.zyc.clover.repository.impl.ChatRepositoryImpl
import com.zyc.clover.repository.impl.MessageRepositoryImpl
import com.zyc.clover.repository.impl.UserRepositoryImpl
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
            chatRepository = get(),
        )
    }
}
