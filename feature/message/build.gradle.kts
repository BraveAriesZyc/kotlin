plugins {
    id("android-feature-convention")
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.zyc.feature.message"
}

dependencies {
    // 核心模块
    implementation(project(":core:model"))
    implementation(project(":core:data"))
}