package com.cheesecomer.rewardseal.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.ui.component.StampTypeGrid

@Composable
fun SelectStampDialog(
    modifier: Modifier = Modifier,
    onStampTypeSelect: (stampType: StampType) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier.testTag("SelectStampDialog"),
        onDismissRequest = onDismissRequest,
        title = {
            Text("スタンプを選ぶ")
        },
        text = {
            StampTypeGrid(
                onStampTypeClick = onStampTypeSelect,
            )
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text("キャンセル")
            }
        },
    )
}
