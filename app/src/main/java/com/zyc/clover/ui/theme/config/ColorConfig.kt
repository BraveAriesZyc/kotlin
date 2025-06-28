package com.zyc.clover.ui.theme.config

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Stable




@Stable
val antDesign = ColorScheme(
    // --------------------- 主色调区域（Ant Design蓝色系）---------------------
    primary = Color(0xFF1890FF),                          // Ant Design主色调(#1890FF)
    onPrimary = Color.White,                              // 主色调文本（白色）
    primaryContainer = Color(0xFF0050B3),                // 主色调容器（蓝色深色调）
    onPrimaryContainer = Color.White,                     // 主色调容器文本（白色）
    inversePrimary = Color(0xFF40A9FF),                  // 反色主色调（蓝色浅色调）

    // --------------------- 辅助色调区域（Ant Design功能色）---------------------
    secondary = Color(0xFF52C41A),                        // Ant Design成功色(#52C41A)
    onSecondary = Color(0xFF000000),                      // 辅助色调文本（黑色）
    secondaryContainer = Color(0xFF389E0D),              // 辅助色调容器（绿色深色调）
    onSecondaryContainer = Color.White,                   // 辅助色调容器文本（白色）

    // --------------------- 第三色调区域（Ant Design扩展色）---------------------
    tertiary = Color(0xFFFAAD14),                         // Ant Design警告色(#FAAD14)
    onTertiary = Color(0xFF000000),                       // 第三色调文本（黑色）
    tertiaryContainer = Color(0xFFD48806),                // 第三色调容器（黄色深色调）
    onTertiaryContainer = Color.White,                    // 第三色调容器文本（白色）

    // --------------------- 背景与表面区域（Ant Design深色体系）---------------------
    background = Color(0xFF141414),                       // Ant Design全局背景色(#141414)
    onBackground = Color(0xFFF5F5F5),                     // 背景文本（主文本色）
    surface = Color(0xFF141414),                          // Ant Design基础表面色
    onSurface = Color(0xFFF5F5F5),                        // 表面文本（主文本色）
    surfaceVariant = Color(0xFF303030),                   // 表面变体（边框背景）
    onSurfaceVariant = Color(0xFFCCCCCC),                 // 表面变体文本（辅助文本色）
    surfaceTint = Color(0xFF1890FF),                      // 表面色调（主色调）
    inverseSurface = Color(0xFFFFFFFF),                  // 反色表面（白色）
    inverseOnSurface = Color(0xFF141414),                 // 反色表面文本（背景色）

    // --------------------- 状态色区域（Ant Design状态色）---------------------
    error = Color(0xFFF5222D),                            // Ant Design错误色(#F5222D)
    onError = Color.White,                                // 错误色文本（白色）
    errorContainer = Color(0xFFC4170C),                  // 错误容器（红色深色调）
    onErrorContainer = Color.White,                       // 错误容器文本（白色）

    // --------------------- 分隔与遮罩区域---------------------
    outline = Color(0xFF4F4F4F),                          // Ant Design基础边框色(#4F4F4F)
    outlineVariant = Color(0xFF8C8C8C),                   // Ant Design浅边框色(#8C8C8C)
    scrim = Color(0x80000000),                            // Ant Design覆盖层背景色(半透明黑色)

    // --------------------- 表面层次区域（Ant Design层次感）---------------------
    surfaceBright = Color(0xFF1E1E1E),                    // Ant Design浅填充色(#1E1E1E)
    surfaceContainer = Color(0xFF1E1E1E),                 // 基础表面容器（基础背景色）
    surfaceContainerHigh = Color(0xFF141414),             // 高级表面容器（全局背景色）
    surfaceContainerHighest = Color(0xFF0A0A0A),          // 最高级表面容器（深填充色）
    surfaceContainerLow = Color(0xFF1E1E1E),              // 低级表面容器（浅填充色）
    surfaceContainerLowest = Color(0xFF1E1E1E),           // 最低级表面容器（基础背景色）
    surfaceDim = Color(0xFF0A0A0A)                        // 暗淡表面（深填充色）
)
@Stable
val element = ColorScheme(
    // 主色调区域（ElementUI主色#409EFF）
    primary = Color(0xFF409EFF),                // ElementUI主色调(#409EFF)
    onPrimary = Color.White,                              // 主色调上的文本色（白色）
    primaryContainer = Color(0xFFE6F7FF),                // 主色调容器（主色浅色调）
    onPrimaryContainer = Color(0xFF0066CC),              // 主色调容器文本（主色深色调）
    inversePrimary = Color(0xFF3083E0),                  // 反色主色调（主色更深色调）

    // 辅助色调（ElementUI成功色#67C23A）
    secondary = Color(0xFF67C23A),            // ElementUI成功色(#67C23A)
    onSecondary = Color.White,                           // 辅助色调上的文本色（白色）
    secondaryContainer = Color(0xFFECF5EB),              // 辅助色调容器（成功色浅色调）
    onSecondaryContainer = Color(0xFF3A7E1D),            // 辅助色调容器文本（成功色深色调）

    // 第三色调（ElementUI警告色#E6A23C）
    tertiary = Color(0xFFE6A23C),                        // ElementUI警告色(#E6A23C)
    onTertiary = Color(0xFF333333),                       // 第三色调上的文本色（深色文本）
    tertiaryContainer = Color(0xFFFFF3E0),                // 第三色调容器（警告色浅色调）
    onTertiaryContainer = Color(0xFF9E6B00),              // 第三色调容器文本（警告色深色调）

    // 背景与表面区域（ElementUI基础背景）
    background = Color(0xFFF2F3F5),                       // 页面级背景色(#F2F3F5)
    onBackground = Color(0xFF303133),                     // 背景上的文本色(#303133)
    surface = Color(0xFFFFFFFF),                         // 基础表面色(#FFFFFF)
    onSurface = Color(0xFF303133),                        // 表面上的文本色(#303133)
    surfaceVariant = Color(0xFFE5E9F2),                   // 表面变体色(#E5E9F2)
    onSurfaceVariant = Color(0xFF909399),                 // 表面变体上的文本色(#909399)
    surfaceTint = Color(0xFF409EFF),                      // 表面色调（主色调）
    inverseSurface = Color(0xFF303133),                   // 反色表面(#303133)
    inverseOnSurface = Color(0xFFF5F7FA),                 // 反色表面上的文本色(#F5F7FA)

    // 错误色（ElementUI错误色#F56C6C）
    error = Color(0xFFF56C6C),                           // ElementUI错误色(#F56C6C)
    onError = Color.White,                                // 错误色上的文本色（白色）
    errorContainer = Color(0xFFFEF0F0),                   // 错误容器（错误色浅色调）
    onErrorContainer = Color(0xFF9E2B25),                 // 错误容器文本（错误色深色调）

    // 轮廓与遮罩色
    outline = Color(0xFFDCDFE6),                          // 轮廓色（基础边框色#DCDFE6）
    outlineVariant = Color(0xFFE4E7ED),                   // 轮廓变体色(#E4E7ED)
    scrim = Color(0x80000000),                            // 遮罩色（半透明黑色）

    // 表面层次色（ElementUI层级规范）
    surfaceBright = Color(0xFFFFFFFF),                    // 明亮表面(#FFFFFF)
    surfaceContainer = Color(0xFFF5F7FA),                 // 表面容器（浅填充色#F5F7FA）
    surfaceContainerHigh = Color(0xFFF0F2F5),             // 高级表面容器（基础填充色#F0F2F5）
    surfaceContainerHighest = Color(0xFFEBEDF0),          // 最高级表面容器（较深填充色#EBEDF0）
    surfaceContainerLow = Color(0xFFF5F7FA),              // 低级表面容器（浅填充色#F5F7FA）
    surfaceContainerLowest = Color(0xFFFAFAFA),           // 最低级表面容器（更浅填充色#FAFAFA）
    surfaceDim = Color(0xFFE6E8EB)                        // 暗淡表面（深填充色#E6E8EB）
)







data class ColorSchemeType(
    val title: String,
    val colorScheme: ColorScheme
)
