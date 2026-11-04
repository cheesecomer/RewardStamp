package com.cheesecomer.rewardstamp.feature.sheet.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.model.RewardStamp
import com.cheesecomer.rewardstamp.model.StampType
import com.cheesecomer.rewardstamp.navigation.BottomTab
import com.cheesecomer.rewardstamp.ui.component.RewardStampBottomBar
import com.cheesecomer.rewardstamp.ui.component.RewardStampScaffold
import com.cheesecomer.rewardstamp.ui.rewardStampViewModel
import com.cheesecomer.rewardstamp.ui.theme.Nikumaru
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import java.time.LocalDateTime

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.img_teddy_bear_sheet),
            contentDescription = null,
            alpha = 0.65f,
            modifier = Modifier.width(256.dp),
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = stringResource(R.string.sheet_list_empty_title),
            style =
                MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Nikumaru,
                ),
        )
        Text(
            text = stringResource(R.string.sheet_list_empty_message),
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
            contentDescription = stringResource(R.string.sheet_list_create),
        )
    }
}

@Composable
internal fun SheetListContent(
    sheets: List<RewardSheet>,
    latestStamps: Map<Long, RewardStamp>,
    onSheetClick: (Long) -> Unit,
    onCreateSheetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RewardStampScaffold(
        title = stringResource(R.string.sheet_list_title),
        modifier = modifier,
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
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 80.dp)
                            .testTag(SheetListTestTags.EMPTY_LIST),
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
                                    .testTag(SheetListTestTags.sheetCard(sheet.id)),
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
    viewModel: SheetListViewModel =
        rewardStampViewModel {
            SheetListViewModel(
                rewardSheetRepository,
                rewardStampRepository,
            )
        },
    onSheetClick: (Long) -> Unit = {},
    onCreateSheetClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.reload()
    }

    SheetListContent(
        modifier = modifier,
        sheets = uiState.sheets,
        latestStamps = uiState.latestStamps,
        onSheetClick = onSheetClick,
        onCreateSheetClick = onCreateSheetClick,
    )
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun EmptySheetListContentPreview() {
    RewardStampTheme {
        Scaffold(
            bottomBar = {
                RewardStampBottomBar(
                    selectedTab = BottomTab.Sheets,
                )
            },
        ) { innerPadding ->
            SheetListContent(
                sheets = emptyList(),
                latestStamps = emptyMap(),
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
    RewardStampTheme {
        Scaffold(
            bottomBar = {
                RewardStampBottomBar(
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
                onSheetClick = {},
                onCreateSheetClick = {},
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
