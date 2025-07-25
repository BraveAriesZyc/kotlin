import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

/**
 * Android 应用模块通用配置插件
 * 
 * 该插件为 Android 应用模块提供统一的构建配置，包括：
 * - 自动应用必要的 Android 和 Kotlin 插件
 * - 统一的 SDK 版本配置
 * - 标准的构建类型配置
 * - Java 和 Kotlin 编译选项
 * - Compose 支持
 * 
 * 使用方式：
 * ```kotlin
 * plugins {
 *     id("android-application-convention")
 * }
 * ```
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    
    /**
     * 应用插件到目标项目
     * 
     * @param target 目标项目
     */
    override fun apply(target: Project) {
        with(target) {
            // 应用必要的插件
            with(pluginManager) {
                apply("com.android.application") // Android 应用插件
                apply("org.jetbrains.kotlin.android") // Kotlin Android 插件
            }

            // 配置 Android 扩展
            extensions.configure<BaseAppModuleExtension> {
                // 编译 SDK 版本
                compileSdk = 35

                // 默认配置
                defaultConfig {
                    minSdk = 34 // 最小支持的 Android API 级别
                    targetSdk = 35 // 目标 Android API 级别
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // 测试运行器
                }

                // 构建类型配置
                buildTypes {
                    release {
                        isMinifyEnabled = false // 是否启用代码混淆
                        // ProGuard 配置文件
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                // Java 编译选项
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11 // Java 源码兼容性
                    targetCompatibility = JavaVersion.VERSION_11 // Java 目标兼容性
                }

                // 构建特性
                buildFeatures {
                    compose = true // 启用 Jetpack Compose 支持
                }
            }
            
            // 配置 Kotlin 编译选项
            extensions.configure<KotlinAndroidProjectExtension> {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11) // Kotlin JVM 目标版本
                }
            }
        }
    }
}