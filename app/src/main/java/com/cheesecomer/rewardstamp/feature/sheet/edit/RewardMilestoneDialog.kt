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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
                Text("ごほうびは？")
            },
            placeholder = {
                Text("例：アイス")
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
                Text("何枚と交換する？")
            },
            suffix = {
                Text("枚")
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
                text = if (isEdit) "ごほうびを編集" else "ごほうびを追加",
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
                    requiredSheetCount.isNotBlank() &&
                        reward.isNotBlank(),
                onClick = onConfirmClick,
                modifier = Modifier.testTag("$tagPrefix.Confirm"),
            ) {
                Text(
                    if (isEdit) "保存" else "追加",
                )
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
