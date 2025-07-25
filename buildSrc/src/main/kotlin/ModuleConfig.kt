/**
 * 模块配置常量
 * 
 * 该对象定义了项目中所有模块共用的配置常量，确保版本一致性。
 * 这些常量被 buildSrc 中的通用配置插件使用。
 * 
 * 使用方法：
 * 1. 应用模块使用 id("android-application-convention") 插件
 * 2. 库模块使用 id("android-library-convention") 插件
 * 3. 只需要设置 namespace，其他配置都会自动应用
 * 4. 依赖管理请使用 gradle/libs.versions.toml 中定义的 libs 引用
 * 
 * 应用模块示例：
 * ```kotlin
 * plugins {
 *     id("android-application-convention")
 *     alias(libs.plugins.composeCompiler)
 * }
 * 
 * android {
 *     namespace = "com.zyc.clover"
 *     defaultConfig {
 *         applicationId = "com.zyc.clover"
 *         versionCode = 110
 *         versionName = "1.0.5"
 *     }
 * }
 * ```
 * 
 * 库模块示例：
 * ```kotlin
 * plugins {
 *     id("android-library-convention")
 *     alias(libs.plugins.composeCompiler) // 如果需要 Compose
 * }
 * 
 * android {
 *     namespace = "com.zyc.clover.yourmodule"
 * }
 * 
 * dependencies {
 *     implementation(project(":common"))
 *     implementation(project(":data"))
 *     implementation(libs.androidx.core.ktx)
 *     // 使用 libs 引用添加其他依赖...
 * }
 * ```
 */

/**
 * 模块配置常量对象
 * 
 * 包含项目中所有模块共用的配置参数，确保版本一致性。
 */
object ModuleConfig {
    /** 编译 SDK 版本 */
    const val COMPILE_SDK = 35
    
    /** 最小支持的 Android API 级别 */
    const val MIN_SDK = 34
    
    /** Kotlin JVM 目标版本 */
    const val JVM_TARGET = "11"
    
    /** Kotlin Compose 编译器扩展版本 */
    const val KOTLIN_COMPILER_EXTENSION_VERSION = "1.5.8"
}