package com.zyc.clover.route

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import kotlin.reflect.KType

// 渐变 + 缩小
inline fun <reified T : Any> NavGraphBuilder.composableScale(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline sizeTransform:
    (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
    SizeTransform?)? =
        null,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        T::class,
        typeMap,
        deepLinks,
        enterTransition = {
            // 新页面：由内往外扩展 + 渐变显示
            fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f, animationSpec = tween(300))
        },
        exitTransition = {
            // 旧页面：向内缩放 + 渐变消失
            fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f, animationSpec = tween(300))
        }, popEnterTransition = {
            // 返回时：新页面（实际上是旧页面）由内往外扩展 + 渐变显示
            fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f, animationSpec = tween(300))
        }, popExitTransition = {
            // 返回时：旧页面（实际上是新页面）向内缩放 + 渐变消失
            fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f, animationSpec = tween(300))
        },
        sizeTransform,
        content
    )
}

//  滑动动画
inline fun <reified T : Any> NavGraphBuilder.composableSlide(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline sizeTransform:
    (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
    SizeTransform?)? =
        null,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        T::class,
        typeMap,
        deepLinks,
        enterTransition = {
            // 新页面：从右到左滑入 + 渐变显示
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
        },
        exitTransition = {
            // 旧页面：缩小消失 + 渐变消失
            scaleOut(targetScale = 0.8f, animationSpec = tween(300))
        },
        popEnterTransition = {
            // 返回时：新页面从小到大缩放 + 渐变显示
            scaleIn(initialScale = 0.8f, animationSpec = tween(300))
        },
        popExitTransition = {
            // 返回时：旧页面从左到右滑出 + 渐变消失
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
        },
        sizeTransform,
        content
    )
}

// 创建一个 composable 函数，用于创建带淡入淡出的导航页面
inline fun <reified T : Any> NavGraphBuilder.composableFaded(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline sizeTransform:
    (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
    SizeTransform?)? =
        null,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        T::class,
        typeMap,
        deepLinks,
        enterTransition = {
            // 新页面：逐渐呈现（不透明度从 0 到 100%）
            fadeIn(animationSpec = tween(400))
        }, exitTransition = {
            // 旧页面：逐渐变淡消失（不透明度从 100% 到 0）
            fadeOut(animationSpec = tween(300))
        }, popEnterTransition = {
            // 返回时：新页面（实际上是旧页面）逐渐呈现（不透明度从 0 到 100%）
            fadeIn(animationSpec = tween(300))
        }, popExitTransition = {
            // 返回时：旧页面（实际上是新页面）逐渐变淡消失（不透明度从 100% 到 0）
            fadeOut(animationSpec = tween(400))
        },
        sizeTransform,
        content
    )
}
