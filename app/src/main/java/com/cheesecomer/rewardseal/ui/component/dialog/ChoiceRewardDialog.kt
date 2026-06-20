package com.cheesecomer.rewardseal.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardseal.R
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.RewardMilestone
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import com.cheesecomer.rewardseal.ui.theme.SheetCard
import com.cheesecomer.rewardseal.ui.theme.SheetDivider
import com.cheesecomer.rewardseal.ui.theme.SheetPrimary
import com.cheesecomer.rewardseal.ui.theme.SheetPrimaryContainer
import com.cheesecomer.rewardseal.ui.theme.SheetText

@Composable
private fun RewardExchangeOptionRow(
    selected: Boolean,
    reward: String,
    sheetCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors =
                RadioButtonDefaults.colors(
                    selectedColor = SheetPrimary,
                    unselectedColor = SheetText.copy(alpha = 0.4f),
                ),
        )
        Text(
            text = reward,
            modifier = Modifier.weight(1f),
            color = SheetText,
        )

        Text(
            text = "(${sheetCount}枚)",
            color = SheetText,
        )
    }
}

@Composable
fun ChoiceRewardDialogHeadline(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(SheetPrimaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_gift),
                contentDescription = null,
                tint = SheetPrimary,
                modifier = Modifier.size(32.dp),
            )
        }

        Text(
            text = "どれと交換する？",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = SheetText,
        )
        Text(
            text = "ごほうびをえらんでね",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceRewardDialog(
    milestones: List<RewardMilestone>,
    modifier: Modifier = Modifier,
    onRewardSelect: (milestone: RewardMilestone) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    var selectedId by remember { mutableStateOf<Long?>(null) }
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            shape = RoundedCornerShape(28.dp),
            color = SheetCard,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ChoiceRewardDialogHeadline()

                HorizontalDivider(color = SheetDivider)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    milestones.forEach { milestone ->
                        RewardExchangeOptionRow(
                            modifier = Modifier.testTag("ChoiceRewardDialog.Rewards.${milestone.id}"),
                            selected = selectedId == milestone.id,
                            reward = milestone.reward,
                            sheetCount = milestone.requiredSheetCount,
                            onClick = { selectedId = milestone.id },
                        )
                    }
                }

                HorizontalDivider(
                    color = SheetDivider,
                    modifier = Modifier.padding(top = 4.dp),
                )

                DialogButtons(
                    confirmText = "交換する",
                    enable = selectedId != null,
                    onDismissClick = onDismissRequest,
                    onConfirmClick = {
                        onRewardSelect(milestones.first { it.id == selectedId })
                        onDismissRequest()
                    },
                )
            }
        }
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun ChoiceRewardDialogPreview() {
    RewardSealTheme {
        ChoiceRewardDialog(
            milestones =
                listOf(
                    RewardMilestone(
                        id = 0L,
                        sheetId = 1L,
                        requiredSheetCount = 1,
                        reward = "スーパーカップ",
                    ),
                    RewardMilestone(
                        id = 0L,
                        sheetId = 1L,
                        requiredSheetCount = 2,
                        reward = "ハーゲンダッツ",
                    ),
                ),
        )
    }
}
