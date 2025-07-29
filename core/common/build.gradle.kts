plugins {
    id("android-library-convention")
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.zyc.core.common"

    buildFeatures {
        compose = true
    }
}

dependencies {

    // Android 核心库
    api(libs.androidx.core.ktx)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.material3)
    // Kotlin 协程
    api(libs.kotlinx.coroutines.android)

    // 序列化
    api(libs.kotlinx.serialization.json)

    // 依赖注入
    api(libs.koin.android)
    api(libs.koin.core)

    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
