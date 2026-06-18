package com.cheesecomer.rewardseal.feature.completedsheet.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.CompletedRewardSheet

@ExcludeFromCoverage
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompletedSheetListHeader(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text("これまでのがんばり")
        },
        modifier = modifier,
    )
}

@Composable
internal fun CompletedSheetListContent(
    sheets: List<CompletedRewardSheet>,
    modifier: Modifier = Modifier,
    onRewardClick: (Long) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = { CompletedSheetListHeader() },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
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
}

@ExcludeFromCoverage
@Composable
fun CompletedSheetListScreen(
    modifier: Modifier = Modifier,
    viewModel: CompletedSheetListViewModel = completedSheetListViewModel(),
    onRewardClick: (Long) -> Unit = {},
) {
    CompletedSheetListContent(
        sheets = viewModel.sheets,
        modifier = modifier,
        onRewardClick = onRewardClick,
    )
}
