plugins {
    id("android-library-convention")
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.zyc.feature.common_page"

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core modules
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    
    // Feature modules - direct dependencies
    implementation(project(":feature:home"))
    implementation(project(":feature:message"))
    implementation(project(":feature:friend"))
    implementation(project(":feature:profile"))

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
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
    
    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}