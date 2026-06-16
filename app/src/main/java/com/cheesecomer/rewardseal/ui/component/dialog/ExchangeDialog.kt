package com.cheesecomer.rewardseal.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.cheesecomer.rewardseal.model.RewardMilestone

@Composable
fun ExchangeDialog(
    milestone: RewardMilestone,
    onRewardSelect: (milestone: RewardMilestone) -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
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
                    onRewardSelect(milestone)
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
