package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cheesecomer.rewardseal.R
import com.cheesecomer.rewardseal.model.GoalStampType
import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.ui.component.RewardBoardState
import com.cheesecomer.rewardseal.ui.component.RewardBoardView
import com.cheesecomer.rewardseal.ui.theme.Nikumaru
import com.cheesecomer.rewardseal.ui.theme.SheetBorder
import com.cheesecomer.rewardseal.ui.theme.SheetPrimary
import com.cheesecomer.rewardseal.ui.theme.SheetText

@Composable
private fun StampButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues =
        PaddingValues(
            horizontal = 32.dp,
            vertical = 12.dp,
        ),
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        color = SheetPrimary,
        shadowElevation = 6.dp,
    ) {
        Box(
            modifier = Modifier.padding(paddingValues = contentPadding),
        ) {
            Text(
                text = "スタンプを押す",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontFamily = Nikumaru,
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White,
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .offset(x = 72.dp),
            )
        }
    }
}

@Composable
private fun SheetProgress(
    goalCount: Int,
    currentCount: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.stamp_cherry_blossom),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = SheetPrimary,
            )

            Text(
                text = "回数",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = Nikumaru,
                color = SheetText,
            )
        }

        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "$currentCount",
                fontSize = 48.sp,
                color = SheetPrimary,
                modifier = Modifier.alignByBaseline(),
            )

            Text(
                text = " / ",
                modifier = Modifier.alignByBaseline(),
            )

            Text(
                text = "$goalCount",
                modifier = Modifier.alignByBaseline(),
            )

            Text(
                text = " 回",
                modifier = Modifier.alignByBaseline(),
            )
        }

        Text(
            text = "がんばったね！",
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun SheetDescription(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.stamp_cherry_blossom),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = SheetPrimary,
            )

            Text(
                text = "きょうの がんばり",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = Nikumaru,
                color = SheetText,
            )
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "シートをすべてうめて",
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = "ごほうびのスタンプをもらおう！",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun SheetSummaryCard(
    sheet: RewardSheet,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border =
            BorderStroke(
                1.dp,
                SheetBorder,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SheetDescription(
                modifier = Modifier.weight(1f).fillMaxHeight(),
            )

            VerticalDivider(
                modifier =
                    Modifier
                        .height(80.dp)
                        .padding(horizontal = 12.dp),
            )

            SheetProgress(
                goalCount = sheet.goalCount,
                currentCount = sheet.currentCount,
            )
        }
    }
}

@Composable
fun ProgressSheet(
    sheet: RewardSheet,
    stamps: List<RewardStamp>,
    goalStampType: GoalStampType?,
    onStampTypeSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SheetSummaryCard(
            sheet = sheet,
        )

        RewardBoardView(
            board =
                RewardBoardState(
                    title = sheet.title,
                    currentCount = sheet.currentCount,
                    goalCount = sheet.goalCount,
                ),
            stamps = stamps,
            goalStampType = goalStampType,
            modifier = Modifier.weight(1f),
        )
        StampButton(
            onClick = onStampTypeSelect,
            enabled = sheet.currentCount < sheet.goalCount,
            modifier =
                Modifier
                    .padding(horizontal = 8.dp)
                    .height(48.dp),
            contentPadding =
                PaddingValues(
                    horizontal = 62.dp,
                    vertical = 12.dp,
                ),
        )
    }
}
