package com.zyc.feature.ui_showcase.screen.network.upload

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zyc.core.ui.components.common.ZAppBar
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FileUploadComponentsScreen(onBack: () -> Unit = {}) {
    // 创建协程作用域（与 Composable 生命周期绑定）
    val coroutineScope = rememberCoroutineScope()
    val uploadViewModel = viewModel<UploadViewModel>()
    val uploadProgress by uploadViewModel.uploadProgress.collectAsState()
    val uploading by uploadViewModel.uploading.collectAsState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data?.data == null) {
                Log.i("===", "fei1")
            } else {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let { uri ->
                    coroutineScope.launch {
                        // 在这里处理选中的图片（例如显示或上传）
                        uploadViewModel.uploadFile(uri)
                    }
                }
            }
        }

    }
    Scaffold(
        topBar = {
            ZAppBar(title = "文件上传")
        },
        content = { pd ->
            Column(
                modifier = Modifier.padding(top = pd.calculateTopPadding()),
                content = {
                    Row(
                        content = {
                            Button(
                                onClick = {
                                    launcher.launch(
                                        Intent(Intent.ACTION_PICK).apply {
                                            type = "image/* video/*" // 指定选择图片类型
                                        }
                                    )

                                },
                                content = {
                                    Text(
                                        text = if (uploading) "上传中 ${uploadProgress}%" else "上传图片"
                                    )
                                }
                            )
                        }
                    )

                }
            )

        }
    )
}