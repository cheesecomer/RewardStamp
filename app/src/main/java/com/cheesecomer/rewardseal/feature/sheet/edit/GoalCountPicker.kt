package com.cheesecomer.rewardseal.feature.sheet.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cheesecomer.rewardseal.ui.theme.SheetBorder
import com.cheesecomer.rewardseal.ui.theme.SheetPrimary
import com.cheesecomer.rewardseal.ui.theme.SheetPrimaryContainer

@Composable
private fun DecrementButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .clip(CircleShape)
                .background(SheetPrimaryContainer)
                .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.Remove,
            contentDescription = null,
            tint = SheetPrimary,
        )
    }
}

@Composable
private fun IncrementButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .clip(CircleShape)
                .background(SheetPrimaryContainer)
                .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            tint = SheetPrimary,
        )
    }
}

@Composable
private fun GoalCountLabel(
    goalCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .wrapContentWidth(),
    ) {
        Text(
            text = goalCount.toString(),
            modifier = Modifier.alignByBaseline(),
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.width(6.dp))

        Text(
            text = "回",
            modifier = Modifier.alignByBaseline(),
            fontSize = 28.sp,
        )
    }
}

@Composable
fun GoalCountPicker(
    goalCount: Int,
    modifier: Modifier = Modifier,
    onPlusClick: () -> Unit = {},
    onMinusClick: () -> Unit = {},
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
            SheetEditSectionTitle(text = "何回がんばる？")
            Spacer(modifier = Modifier.size(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                DecrementButton(
                    modifier =
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 32.dp)
                            .size(48.dp)
                            .testTag("GoalCountPicker.DecrementButton"),
                    onClick = onMinusClick,
                )

                GoalCountLabel(
                    goalCount = goalCount,
                    modifier =
                        Modifier
                            .align(Alignment.Center),
                )

                IncrementButton(
                    modifier =
                        Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 32.dp)
                            .size(48.dp)
                            .testTag("GoalCountPicker.IncrementButton"),
                    onClick = onPlusClick,
                )
            }
        }
    }
}
