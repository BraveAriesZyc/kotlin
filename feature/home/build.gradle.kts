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
    implementation(project(":core:data"))
}