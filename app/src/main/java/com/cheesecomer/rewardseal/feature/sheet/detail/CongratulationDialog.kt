package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cheesecomer.rewardseal.R
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.RewardMilestone
import com.cheesecomer.rewardseal.ui.theme.Nikumaru
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import com.cheesecomer.rewardseal.ui.theme.SheetPrimary

@Composable
private fun CongratulationDialogDescription(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "やったー！",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            fontFamily = Nikumaru,
        )
        Text(
            text = "シートがいっぱいになったよ！",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private const val MAX_HEIGHT_RATIO = 0.9f

@Composable
fun CongratulationDialogBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(MAX_HEIGHT_RATIO)
                    .wrapContentHeight()
                    .padding(horizontal = 24.dp),
        ) {
            Box {
                content()
            }
        }
    }
}

@Composable
fun CongratulationDialogContent(
    exchangeableRewards: List<RewardMilestone>,
    lockedRewards: List<RewardMilestone>,
    onEditButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CongratulationDialogBox(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.img_teddy_bear_congratulation),
                contentDescription = null,
                modifier = Modifier.size(236.dp),
            )
            CongratulationDialogDescription()
            RewardList(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .padding(top = 20.dp, bottom = 8.dp),
                exchangeableRewards = exchangeableRewards,
                lockedRewards = lockedRewards,
            )
            OutlinedButton(
                onClick = onDismissRequest,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .testTag("CongratulationDialog.SureButton"),
            ) {
                Text("わかった")
            }
        }
        FilledTonalIconButton(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .testTag("CongratulationDialog.EditButton"),
            onClick = onEditButtonClick,
        ) {
            Icon(
                Icons.Outlined.Edit,
                contentDescription = "ごほうびを編集",
                tint = SheetPrimary,
            )
        }
    }
}

@Composable
fun CongratulationDialog(
    onEditButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    exchangeableRewards: List<RewardMilestone>,
    lockedRewards: List<RewardMilestone>,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
            ),
    ) {
        CongratulationDialogContent(
            exchangeableRewards = exchangeableRewards,
            lockedRewards = lockedRewards,
            onEditButtonClick = onEditButtonClick,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
        )
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Composable
private fun CongratulationDialogPreview() {
    RewardSealTheme {
        Scaffold {
            Surface(
                color = Color.Gray,
                modifier = Modifier.padding(it),
            ) {
                CongratulationDialogContent(
                    onDismissRequest = {},
                    onEditButtonClick = {},
                    exchangeableRewards =
                        listOf(
                            RewardMilestone(
                                id = 1L,
                                sheetId = 1L,
                                requiredSheetCount = 1,
                                reward = "アイス",
                            ),
                            RewardMilestone(
                                id = 1L,
                                sheetId = 1L,
                                requiredSheetCount = 2,
                                reward = "おもちゃ",
                            ),
                        ),
                    lockedRewards =
                        listOf(
                            RewardMilestone(
                                id = 1L,
                                sheetId = 1L,
                                requiredSheetCount = 10,
                                reward = "どうぶつえん",
                            ),
                            RewardMilestone(
                                id = 1L,
                                sheetId = 1L,
                                requiredSheetCount = 15,
                                reward = "水族館",
                            ),
                            RewardMilestone(
                                id = 1L,
                                sheetId = 1L,
                                requiredSheetCount = 500,
                                reward = "ディズニーリゾート",
                            ),
                        ),
                )
            }
        }
    }
}
