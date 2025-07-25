# 通用构建配置

本项目使用 `buildSrc` 来统一管理所有模块的构建配置，避免重复代码。

## 使用方法

### 1. Android 应用模块配置

在应用模块的 `build.gradle.kts` 文件中使用：

```kotlin
plugins {
    id("android-application-convention")
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization) // 如果需要序列化
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
    // 添加依赖...
}
```

### 2. Android 库模块配置

在库模块的 `build.gradle.kts` 文件中使用：

```kotlin
plugins {
    id("android-library-convention")
    // 如果需要 Compose，添加：
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.zyc.clover.yourmodule"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    // 添加其他依赖...
}
```

### 2. 自动配置的内容

#### Android 应用模块 (android-application-convention)

- **Android 插件**：自动应用 `com.android.application` 和 `org.jetbrains.kotlin.android`
- **编译 SDK**：设置为 35
- **最小 SDK**：设置为 34
- **目标 SDK**：设置为 35
- **Java 版本**：设置为 Java 11
- **Kotlin JVM 目标**：设置为 11
- **测试运行器**：配置为 AndroidJUnitRunner
- **Compose 支持**：自动启用 Compose 构建特性
- **构建类型**：配置 release 构建类型

#### Android 库模块 (android-library-convention)

- **Android 插件**：自动应用 `com.android.library` 和 `org.jetbrains.kotlin.android`
- **编译 SDK**：设置为 35
- **最小 SDK**：设置为 34
- **目标 SDK**：设置为 35
- **Java 版本**：设置为 Java 11
- **Kotlin JVM 目标**：设置为 11
- **测试运行器**：配置为 AndroidJUnitRunner

### 3. 优势

- **减少重复代码**: 所有模块共享相同的基础配置
- **统一管理**: 版本和配置集中管理，便于维护
- **易于更新**: 只需在一个地方修改配置，所有模块自动更新
- **类型安全**: 使用 Kotlin DSL，提供更好的 IDE 支持

### 4. 依赖管理

项目使用 `gradle/libs.versions.toml` 来统一管理所有依赖版本，请直接使用 `libs` 引用：

```kotlin
dependencies {
    // 项目模块依赖
    implementation(project(":common"))
    implementation(project(":data"))
    
    // AndroidX 核心库
    implementation(libs.androidx.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose UI
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    
    // Koin 依赖注入
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    
    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
```

### 5. 自定义配置

如果需要特殊配置，可以在模块的 `android` 块中覆盖：

```kotlin
android {
    namespace = "com.zyc.clover.yourmodule"
    
    // 覆盖默认配置
    defaultConfig {
        // 自定义配置
    }
}
```