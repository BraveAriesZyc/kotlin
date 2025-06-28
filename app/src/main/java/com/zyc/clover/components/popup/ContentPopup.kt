package com.zyc.clover.components.popup


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun ContentPopup(
    title: String = "提示",
    expanded: MutableState<Boolean>,
    content: @Composable () -> Unit,
    onSubmit: () -> Unit
) {
    if (expanded.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = title, style = MaterialTheme.typography.bodySmall)
            },
            text = {
                content()
            },
            confirmButton = {
                TextButton(onClick = {
                    onSubmit()
                    expanded.value = false
                }) {
                    Text(text = "确定")

                }
            },
            dismissButton = {
                TextButton(onClick = {
                    expanded.value = false
                }) {
                    Text(text = "取消")
                }
            }
        )
    }
}
