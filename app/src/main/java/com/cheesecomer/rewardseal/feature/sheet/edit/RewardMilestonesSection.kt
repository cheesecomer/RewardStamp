package com.cheesecomer.rewardseal.feature.sheet.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardseal.model.RewardMilestone
import com.cheesecomer.rewardseal.ui.RewardSealTextFieldDefaults
import com.cheesecomer.rewardseal.ui.theme.SheetBorder

@Composable
private fun RewardMilestoneRow(
    milestone: RewardMilestone,
    modifier: Modifier = Modifier,
    canEdit: Boolean = true,
    canRemove: Boolean = true,
    onEditClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${milestone.requiredSheetCount}枚で",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = milestone.reward.ifBlank { "ごほうび" },
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (canEdit) {
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "編集",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }

        if (canRemove) {
            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "削除",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

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
                    .testTag("$tagPrefix.RequiredSheetCount"),
            value = requiredSheetCount,
            onValueChange = onRequiredSheetCountChange,
            label = {
                Text("必要な枚数")
            },
            suffix = {
                Text("枚")
            },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            singleLine = true,
            colors = RewardSealTextFieldDefaults.colors(),
        )

        OutlinedTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("$tagPrefix.Reward"),
            value = reward,
            onValueChange = onRewardChange,
            label = {
                Text("ごほうび")
            },
            placeholder = {
                Text("例：アイス")
            },
            singleLine = true,
            colors = RewardSealTextFieldDefaults.colors(),
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

private const val NEW_MILESTONE_INDEX = -1

private fun RewardMilestone.toUiState(): RewardMilestoneUiState =
    RewardMilestoneUiState(
        id = id,
        requiredSheetCount = requiredSheetCount.toString(),
        reward = reward,
    )

private data class RewardMilestoneUiState(
    val id: Long = 0,
    val sheetId: Long? = 0,
    val requiredSheetCount: String = "",
    val reward: String = "",
) {
    fun toModal() =
        RewardMilestone(
            id = id,
            sheetId = sheetId ?: 0L,
            requiredSheetCount = requiredSheetCount.toIntOrNull() ?: 0,
            reward = reward,
        )
}

private data class EditingRewardMilestone(
    val index: Int,
    val milestone: RewardMilestoneUiState,
) {
    val isNew: Boolean
        get() = index == NEW_MILESTONE_INDEX
}

@Composable
private fun RewardMilestoneDialogHost(
    editingMilestone: EditingRewardMilestone?,
    onEditingMilestoneChange: (EditingRewardMilestone?) -> Unit,
    onCreateClick: (RewardMilestone) -> Unit,
    onUpdateClick: (index: Int, milestone: RewardMilestone) -> Unit,
) {
    val editing = editingMilestone ?: return

    RewardMilestoneDialog(
        requiredSheetCount = editing.milestone.requiredSheetCount,
        reward = editing.milestone.reward,
        isEdit = !editing.isNew,
        onRequiredSheetCountChange = {
            onEditingMilestoneChange(
                editing.copy(
                    milestone = editing.milestone.copy(requiredSheetCount = it),
                ),
            )
        },
        onRewardChange = {
            onEditingMilestoneChange(
                editing.copy(
                    milestone = editing.milestone.copy(reward = it),
                ),
            )
        },
        onDismissRequest = {
            onEditingMilestoneChange(null)
        },
        onConfirmClick = {
            if (editing.isNew) {
                onCreateClick(editing.milestone.toModal())
            } else {
                onUpdateClick(editing.index, editing.milestone.toModal())
            }

            onEditingMilestoneChange(null)
        },
    )
}

@Composable
private fun EmptyLabel() {
    Column {
        Text(
            text = "まだごほうびがありません",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(
            color = SheetBorder.copy(alpha = 0.6f),
        )
    }
}

@Composable
private fun RewardMilestoneRows(
    milestones: List<RewardMilestone>,
    onEditClick: (EditingRewardMilestone) -> Unit,
    onRemoveClick: (Int) -> Unit,
) {
    milestones.forEachIndexed { index, milestone ->
        RewardMilestoneRow(
            milestone = milestone,
            canRemove = milestones.size > 1,
            onEditClick = {
                val editingMilestone =
                    EditingRewardMilestone(
                        index = index,
                        milestone = milestone.toUiState(),
                    )
                onEditClick(editingMilestone)
            },
            onRemoveClick = { onRemoveClick(index) },
        )
        HorizontalDivider(
            color = SheetBorder.copy(alpha = 0.6f),
        )
    }
}

@Composable
fun RewardMilestonesSection(
    milestones: List<RewardMilestone>,
    onCreateClick: (RewardMilestone) -> Unit,
    onRemoveClick: (Int) -> Unit,
    onUpdateClick: (Int, RewardMilestone) -> Unit,
    modifier: Modifier = Modifier,
) {
    var editingMilestone by remember {
        mutableStateOf<EditingRewardMilestone?>(null)
    }
    RewardMilestoneDialogHost(
        editingMilestone = editingMilestone,
        onEditingMilestoneChange = {
            editingMilestone = it
        },
        onCreateClick = onCreateClick,
        onUpdateClick = onUpdateClick,
    )

    Card(
        modifier = modifier,
        border =
            BorderStroke(
                1.dp,
                SheetBorder,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
        ) {
            SheetEditSectionTitle(text = "満タンカードと交換できるごほうび")
            Text(
                modifier = Modifier.padding(start = 32.dp),
                text = "ごほうびを複数登録できます",
                style = MaterialTheme.typography.bodySmall,
            )

            RewardMilestoneRows(
                milestones = milestones,
                onEditClick = { editingMilestone = it },
                onRemoveClick = onRemoveClick,
            )

            if (milestones.isEmpty()) {
                EmptyLabel()
            }

            Button(
                onClick = {
                    editingMilestone =
                        EditingRewardMilestone(
                            index = NEW_MILESTONE_INDEX,
                            milestone = RewardMilestoneUiState(),
                        )
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .testTag("MilestoneFormList.addMilestoneButton"),
            ) {
                Text("＋ ごほうびを追加")
            }
        }
    }
}
