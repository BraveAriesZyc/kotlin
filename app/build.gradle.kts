plugins {
    id("android-application-convention")
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.zyc.clover"

    defaultConfig {
        applicationId = "com.zyc.clover"
        versionCode = 110
        versionName = "1.0.5"
    }
}




dependencies {
    // 功能模块
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:message"))
    implementation(project(":feature:friend"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:layout"))

    // 核心模块
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:database"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //
    implementation(libs.kotlinx.coroutines.android)
    //
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.core)
    //
    implementation(libs.androidx.navigation.compose)
    //
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    //
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose)
    //


    //
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)




    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.00"))
}
