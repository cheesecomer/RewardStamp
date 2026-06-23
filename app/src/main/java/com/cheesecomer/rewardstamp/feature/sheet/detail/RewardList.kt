package com.cheesecomer.rewardstamp.feature.sheet.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.ui.theme.Nikumaru
import com.cheesecomer.rewardstamp.ui.theme.SheetBorder
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimary
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimaryContainer
import com.cheesecomer.rewardstamp.ui.theme.SheetText

@Composable
private fun RequiredSheetCount(
    requiredSheetCount: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_cherry_blossom_fill),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = SheetPrimaryContainer,
        )
        Row(modifier = Modifier.align(Alignment.Center)) {
            Text(
                text = requiredSheetCount.toString(),
                color = SheetText,
                modifier = Modifier.alignByBaseline(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "枚",
                color = SheetText,
                modifier = Modifier.alignByBaseline(),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun MilestoneRow(
    requiredSheetCount: Int,
    reward: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RequiredSheetCount(
            requiredSheetCount = requiredSheetCount,
        )
        Text(
            text = reward,
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = SheetPrimary,
        )
    }
}

@Composable
private fun ExchangeableRewardsCardHeadline(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_gift),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = SheetPrimary,
        )

        Text(
            text = "いま もらえる ごほうび",
            style = MaterialTheme.typography.titleMedium,
            fontFamily = Nikumaru,
            color = SheetText,
        )
    }
}

@Composable
private fun ExchangeableRewardsCard(
    exchangeableRewards: List<RewardMilestone>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        border =
            BorderStroke(
                1.dp,
                SheetBorder,
            ),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            ExchangeableRewardsCardHeadline(
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                exchangeableRewards.forEach {
                    MilestoneRow(
                        requiredSheetCount = it.requiredSheetCount,
                        reward = it.reward,
                    )
                }
            }
        }
    }
}

@Composable
private fun LockedRewardsCardHeadline(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Flag,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = SheetPrimary,
        )

        Text(
            text = "つぎの ごほうび",
            style = MaterialTheme.typography.titleMedium,
            fontFamily = Nikumaru,
            color = SheetText,
        )
    }
}

@Composable
private fun LockedRewardsCard(
    lockedRewards: List<RewardMilestone>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        border =
            BorderStroke(
                1.dp,
                SheetBorder,
            ),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            LockedRewardsCardHeadline(
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                lockedRewards.forEach {
                    MilestoneRow(
                        requiredSheetCount = it.requiredSheetCount,
                        reward = it.reward,
                    )
                }
            }
        }
    }
}

@Composable
fun RewardList(
    exchangeableRewards: List<RewardMilestone>,
    lockedRewards: List<RewardMilestone>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (exchangeableRewards.isNotEmpty()) {
            item {
                ExchangeableRewardsCard(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .testTag("RewardList.ExchangeableRewardsCard"),
                    exchangeableRewards = exchangeableRewards,
                )
            }
        }
        if (lockedRewards.isNotEmpty()) {
            item {
                LockedRewardsCard(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .testTag("RewardList.LockedRewardsCard"),
                    lockedRewards = lockedRewards,
                )
            }
        }
    }
}
