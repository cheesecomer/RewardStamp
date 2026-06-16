package com.cheesecomer.rewardseal.feature.completedsheet.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication

@Composable
private fun completedSheetListViewModel(): CompletedSheetListViewModel {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    return viewModel(
        factory =
            CompletedSheetListViewModel.factory(
                application.completedRewardSheetRepository,
            ),
    )
}

@Composable
fun CompletedSheetListScreen(
    modifier: Modifier = Modifier,
    viewModel: CompletedSheetListViewModel = completedSheetListViewModel(),
    onRewardClick: (Long) -> Unit = {},
) {
    val sheets = viewModel.sheets

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "これまでのがんばり",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text("達成したごほうび：${sheets.size}こ")

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(sheets) { reward ->
                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                onRewardClick(reward.id)
                            },
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "${reward.title} を ${reward.goalCount}回 がんばりました！",
//                            style = MaterialTheme.typography.headlineMedium
                        )

                        if (reward.consumedAt != null) {
                            Text(
                                text = "交換済み",
                            )
                        }
                    }
                }
            }
        }
    }
}
