plugins {
    id("android-feature-convention")
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.zyc.feature.auth"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:network"))
}