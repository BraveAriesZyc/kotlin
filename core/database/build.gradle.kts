plugins {
    id("android-library-convention")
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.zyc.core.database"
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.zyc.core.database")
        }
    }
}

dependencies {
    // 依赖核心模块
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    
    // SQLDelight
    api(libs.android.driver)
    
    // 序列化
    implementation(libs.ktor.serialization.kotlinx.json)
    
    // 依赖注入
    api(libs.koin.android)
    api(libs.koin.core)
    
    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
