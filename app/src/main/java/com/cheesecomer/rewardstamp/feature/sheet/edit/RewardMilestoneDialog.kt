package com.cheesecomer.rewardstamp.feature.sheet.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.ui.RewardStampTextFieldDefaults

@Composable
private fun RewardMilestoneDialogText(
    requiredSheetCount: String,
    reward: String,
    onRequiredSheetCountChange: (String) -> Unit,
    onRewardChange: (String) -> Unit,
) {
    val tagPrefix = "RewardMilestonesSection.RewardMilestoneDialog.Text"
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("$tagPrefix.Reward"),
            value = reward,
            onValueChange = onRewardChange,
            label = {
                Text(stringResource(R.string.milestone_dialog_reward_label))
            },
            placeholder = {
                Text(stringResource(R.string.milestone_dialog_reward_placeholder))
            },
            singleLine = true,
            colors = RewardStampTextFieldDefaults.colors(),
        )
        OutlinedTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("$tagPrefix.RequiredSheetCount"),
            value = requiredSheetCount,
            onValueChange = onRequiredSheetCountChange,
            label = {
                Text(stringResource(R.string.milestone_dialog_required_label))
            },
            suffix = {
                Text(stringResource(R.string.common_sheet_count_unit))
            },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            singleLine = true,
            colors = RewardStampTextFieldDefaults.colors(),
        )
    }
}

@Composable
fun RewardMilestoneDialog(
    requiredSheetCount: String,
    reward: String,
    isEdit: Boolean,
    onRequiredSheetCountChange: (String) -> Unit,
    onRewardChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    val tagPrefix = "RewardMilestonesSection.RewardMilestoneDialog"
    AlertDialog(
        modifier = Modifier.testTag(tagPrefix),
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text =
                    stringResource(
                        if (isEdit) R.string.milestone_dialog_title_edit else R.string.milestone_dialog_title_add,
                    ),
            )
        },
        text = {
            RewardMilestoneDialogText(
                requiredSheetCount = requiredSheetCount,
                reward = reward,
                onRequiredSheetCountChange = onRequiredSheetCountChange,
                onRewardChange = onRewardChange,
            )
        },
        confirmButton = {
            TextButton(
                enabled =
                    (requiredSheetCount.toIntOrNull() ?: 0) >= 1 &&
                        reward.isNotBlank(),
                onClick = onConfirmClick,
                modifier = Modifier.testTag("$tagPrefix.Confirm"),
            ) {
                Text(
                    stringResource(if (isEdit) R.string.common_save else R.string.common_add),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(stringResource(R.string.common_cancel))
            }
        },
    )
}
