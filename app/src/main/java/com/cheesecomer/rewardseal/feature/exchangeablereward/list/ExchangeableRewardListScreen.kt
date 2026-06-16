package com.cheesecomer.rewardseal.feature.exchangeablereward.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.cheesecomer.rewardseal.model.ExchangeableSheet
import com.cheesecomer.rewardseal.model.RewardMilestone
import com.cheesecomer.rewardseal.ui.component.dialog.ChoiceRewardDialog
import com.cheesecomer.rewardseal.ui.component.dialog.ExchangeDialog

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
fun RewardDialog(
    sheet: ExchangeableSheet,
    onDismissRequest: () -> Unit = {},
    onRewardSelect: (RewardMilestone) -> Unit = {},
) {
    if (sheet.exchangeableMilestones.size == 1) {
        ExchangeDialog(
            milestone = sheet.exchangeableMilestones[0],
            onDismissRequest = onDismissRequest,
            onRewardSelect = onRewardSelect,
        )
    } else {
        ChoiceRewardDialog(
            milestones = sheet.exchangeableMilestones,
            onDismissRequest = onDismissRequest,
            onRewardSelect = onRewardSelect,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeableRewardListScreenHeader(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text("交換できるごほうび")
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "戻る",
                )
            }
        },
    )
}

@Composable
fun ExchangeableRewardListItem(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeableRewardListScreen(
    modifier: Modifier = Modifier,
    viewModel: ExchangeableRewardListViewModel = exchangeableRewardListViewModel(),
    onBackClick: () -> Unit = {},
) {
    val uiState = viewModel.uiState
    val sheets = uiState.sheets
    var showExchangeDialog by remember { mutableLongStateOf(0L) }
    if (showExchangeDialog != 0L) {
        val sheet = sheets.find { it.id == showExchangeDialog }!!
        RewardDialog(
            sheet,
            onDismissRequest = {
                showExchangeDialog = 0L
            },
            onRewardSelect = { milestone ->
                viewModel.receiveReward(sheet.id, milestone)
            },
        )
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ExchangeableRewardListScreenHeader(onBackClick = onBackClick)

        if (sheets.isEmpty()) {
            Text("交換できるごほうびはありません")
            return
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
