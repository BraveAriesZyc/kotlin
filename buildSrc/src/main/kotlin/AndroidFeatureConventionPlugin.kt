import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

/**
 * Android 功能模块通用配置插件
 * 
 * 该插件为 Android 功能模块提供统一的构建配置，包括：
 * - 自动应用必要的 Android 库插件
 * - 统一的 SDK 版本配置
 * - Compose 支持
 * - 通用依赖配置
 * 
 * 使用方式：
 * ```kotlin
 * plugins {
 *     id("android-feature-convention")
 * }
 * ```
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    
    /**
     * 应用插件到目标项目
     * 
     * @param target 目标项目
     */
    override fun apply(target: Project) {
        with(target) {
            // 应用必要的插件
            with(pluginManager) {
                apply("android-library-convention") // Android 库插件
                // 注意：序列化插件应该在需要的模块中单独应用，而不是在这里统一应用
            }

            // 配置 Android 扩展
            extensions.configure<LibraryExtension> {
                // 构建特性
                buildFeatures {
                    compose = true // 启用 Jetpack Compose 支持
                }
            }
            
            // 添加通用依赖
            dependencies {
                // 核心模块依赖 - 优化后的依赖架构
                "implementation"(project(":core:common"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:model"))
                "implementation"(project(":core:data")) // 通过data层访问network和database
                
                // 测试依赖 - 使用字符串形式的依赖
                "testImplementation"("junit:junit:4.13.2")
                "androidTestImplementation"("androidx.test.ext:junit:1.1.5")
                "androidTestImplementation"("androidx.test.espresso:espresso-core:3.5.1")
                "androidTestImplementation"("androidx.compose.ui:ui-test-junit4")
                "debugImplementation"("androidx.compose.ui:ui-tooling")
                "debugImplementation"("androidx.compose.ui:ui-test-manifest")
            }
        }
    }
}