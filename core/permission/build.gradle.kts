plugins {
    id("android-library-convention")
}

android {
    namespace = "com.zyc.core.permission"
}

dependencies {
    // 核心Android依赖
    implementation(libs.androidx.core.ktx)
}
