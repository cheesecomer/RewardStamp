package com.cheesecomer.rewardseal.feature.sheet.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.R
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.navigation.BottomTab
import com.cheesecomer.rewardseal.ui.component.RewardSealBottomBar
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import java.time.LocalDateTime

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
                application.rewardStampRepository,
            ),
    )
}

@Composable
private fun EmptyList(
    modifier: Modifier = Modifier,
    completedRewardCount: Int = 0,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.img_teddy_bear_sheet),
            contentDescription = null,
            alpha = 0.65f,
        )
        Spacer(modifier = Modifier.size(20.dp))
        if (completedRewardCount > 0) {
            Text(
                text = "もうシートがありません",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        } else {
            Text(
                text = "まだシートがありません",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = "右下の「＋」から",
            style = MaterialTheme.typography.bodyMedium,
        )

        Text(
            text = "シートを作ってみましょう！",
            style = MaterialTheme.typography.bodyMedium,
        )
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
    latestStamps: Map<Long, RewardStamp>,
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
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 80.dp)
                            .testTag("SheetListScreen.EmptyList"),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {
                    items(sheets) { sheet ->
                        SheetCard(
                            sheet = sheet,
                            lastStampType = latestStamps[sheet.id]?.stampType,
                            modifier =
                                Modifier
                                    .clickable { onSheetClick(sheet.id) }
                                    .padding(12.dp)
                                    .testTag("SheetListScreen.SheetCard.${sheet.id}"),
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
        latestStamps = viewModel.latestStamps,
        completedSheetCount = viewModel.completedSheetCount,
        onSheetClick = onSheetClick,
        onCreateSheetClick = onCreateSheetClick,
    )
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun EmptySheetListContentPreview() {
    RewardSealTheme {
        Scaffold(
            bottomBar = {
                RewardSealBottomBar(
                    selectedTab = BottomTab.Sheets,
                )
            },
        ) { innerPadding ->
            SheetListContent(
                sheets = emptyList(),
                latestStamps = emptyMap(),
                completedSheetCount = 0,
                onSheetClick = {},
                onCreateSheetClick = {},
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun SheetListContentPreview() {
    RewardSealTheme {
        Scaffold(
            bottomBar = {
                RewardSealBottomBar(
                    selectedTab = BottomTab.Sheets,
                )
            },
        ) { innerPadding ->
            SheetListContent(
                sheets =
                    listOf(
                        RewardSheet(
                            id = 1L,
                            title = "おてつだい",
                            goalCount = 10,
                            currentCount = 9,
                        ),
                    ),
                latestStamps =
                    mapOf(
                        1L to
                            RewardStamp(
                                id = 1L,
                                sheetId = 1,
                                completedRewardSheetId = null,
                                position = 0,
                                stampedAt = LocalDateTime.now().minusSeconds(10 - 0.toLong()),
                                stampType = StampType.Star,
                            ),
                    ),
                completedSheetCount = 0,
                onSheetClick = {},
                onCreateSheetClick = {},
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
