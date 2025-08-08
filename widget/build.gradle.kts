plugins {
    id("android-feature-convention")
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.zyc.feature.ui_showcase"


}
dependencies {
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.work.runtime.ktx)
}

