package com.cheesecomer.rewardseal.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.cheesecomer.rewardseal.model.RewardMilestone

@Composable
fun ExchangeDialog(
    milestone: RewardMilestone,
    modifier: Modifier = Modifier,
    onRewardSelect: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier.testTag("ExchangeDialog"),
        onDismissRequest = onDismissRequest,
        title = {
            Text("交換しますか？")
        },
        text = {
            Text("${milestone.reward} と交換しますか？")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    onRewardSelect()
                },
            ) {
                Text("交換する")
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
