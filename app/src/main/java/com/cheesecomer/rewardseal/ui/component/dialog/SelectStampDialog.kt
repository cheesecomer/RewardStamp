package com.cheesecomer.rewardseal.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.ui.component.StampTypeGrid

@Composable
fun SelectStampDialog(
    onStampTypeSelected: (stampType: StampType) -> Unit,
    onDismissRequest: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("スタンプを選ぶ")
        },
        text = {
            StampTypeGrid(
                onStampTypeClick = onStampTypeSelected
            )
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("キャンセル")
            }
        }
    )
}