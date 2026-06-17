package com.cheesecomer.rewardseal.ui.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardseal.model.RewardMilestone

@Composable
fun ChoiceRewardDialog(
    milestones: List<RewardMilestone>,
    modifier: Modifier = Modifier,
    onRewardSelect: (milestone: RewardMilestone) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier.testTag("ChoiceRewardDialog"),
        onDismissRequest = onDismissRequest,
        title = {
            Text("ごほうびを選んでね")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                milestones.forEach { milestone ->
                    TextButton(
                        onClick = {
                            onDismissRequest()
                            onRewardSelect(milestone)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "${milestone.reward}（${milestone.requiredCompletions}枚）",
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
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
