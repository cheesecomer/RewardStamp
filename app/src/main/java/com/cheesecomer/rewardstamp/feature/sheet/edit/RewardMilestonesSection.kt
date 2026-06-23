package com.cheesecomer.rewardstamp.feature.sheet.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.cheesecomer.rewardstamp.ui.theme.SheetBorder

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
private fun RewardMilestonesSectionHeadline(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        SheetEditSectionTitle(text = "ごほうびは？")
        Text(
            modifier = Modifier.padding(start = 32.dp),
            text =
                "最後までがんばったシートと交換するごほうびです。\n" +
                    "ごほうびは何個でも登録できます",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun AddMilestoneButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text("＋ ごほうびを追加")
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
            RewardMilestonesSectionHeadline()
            RewardMilestoneRows(
                milestones = milestones,
                onEditClick = { editingMilestone = it },
                onRemoveClick = onRemoveClick,
            )

            if (milestones.isEmpty()) {
                EmptyLabel()
            }

            AddMilestoneButton(
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
            )
        }
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Composable
private fun RewardMilestonesSectionPreview() {
    RewardStampTheme {
        Scaffold {
            RewardMilestonesSection(
                modifier = Modifier.padding(it),
                milestones = emptyList(),
                onRemoveClick = {},
                onUpdateClick = { _, _ -> },
                onCreateClick = {},
            )
        }
    }
}
