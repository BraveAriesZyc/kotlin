plugins {
    id("android-feature-convention")
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.zyc.feature.permission"
}

dependencies {
    // Accompanist Permissions - 权限管理专用库
    implementation(libs.accompanist.permissions)
}