plugins {
    id("android-library-convention")
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.zyc.core.permission"
}

dependencies {
    implementation(libs.accompanist.permissions)
}
