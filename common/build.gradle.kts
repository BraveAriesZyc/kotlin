plugins {
    id("android-library-convention")
}

android {
    namespace = "com.zyc.common"
}
dependencies {
    api(libs.androidx.core.ktx)
}
