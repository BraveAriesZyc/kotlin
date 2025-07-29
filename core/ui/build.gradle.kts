plugins {
    id("android-library-convention")
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.zyc.core.ui"

    buildFeatures {
        compose = true
    }
}

dependencies {
    // 依赖核心模块
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    api(project(":core:router"))

    // 依赖功能模块 - 移除以避免循环依赖

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Compose 核心库
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
    api(libs.androidx.activity.compose)

    // Navigation
    api(libs.androidx.navigation.compose)

    // 图片加载
    api(libs.coil.compose)
    api(libs.coil.network.okhttp)

    // 依赖注入
    api(libs.koin.compose)
    api(libs.koin.compose.viewmodel)

    // 生命周期
    api(libs.lifecycle.runtime.ktx)

    // 视屏播放exoplayer

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.ui.compose)

    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}
