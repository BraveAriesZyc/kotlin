pluginManagement {
    repositories {

        // 优先添加 Square 仓库
        maven { url = uri("https://maven.squareup.com") }
        mavenCentral()
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
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
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 优先添加 Square 仓库
        maven { url = uri("https://maven.squareup.com") }
        mavenCentral()
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        // 官方仓库
        maven { url = uri("https://dl.google.com/dl/android/maven2/") }
        maven { url = uri("https://jcenter.bintray.com") }
        google()

    }
}

rootProject.name = "clover_app"
include(":app")
include(":db")
include(":common")
include(":data")
