plugins {
    id("android-feature-convention")
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.zyc.feature.home"
}

dependencies {
    // 核心模块
    implementation(project(":core:model"))
    
    // 媒体播放
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.ui.compose)
}