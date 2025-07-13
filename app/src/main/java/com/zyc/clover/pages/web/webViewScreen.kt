package com.zyc.clover.pages.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zyc.clover.route.LocalNavController


@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(url: String) {
    val navController = LocalNavController.current
    Scaffold(
        content = { padding ->
            Box(
                content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = padding.calculateTopPadding()),
                    content = {
                        // 使用 AndroidView 包装 WebView
                        AndroidView(
                            factory = { context ->
                                // 创建 WebView 实例
                                WebView(context).apply {
                                    // 设置 WebViewClient 以在当前 WebView 中加载 URL
                                    webViewClient = WebViewClient()

                                    // 设置 WebChromeClient 以处理 JavaScript 对话框、标题等
                                    webChromeClient = WebChromeClient()

                                    // 启用 JavaScript
                                    settings.javaScriptEnabled = true

                                    // 其他常用设置
                                    settings.domStorageEnabled = true
                                    settings.setSupportZoom(true)
                                    settings.builtInZoomControls = true
                                    settings.displayZoomControls = false
                                    settings.cacheMode = WebSettings.LOAD_DEFAULT

                                    // 加载初始 URL
                                    loadUrl(url)
                                }
                            },
                            update = { webView ->
                                // 当 URL 变化时更新 WebView
                                if (webView.url != url) {
                                    webView.loadUrl(url)
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                )
                FloatingActionButton(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding(), start = padding.calculateTopPadding() / 2)
                        .size(30.dp),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(100),
                    containerColor = Color.White.copy(alpha = 0.5f),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,    // 正常状态阴影
                        pressedElevation = 0.dp,   // 点击状态阴影
                        focusedElevation = 0.dp     // 聚焦状态阴影
                    ),
                    onClick = { navController.popBackStack() },
                    content = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "返回") }
                )
            })
        }
    )
}
