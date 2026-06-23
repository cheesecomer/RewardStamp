package com.cheesecomer.rewardstamp.feature.exchangeablesheet.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.RewardStampApplication
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.ExchangeableSheet
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.navigation.BottomTab
import com.cheesecomer.rewardstamp.ui.component.RewardStampBottomBar
import com.cheesecomer.rewardstamp.ui.component.dialog.ChoiceRewardDialog
import com.cheesecomer.rewardstamp.ui.component.dialog.ExchangeDialog
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.img_teddy_bear_exchange),
            contentDescription = null,
            modifier = Modifier.width(256.dp),
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "交換できるごほうびはありません",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "がんばってシートをいっぱいにしよう！",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "ごほうびが待ってるよ",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@ExcludeFromCoverage
@Composable
private fun exchangeableSheetListViewModel(): ExchangeableSheetListViewModel {
    val application =
        LocalContext.current.applicationContext as RewardStampApplication
    return viewModel(
        factory =
            ExchangeableSheetListViewModel.factory(
                application.exchangeableSheetRepository,
            ),
    )
}

@Composable
internal fun RewardDialog(
    sheet: ExchangeableSheet,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onRewardSelect: (RewardMilestone) -> Unit = {},
) {
    if (sheet.exchangeableMilestones.size == 1) {
        val milestone = sheet.exchangeableMilestones[0]
        ExchangeDialog(
            milestone = milestone,
            modifier = modifier.testTag("ExchangeDialog"),
            onDismissRequest = onDismissRequest,
            onRewardSelect = { onRewardSelect(milestone) },
        )
    } else {
        ChoiceRewardDialog(
            milestones = sheet.exchangeableMilestones,
            modifier = modifier.testTag("ChoiceRewardDialog"),
            onDismissRequest = onDismissRequest,
            onRewardSelect = onRewardSelect,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExchangeableSheetListScreenHeader(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text("ごほうび交換")
        },
        modifier = modifier,
    )
}

@Composable
internal fun ExchangeableSheetListContent(
    sheets: List<ExchangeableSheet>,
    modifier: Modifier = Modifier,
    onRewardSelect: (Long, RewardMilestone) -> Unit = { _, _ -> },
) {
    var showExchangeDialog by remember { mutableLongStateOf(0L) }
    if (showExchangeDialog != 0L) {
        val sheet = sheets.find { it.rewardSheet.id == showExchangeDialog }!!
        RewardDialog(
            sheet,
            onDismissRequest = {
                showExchangeDialog = 0L
            },
            onRewardSelect = { milestone ->
                onRewardSelect(sheet.rewardSheet.id, milestone)
            },
        )
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            ExchangeableSheetListScreenHeader()
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (sheets.isEmpty()) {
                EmptyList(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(vertical = 80.dp)
                            .testTag("ExchangeableRewardListScreen.EmptyList"),
                )
                return@Column
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.testTag("ExchangeableRewardListScreen.List"),
            ) {
                items(sheets) { sheet ->
                    ExchangeableSheetCard(
                        sheet,
                        onRewardReceiveClick = {
                            showExchangeDialog = sheet.rewardSheet.id
                        },
                    )
                }
            }
        }
    }
}

@ExcludeFromCoverage
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeableSheetListScreen(
    modifier: Modifier = Modifier,
    viewModel: ExchangeableSheetListViewModel = exchangeableSheetListViewModel(),
) {
    ExchangeableSheetListContent(
        sheets = viewModel.uiState.sheets,
        modifier = modifier,
        onRewardSelect = { sheetId, milestone ->
            viewModel.receiveReward(sheetId, milestone)
        },
    )
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber", "LongMethod")
@Composable
private fun ExchangeableSheetListPreview() {
    RewardStampTheme {
        Scaffold(
            bottomBar = {
                RewardStampBottomBar(
                    selectedTab = BottomTab.Sheets,
                )
            },
        ) { innerPadding ->
            ExchangeableSheetListContent(
                modifier = Modifier.padding(innerPadding),
                sheets =
                    listOf(
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
                    ),
                onRewardSelect = { _, _ -> },
            )
        }
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber", "LongMethod")
@Composable
private fun EmptyExchangeableSheetListPreview() {
    RewardStampTheme {
        Scaffold(
            bottomBar = {
                RewardStampBottomBar(
                    selectedTab = BottomTab.Sheets,
                )
            },
        ) { innerPadding ->
            ExchangeableSheetListContent(
                modifier = Modifier.padding(innerPadding),
                sheets = emptyList(),
                onRewardSelect = { _, _ -> },
            )
        }
    }
}
