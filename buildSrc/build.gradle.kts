/**
 * buildSrc 模块构建配置
 * 
 * buildSrc 是 Gradle 的特殊模块，用于定义项目的构建逻辑和通用配置插件。
 * 该模块中的代码会被自动编译并在项目构建时可用。
 * 
 * 主要功能：
 * - 定义通用的 Android 配置插件
 * - 统一管理构建配置常量
 * - 提供类型安全的构建脚本
 * - 减少各模块间的重复配置
 */

plugins {
    // Kotlin DSL 插件，允许使用 Kotlin 编写 Gradle 构建脚本
    `kotlin-dsl`
}

// 仓库配置，用于解析插件依赖
// 与 settings.gradle.kts 中的 pluginManagement 仓库配置保持一致
repositories {
    // 阿里云镜像源，提供更快的下载速度
    maven { url = uri("https://maven.aliyun.com/repository/google") }
    maven { url = uri("https://maven.aliyun.com/repository/central") }
    maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    
    // 官方仓库作为备用
    maven { url = uri("https://dl.google.com/dl/android/maven2/") }
    maven { url = uri("https://jcenter.bintray.com") }
    
    // Google 官方仓库，限制特定组的依赖
    google {
        content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("com\\.google.*")
            includeGroupByRegex("androidx.*")
        }
    }
    
    mavenCentral()       // Maven 中央仓库
    gradlePluginPortal() // Gradle 插件门户
}

// 依赖配置，包含构建插件所需的依赖
// buildSrc 需要这些依赖来编译自定义插件
dependencies {
    // Android Gradle 插件，用于 Android 应用和库的构建
    // 使用与 libs.versions.toml 中 agp 版本一致的版本 (8.10.0)
    implementation("com.android.tools.build:gradle:8.10.0")
    
    // Kotlin Gradle 插件，用于 Kotlin 代码的编译
    // 使用与 libs.versions.toml 中 kotlin 版本一致的版本 (2.0.21)
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21")
}

// 注册自定义插件
gradlePlugin {
    plugins {
        register("android-application-convention") {
            id = "android-application-convention"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("android-library-convention") {
            id = "android-library-convention"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("android-feature-convention") {
            id = "android-feature-convention"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
    }
}