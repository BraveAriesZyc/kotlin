plugins {
    id("android-library-convention")
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.zyc.core.model"
}

dependencies {
    // 依赖 common 模块
    implementation(project(":core:common"))
    
    // 序列化
    api(libs.kotlinx.serialization.json)
    
    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}