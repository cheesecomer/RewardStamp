package com.cheesecomer.rewardseal.feature.exchangeablereward.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.ExchangeableSheet
import com.cheesecomer.rewardseal.model.RewardMilestone
import com.cheesecomer.rewardseal.ui.component.dialog.ChoiceRewardDialog
import com.cheesecomer.rewardseal.ui.component.dialog.ExchangeDialog

@ExcludeFromCoverage
@Composable
private fun exchangeableRewardListViewModel(): ExchangeableRewardListViewModel {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    return viewModel(
        factory =
            ExchangeableRewardListViewModel.factory(
                application.exchangeableRewardRepository,
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
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            onRewardSelect = { onRewardSelect(milestone) },
        )
    } else {
        ChoiceRewardDialog(
            milestones = sheet.exchangeableMilestones,
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            onRewardSelect = onRewardSelect,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExchangeableRewardListScreenHeader(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text("交換できるごほうび")
        },
        modifier = modifier,
    )
}

@Composable
private fun ExchangeableRewardListItem(
    sheet: ExchangeableSheet,
    modifier: Modifier = Modifier,
    onRewardReceiveClick: () -> Unit = { },
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = sheet.title,
                style = MaterialTheme.typography.titleLarge,
            )

            Text("未交換シート ${sheet.unconsumedCompletedCount}枚")
            Text(
                text = "交換可能",
                style = MaterialTheme.typography.titleMedium,
            )
            sheet.exchangeableMilestones.forEach { milestone ->
                Text("${milestone.requiredCompletions}枚 ${milestone.reward}")
            }

            if (sheet.nextMilestone != null) {
                val leftUntil = sheet.nextMilestone.requiredCompletions - sheet.unconsumedCompletedCount
                Text(
                    "あと ${leftUntil}枚 で ${sheet.nextMilestone.reward} と交換できるよ",
                )
            }

            Button(
                onClick = onRewardReceiveClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("受け取った")
            }
        }
    }
}

@Composable
internal fun ExchangeableRewardListContent(
    sheets: List<ExchangeableSheet>,
    modifier: Modifier = Modifier,
    onRewardSelect: (Long, RewardMilestone) -> Unit = { _, _ -> },
) {
    var showExchangeDialog by remember { mutableLongStateOf(0L) }
    if (showExchangeDialog != 0L) {
        val sheet = sheets.find { it.id == showExchangeDialog }!!
        RewardDialog(
            sheet,
            onDismissRequest = {
                showExchangeDialog = 0L
            },
            onRewardSelect = { milestone ->
                onRewardSelect(sheet.id, milestone)
            },
        )
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            ExchangeableRewardListScreenHeader()
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
                Text("交換できるごほうびはありません")
                return@Column
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(sheets) { sheet ->
                    ExchangeableRewardListItem(
                        sheet,
                        onRewardReceiveClick = {
                            showExchangeDialog = sheet.id
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
fun ExchangeableRewardListScreen(
    modifier: Modifier = Modifier,
    viewModel: ExchangeableRewardListViewModel = exchangeableRewardListViewModel(),
) {
    ExchangeableRewardListContent(
        sheets = viewModel.uiState.sheets,
        modifier = modifier,
        onRewardSelect = { sheetId, milestone ->
            viewModel.receiveReward(sheetId, milestone)
        },
    )
}
