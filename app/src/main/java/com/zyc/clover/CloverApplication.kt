package com.zyc.clover

import android.app.Application
import com.zyc.clover.di.initializeKoin
import org.koin.android.ext.koin.androidContext

// 启动时初始化koin 依赖注入
class CloverApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidContext(this@CloverApplication)
        }
    }
}
