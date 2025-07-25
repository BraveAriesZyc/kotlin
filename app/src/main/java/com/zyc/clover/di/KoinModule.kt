package com.zyc.clover.di

import com.zyc.db.di.databaseModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initializeKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, databaseModule)
    }
}
