plugins {
    id("android-library-convention")
    alias(libs.plugins.sqlDelight)
}

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
