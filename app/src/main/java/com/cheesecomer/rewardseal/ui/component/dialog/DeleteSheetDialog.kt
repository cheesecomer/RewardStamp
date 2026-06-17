package com.cheesecomer.rewardseal.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun DeleteSheetDialog(
    modifier: Modifier = Modifier,
    onDeleteRequest: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier.testTag("DeleteSheetDialog"),
        onDismissRequest = onDismissRequest,
        title = {
            Text("シートを削除しますか？")
        },
        text = {
            Text("このシートは削除されます。")
        },
        confirmButton = {
            TextButton(
                onClick = onDeleteRequest,
                modifier = Modifier.testTag("DeleteSheetDialog.DeleteButton"),
            ) {
                Text("削除")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text("キャンセル")
            }
        },
    )
}
