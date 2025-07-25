pluginManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven {
            url = uri("https://maven.aliyun.com/repository/central")
            content {
                // 确保 SQLDelight 相关依赖从阿里云镜像下载
                includeGroup("app.cash.sqldelight")
                includeGroup("com.squareup.sqldelight")
                // 其他常用依赖
                includeGroupByRegex("org\\.jetbrains.*")
                includeGroupByRegex("com\\.squareup.*")
            }
        }
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        // 官方仓库
        maven { url = uri("https://dl.google.com/dl/android/maven2/") }
        maven { url = uri("https://jcenter.bintray.com") }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {

        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        // 官方仓库
        maven { url = uri("https://dl.google.com/dl/android/maven2/") }
        maven { url = uri("https://jcenter.bintray.com") }
        mavenCentral()
        google()

    }
}

rootProject.name = "clover_app"

// 应用模块
include(":app")

// 功能模块
include(":feature:auth")
include(":feature:home")
include(":feature:message")
include(":feature:friend")
include(":feature:profile")
include(":feature:common-ui")

// 核心模块
include(":core:common")
include(":core:ui")
include(":core:network")
include(":core:database")
include(":core:model")
include(":core:data")
