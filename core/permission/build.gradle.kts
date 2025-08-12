plugins {
    id("android-library-convention")
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.zyc.core.permission"
}

dependencies {
    // 核心Android依赖
    implementation(libs.androidx.activity.compose)
}
