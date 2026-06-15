package com.cheesecomer.rewardseal.ui.screen.sheetlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication

@Composable
fun SheetListScreen(
    modifier: Modifier = Modifier,
    onSheetClick: (Long) -> Unit = {},
    onUnreceivedRewardsClick: () -> Unit = {},
    onCompletedRewardsClick: () -> Unit = {},
) {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    val viewModel: SheetListViewModel = viewModel(
        factory = SheetListViewModel.factory(
            application.rewardSheetRepository,
            application.completedRewardSheetRepository
        )
    )
    val sheets = viewModel.sheets
    LaunchedEffect(Unit) {
        viewModel.reload()
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "ごほうびシール",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val unreceivedRewardCount = viewModel.unreceivedRewardCount
        if (unreceivedRewardCount > 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        onUnreceivedRewardsClick()
                    }
            ) {
                Text(
                    text = "交換できるごほうびはあります：${unreceivedRewardCount}件",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        val completedRewardCount = viewModel.completedRewardCount
        if (completedRewardCount > 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        onCompletedRewardsClick()
                    }
            ) {
                Text(
                    text = "これまでのがんばりを見る",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        if (sheets.isEmpty()) {
            if (completedRewardCount > 0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "もうシートがありません",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Text(
                        text = "「＋」から、がんばることを作ってみましょう",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "まだシートがありません",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Text(
                        text = "「＋」から、がんばることを作ってみましょう",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(sheets) { sheet ->
                    val sheet = sheet
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clickable {
                                onSheetClick(sheet.id)
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = sheet.title, style = MaterialTheme.typography.titleLarge)
                            LinearProgressIndicator(
                                progress = { sheet.currentCount.toFloat() / sheet.goalCount }
                            )
                            Text(text= "${sheet.currentCount} / ${sheet.goalCount}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}