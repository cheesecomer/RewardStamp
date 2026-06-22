package com.cheesecomer.rewardstamp.feature.completedsheet.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.RewardStampApplication
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.CompletedRewardSheet
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.ui.theme.Nikumaru
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.cheesecomer.rewardstamp.ui.theme.SheetBorder
import com.cheesecomer.rewardstamp.ui.theme.SheetCard
import com.cheesecomer.rewardstamp.ui.theme.StampInk
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ExcludeFromCoverage
@Composable
private fun completedSheetListViewModel(): CompletedSheetListViewModel {
    val application =
        LocalContext.current.applicationContext as RewardStampApplication
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
            Text("がんばったきろく")
        },
        modifier = modifier,
    )
}

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.img_teddy_bear_completed_sheet),
            contentDescription = null,
            modifier = Modifier.width(256.dp),
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "がんばったシートはまだないよ",
            style =
                MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Nikumaru,
                ),
        )
        Text(
            text = "最後までがんばると\nここにのこるよ♪",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun RewardReceivedBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(36.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 2.dp,
        border =
            BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Outlined.CardGiftcard,
                contentDescription = "ごほうび交換済み",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun CompletedSheetGridItem(
    sheet: CompletedRewardSheet,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            color = SheetCard,
            border = BorderStroke(1.dp, SheetBorder),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(sheet.goalStampType.iconRes),
                    contentDescription = sheet.goalStampType.id,
                    tint = StampInk,
                )
                Text(
                    text =
                        sheet.completedAt.format(
                            DateTimeFormatter.ofPattern("y/M/d"),
                        ),
                    color = StampInk,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = sheet.title,
                    style =
                        MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                        ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = "${sheet.goalCount}かい",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        if (sheet.consumedAt != null) {
            RewardReceivedBadge(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-10).dp, y = 10.dp)
                        .testTag("CompletedSheetListScreen.CompletedSheetGrid.${sheet.id}.RewardReceivedBadge"),
            )
            Spacer(Modifier.size(8.dp))
        }
    }
}

@Composable
internal fun CompletedSheetListContent(
    sheets: List<CompletedRewardSheet>,
    modifier: Modifier = Modifier,
    onSheetClick: (Long) -> Unit = {},
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
            if (sheets.isEmpty()) {
                EmptyList(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp)
                            .testTag("CompletedSheetListScreen.EmptyList"),
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.testTag("CompletedSheetListScreen.CompletedSheetGrid"),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(sheets) { sheet ->
                        CompletedSheetGridItem(
                            sheet,
                            modifier = Modifier.testTag("CompletedSheetListScreen.CompletedSheetGrid.${sheet.id}"),
                            onClick = { onSheetClick(sheet.id) },
                        )
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
        onSheetClick = onRewardClick,
    )
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun CompletedSheetListScreenPreview() {
    RewardStampTheme {
        CompletedSheetListContent(
            sheets =
                listOf(
                    CompletedRewardSheet(
                        id = 1L,
                        sheetId = 1L,
                        title = "おかたずけ",
                        goalCount = 10,
                        goalStampType = GoalStampType.Bear1,
                        completedAt = LocalDateTime.now(),
                        consumedAt = null,
                    ),
                    CompletedRewardSheet(
                        id = 1L,
                        sheetId = 1L,
                        title = "おかたずけ",
                        goalCount = 10,
                        goalStampType = GoalStampType.Bear1,
                        completedAt = LocalDateTime.now().minusDays(10),
                        consumedAt = null,
                    ),
                    CompletedRewardSheet(
                        id = 1L,
                        sheetId = 1L,
                        title = "おかたずけ",
                        goalCount = 10,
                        goalStampType = GoalStampType.Bear1,
                        completedAt = LocalDateTime.now().minusDays(10),
                        consumedAt = null,
                    ),
                    CompletedRewardSheet(
                        id = 1L,
                        sheetId = 1L,
                        title = "おかたずけ",
                        goalCount = 10,
                        goalStampType = GoalStampType.Bear1,
                        completedAt = LocalDateTime.now().minusDays(20),
                        consumedAt = LocalDateTime.now(),
                    ),
                ),
        )
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun EmptyCompletedSheetListScreenPreview() {
    RewardStampTheme {
        CompletedSheetListContent(
            sheets = emptyList(),
        )
    }
}
