package com.cheesecomer.rewardseal.feature.sheet.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.RewardSheet

@ExcludeFromCoverage
@Composable
private fun sheetListViewModel(): SheetListViewModel {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    return viewModel(
        factory =
            SheetListViewModel.factory(
                application.rewardSheetRepository,
                application.completedRewardSheetRepository,
            ),
    )
}

@Composable
private fun EmptyList(
    modifier: Modifier = Modifier,
    completedRewardCount: Int = 0,
) {
    if (completedRewardCount > 0) {
        Column(
            modifier =
                modifier
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
            modifier =
                Modifier
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
}

@Composable
private fun SheetListItem(
    sheet: RewardSheet,
    modifier: Modifier = Modifier,
    onSheetClick: (Long) -> Unit = {},
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable {
                    onSheetClick(sheet.id)
                },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = sheet.title, style = MaterialTheme.typography.titleLarge)
            LinearProgressIndicator(
                progress = { sheet.currentCount.toFloat() / sheet.goalCount },
            )
            Text(
                text = "${sheet.currentCount} / ${sheet.goalCount}",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun FloatingCreateSheetButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "シートを作る",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SheetListScreenHeader(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text("ごほうびスタンプ")
        },
        modifier = modifier,
    )
}

@Composable
internal fun SheetListContent(
    sheets: List<RewardSheet>,
    completedSheetCount: Int,
    onSheetClick: (Long) -> Unit,
    onCreateSheetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { SheetListScreenHeader() },
        floatingActionButton = {
            FloatingCreateSheetButton(
                onClick = onCreateSheetClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
        ) {
            if (sheets.isEmpty()) {
                EmptyList(
                    completedRewardCount = completedSheetCount,
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {
                    items(sheets) { sheet ->
                        SheetListItem(
                            sheet = sheet,
                            onSheetClick = onSheetClick,
                        )
                    }
                }
            }
        }
    }
}

@ExcludeFromCoverage
@Composable
fun SheetListScreen(
    modifier: Modifier = Modifier,
    viewModel: SheetListViewModel = sheetListViewModel(),
    onSheetClick: (Long) -> Unit = {},
    onCreateSheetClick: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        viewModel.reload()
    }

    SheetListContent(
        modifier = modifier,
        sheets = viewModel.sheets,
        completedSheetCount = viewModel.completedSheetCount,
        onSheetClick = onSheetClick,
        onCreateSheetClick = onCreateSheetClick,
    )
}
