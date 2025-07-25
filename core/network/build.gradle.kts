plugins {
    id("android-library-convention")
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.zyc.core.network"
}

dependencies {
    // 依赖核心模块
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    // Ktor 网络库
    api(libs.ktor.client.core)
    api(libs.ktor.client.okhttp)
    api(libs.ktor.client.logging)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.serialization.kotlinx.json)

    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
