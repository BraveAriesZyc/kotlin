plugins {
    id("android-library-convention")
    alias(libs.plugins.sqlDelight)
}

// SQLDelight 依赖将从 settings.gradle.kts 中配置的仓库下载

android {
    namespace = "com.zyc.db"
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.zyc.db")
        }
    }
}



dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.android.driver)
}
