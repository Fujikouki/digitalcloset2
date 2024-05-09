package com.example.digitalcloset2.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DeletingDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("削除しますか？") },
        confirmButton = {
            Button(
                onClick = {
                    onDelete()
                    onDismiss()
                }
            ) {
                Text("削除")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("キャンセル")
            }
        }
    )
}