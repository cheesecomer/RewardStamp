package com.cheesecomer.rewardseal.feature.sheet.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MilestoneFormList(
    milestones: List<RewardMilestoneUiState>,
    onRequiredCompletionsChange: (index: Int, value: String) -> Unit,
    onRewardChange: (index: Int, value: String) -> Unit,
    onAddClick: () -> Unit,
    onRemoveClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "ごほうび",
            style = MaterialTheme.typography.titleMedium,
        )

        milestones.forEachIndexed { index, milestone ->
            MilestoneFormItem(
                index = index,
                milestone = milestone,
                canRemove = milestones.size > 1,
                onRequiredCompletionsChange = onRequiredCompletionsChange,
                onRewardChange = onRewardChange,
                onRemoveClick = onRemoveClick,
            )
        }

        Button(
            onClick = onAddClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("＋ ごほうびを追加")
        }
    }
}

@Composable
private fun MilestoneFormItem(
    index: Int,
    milestone: RewardMilestoneUiState,
    canRemove: Boolean,
    onRequiredCompletionsChange: (index: Int, value: String) -> Unit,
    onRewardChange: (index: Int, value: String) -> Unit,
    onRemoveClick: (index: Int) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = milestone.requiredCompletions,
                    onValueChange = {
                        onRequiredCompletionsChange(index, it)
                    },
                    label = {
                        Text("枚数")
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                    singleLine = true,
                    modifier = Modifier.width(96.dp),
                )

                Text("シート達成で")

                if (canRemove) {
                    IconButton(
                        onClick = {
                            onRemoveClick(index)
                        },
                    ) {
                        Text("削除")
                    }
                }
            }

            OutlinedTextField(
                value = milestone.reward,
                onValueChange = {
                    onRewardChange(index, it)
                },
                label = {
                    Text("ごほうび")
                },
                placeholder = {
                    Text("500円までのオモチャ")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
