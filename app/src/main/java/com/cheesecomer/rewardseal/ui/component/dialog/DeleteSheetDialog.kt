package com.cheesecomer.rewardseal.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun DeleteSheetDialog(
    onDeleteRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("シートを削除しますか？")
        },
        text = {
            Text("このシートは削除されます。")
        },
        confirmButton = {
            TextButton(
                onClick = onDeleteRequest
            ) {
                Text("削除")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("キャンセル")
            }
        }
    )
}