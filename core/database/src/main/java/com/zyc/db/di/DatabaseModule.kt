package com.zyc.db.di

import android.content.Context
import com.zyc.db.database.DatabaseInitialize
import com.zyc.db.database.DatabaseRepository
import com.zyc.db.database.DatabaseRepositoryImpl
import com.zyc.db.utils.db.DatabaseDriverFactory
import org.koin.dsl.module

val databaseModule = module {
    // 提供 DatabaseDriverFactory
    single { DatabaseDriverFactory(get<Context>()) }
    
    // 提供 DatabaseInitialize
    single { DatabaseInitialize(get()) }
    
    // 提供 DatabaseRepository
    single<DatabaseRepository> { DatabaseRepositoryImpl(get()) }
}