package com.cheesecomer.rewardstamp.feature.exchangeablesheet.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.ExchangeableSheet
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.navigation.BottomTab
import com.cheesecomer.rewardstamp.ui.component.RewardStampBottomBar
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.cheesecomer.rewardstamp.ui.theme.SheetBorder
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimary
import com.cheesecomer.rewardstamp.ui.theme.SheetSection
import kotlin.collections.forEach

@Composable
private fun SheetTitleLabel(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
    )
}

@Composable
private fun ExchangeableSheetCount(
    count: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = "たまったシート ${count}枚",
        style = MaterialTheme.typography.bodyMedium,
        color = SheetSection,
    )
}

@Composable
private fun ClosestMilestoneHeadline(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Icon(
                imageVector = Icons.Filled.Flag,
                contentDescription = null,
                modifier = Modifier.size(20.dp).align(Alignment.Center),
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Text(
            text = "つぎのごほうび",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SheetSection,
        )
    }
}

@Composable
private fun ClosestMilestone(
    closestMilestone: RewardMilestone,
    exchangeableSheetCount: Int,
    sheet: RewardSheet,
    modifier: Modifier = Modifier,
) {
    val remainingSheetCount = closestMilestone.requiredSheetCount - exchangeableSheetCount
    val remainingStampCount = remainingSheetCount * sheet.goalCount - sheet.currentCount

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ClosestMilestoneHeadline()

        Column(modifier = Modifier.padding(start = 20.dp)) {
            Text(
                text = closestMilestone.reward,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = SheetPrimary,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "あと ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.alignByBaseline(),
                )

                Text(
                    text = remainingStampCount.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = SheetPrimary,
                    modifier = Modifier.alignByBaseline(),
                )
                Text(
                    " 回がんばると交換できるよ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.alignByBaseline(),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "あとシート ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.alignByBaseline(),
                )

                Text(
                    text = remainingSheetCount.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = SheetPrimary,
                    modifier = Modifier.alignByBaseline(),
                )
                Text(
                    " 枚",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.alignByBaseline(),
                )
            }
        }
    }
}

@Composable
private fun ExchangeableMilestonesHeadline() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_gift),
                contentDescription = null,
                modifier = Modifier.size(20.dp).align(Alignment.Center),
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Text(
            text = "交換できるごほうび",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = SheetSection,
        )
    }
}

@Composable
private fun ExchangeableMilestones(
    exchangeableMilestones: List<RewardMilestone>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ExchangeableMilestonesHeadline()
        exchangeableMilestones.forEach { milestone ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 20.dp),
            ) {
                Text(
                    text = "${milestone.requiredSheetCount}枚で ",
                    modifier = Modifier.alignByBaseline(),
                )
                Text(
                    text = milestone.reward,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SheetPrimary,
                    modifier = Modifier.alignByBaseline(),
                )
            }
        }
    }
}

@Composable
private fun ExchangeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text("受け取った")
    }
}

@Composable
fun ExchangeableSheetCard(
    sheet: ExchangeableSheet,
    modifier: Modifier = Modifier,
    onRewardReceiveClick: () -> Unit = { },
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border =
            BorderStroke(
                1.dp,
                SheetBorder,
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SheetTitleLabel(
                    title = sheet.rewardSheet.title,
                    modifier = Modifier.padding(start = 0.dp),
                )
                Spacer(Modifier.weight(1f))
                ExchangeableSheetCount(sheet.exchangeableSheetCount)
            }

            ExchangeableMilestones(
                exchangeableMilestones = sheet.exchangeableMilestones,
                modifier = Modifier.padding(start = 0.dp, end = 16.dp, bottom = 16.dp),
            )

            if (sheet.closestMilestone != null) {
                HorizontalDivider()

                ClosestMilestone(
                    closestMilestone = sheet.closestMilestone,
                    exchangeableSheetCount = sheet.exchangeableSheetCount,
                    sheet = sheet.rewardSheet,
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, bottom = 16.dp),
                )
            }

            HorizontalDivider()

            ExchangeButton(
                onClick = onRewardReceiveClick,
                modifier = Modifier.testTag("ExchangeableSheetCard.${sheet.rewardSheet.id}.ExchangeButton"),
            )
        }
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber", "LongMethod")
@Composable
private fun ExchangeableSheetCardPreview() {
    RewardStampTheme {
        Scaffold(
            bottomBar = {
                RewardStampBottomBar(
                    selectedTab = BottomTab.Sheets,
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
            ) {
                ExchangeableSheetCard(
                    sheet =
                        ExchangeableSheet(
                            rewardSheet =
                                RewardSheet(
                                    id = 1L,
                                    title = "おかたずけ",
                                    currentCount = 0,
                                    goalCount = 10,
                                ),
                            exchangeableSheetCount = 1,
                            exchangeableMilestones =
                                listOf(
                                    RewardMilestone(
                                        id = 1L,
                                        sheetId = 1L,
                                        requiredSheetCount = 1,
                                        reward = "アイスクリーム",
                                    ),
                                ),
                            closestMilestone = null,
                        ),
                )

                ExchangeableSheetCard(
                    sheet =
                        ExchangeableSheet(
                            rewardSheet =
                                RewardSheet(
                                    id = 1L,
                                    title = "おかたずけ",
                                    currentCount = 5,
                                    goalCount = 20,
                                ),
                            exchangeableSheetCount = 1,
                            exchangeableMilestones =
                                listOf(
                                    RewardMilestone(
                                        id = 1L,
                                        sheetId = 1L,
                                        requiredSheetCount = 1,
                                        reward = "アイスクリーム",
                                    ),
                                    RewardMilestone(
                                        id = 1L,
                                        sheetId = 1L,
                                        requiredSheetCount = 2,
                                        reward = "アイスクリーム",
                                    ),
                                ),
                            closestMilestone =
                                RewardMilestone(
                                    id = 1L,
                                    sheetId = 1L,
                                    requiredSheetCount = 2,
                                    reward = "アイスクリーム",
                                ),
                        ),
                )
            }
        }
    }
}
