plugins {
    id("android-library-convention")
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.zyc.feature.ui_showcase"

    buildFeatures {
        compose = true
    }
}

dependencies {
    // 依赖核心模块
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Compose 核心库
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // 生命周期
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)

    // 依赖注入
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
}