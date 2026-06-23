package com.cheesecomer.rewardstamp.feature.sheet.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.model.StampType
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.cheesecomer.rewardstamp.ui.theme.SheetBorder

private const val GOAL_BADGE_CORNER_PERCENT = 50

@Composable
private fun GoalBadge(
    goalCount: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(GOAL_BADGE_CORNER_PERCENT),
        color = color,
    ) {
        Text(
            text = "ゴール $goalCount",
            modifier =
                Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 2.dp,
                ),
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun LastStampView(
    stampType: StampType?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .size(80.dp)
                    .border(
                        width = 3.dp,
                        color = color.copy(alpha = 0.75f),
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            if (stampType != null) {
                Icon(
                    painter = painterResource(stampType.iconRes),
                    contentDescription = stampType.id,
                    modifier = Modifier.size(44.dp).testTag("SheetCard.LastStampView.Stamp"),
                    tint = color.copy(alpha = 0.9f),
                )
            }
        }

        Text(
            text = "さいごのスタンプ",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 9.sp,
        )
    }
}

@Composable
private fun ProgressInfo(
    title: String,
    currentCount: Int,
    goalCount: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )

        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = currentCount.toString(),
                color = color,
                style = MaterialTheme.typography.displayMedium,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "/ $goalCount 回",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }
    }
}

private const val CELLS_IN_ROW = 10

@Composable
private fun ProgressStampRow(
    currentCount: Int,
    goalCount: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val cellNum = CELLS_IN_ROW
        val spacing = 4.dp
        val iconSize = (this.maxWidth - spacing * (CELLS_IN_ROW - 1)) / cellNum
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            repeat(
                goalCount / CELLS_IN_ROW +
                    if (goalCount % CELLS_IN_ROW == 0) {
                        0
                    } else {
                        1
                    },
            ) {
                ProgressStampLine(
                    currentCount = currentCount,
                    count =
                        if (goalCount / CELLS_IN_ROW > it) {
                            CELLS_IN_ROW
                        } else {
                            goalCount % CELLS_IN_ROW
                        },
                    startIndex = it * CELLS_IN_ROW,
                    color = color,
                    iconSize = iconSize,
                )
            }
        }
    }
}

@Composable
private fun ProgressStampLine(
    currentCount: Int,
    count: Int,
    startIndex: Int,
    color: Color,
    iconSize: Dp,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        repeat(count) { index ->
            val filled = startIndex + index < currentCount

            Icon(
                painter =
                    painterResource(
                        if (filled) {
                            R.drawable.ic_cherry_blossom_fill
                        } else {
                            R.drawable.ic_cherry_blossom_outline
                        },
                    ),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = if (filled) color else color.copy(alpha = 0.35f),
            )
        }
    }
}

@Composable
fun SheetCard(
    sheet: RewardSheet,
    lastStampType: StampType?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Card(
        modifier = modifier,
        border =
            BorderStroke(
                1.dp,
                SheetBorder,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(12.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                GoalBadge(goalCount = sheet.goalCount, color = color)
                Spacer(modifier = Modifier.height(8.dp))
                LastStampView(stampType = lastStampType, color = color)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier =
                    Modifier
                        .weight(1f),
            ) {
                ProgressInfo(
                    title = sheet.title,
                    currentCount = sheet.currentCount,
                    goalCount = sheet.goalCount,
                )

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    ProgressStampRow(
                        modifier = Modifier.fillMaxWidth().testTag("SheetCard.ProgressStampRow"),
                        currentCount = sheet.currentCount,
                        goalCount = sheet.goalCount,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun SheetDetailContentPreview() {
    RewardStampTheme {
        Scaffold {
            LazyColumn(
                modifier = Modifier.padding(it),
            ) {
                items(
                    arrayOf(
                        RewardSheet(
                            id = 0,
                            title = "おてつだい",
                            goalCount = 5,
                            currentCount = 3,
                        ),
                        RewardSheet(
                            id = 0,
                            title = "おてつだい",
                            goalCount = 10,
                            currentCount = 5,
                        ),
                        RewardSheet(
                            id = 0,
                            title = "おてつだい",
                            goalCount = 15,
                            currentCount = 12,
                        ),
                        RewardSheet(
                            id = 0,
                            title = "おてつだい",
                            goalCount = 20,
                            currentCount = 14,
                        ),
                    ),
                ) { sheet ->
                    SheetCard(
                        sheet = sheet,
                        lastStampType = StampType.Hippopotamus,
                    )
                }
            }
        }
    }
}
