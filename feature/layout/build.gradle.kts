plugins {
    id("android-library-convention")
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.zyc.feature.layout"

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core modules
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    
    // Feature modules - direct dependencies
    implementation(project(":feature:home"))
    implementation(project(":feature:message"))
    implementation(project(":feature:friend"))
    implementation(project(":feature:profile"))

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    
    // Navigation
    implementation(libs.androidx.navigation.compose)
    
    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    
    // Lifecycle
    implementation(libs.lifecycle.runtime.ktx)
    
    // Serialization
    implementation(libs.kotlinx.serialization.json)
    
    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.00"))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}