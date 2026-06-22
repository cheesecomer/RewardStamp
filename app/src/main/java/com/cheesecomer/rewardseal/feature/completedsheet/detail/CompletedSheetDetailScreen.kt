package com.cheesecomer.rewardseal.feature.completedsheet.detail
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import com.cheesecomer.rewardseal.model.GoalStamp
import com.cheesecomer.rewardseal.model.GoalStampType
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.ui.component.RewardBoardState
import com.cheesecomer.rewardseal.ui.component.RewardBoardView
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import java.time.LocalDateTime

@ExcludeFromCoverage
@Composable
private fun completedSheetDetailViewModel(): CompletedSheetDetailViewModel {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    return viewModel<CompletedSheetDetailViewModel>(
        factory =
            CompletedSheetDetailViewModel.factory(
                application.completedRewardSheetRepository,
                application.rewardStampRepository,
            ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompletedSheetDetailHeader(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text("がんばったきろく")
        },
        modifier = modifier,
    )
}

@Composable
private fun TitleLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = text,
            style =
                MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            modifier = Modifier.alignByBaseline().testTag("CompletedSheetDetailScreen.TitleLabel.Text"),
        )
        Text(
            text = "を",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.alignByBaseline(),
        )
    }
}

@Composable
private fun GoalCountLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.alignByBaseline().testTag("CompletedSheetDetailScreen.GoalCountLabel.Text"),
        )
        Text(
            text = "かい がんばりました！",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.alignByBaseline(),
        )
    }
}

@Composable
internal fun CompletedSheetDetailContent(
    sheet: CompletedRewardSheet?,
    stamps: List<RewardStamp>,
    modifier: Modifier = Modifier,
) {
    if (sheet == null) {
        Text("見つかりません")
        return
    }

    Scaffold(
        modifier = modifier,
        topBar = { CompletedSheetDetailHeader() },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TitleLabel(
                    text = sheet.title,
                    modifier = Modifier.testTag("CompletedSheetDetailScreen.TitleLabel"),
                )
                GoalCountLabel(
                    text = sheet.goalCount.toString(),
                    modifier = Modifier.testTag("CompletedSheetDetailScreen.GoalCountLabel"),
                )
            }

            RewardBoardView(
                board =
                    RewardBoardState(
                        title = sheet.title,
                        currentCount = sheet.goalCount,
                        goalCount = sheet.goalCount,
                    ),
                stamps = stamps,
                goalStamp =
                    GoalStamp(
                        drawable = LocalContext.current.getDrawable(sheet.goalStampType.iconRes)!!,
                        stampedAt = sheet.completedAt,
                    ),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@ExcludeFromCoverage
@Composable
fun CompletedSheetDetailScreen(
    completedSheetId: Long,
    modifier: Modifier = Modifier,
    viewModel: CompletedSheetDetailViewModel = completedSheetDetailViewModel(),
) {
    LaunchedEffect(completedSheetId) {
        viewModel.load(completedSheetId)
    }

    CompletedSheetDetailContent(
        sheet = viewModel.uiState.sheet,
        stamps = viewModel.uiState.stamps,
        modifier = modifier,
    )
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun CompletedSheetDetailScreenPreview() {
    val rewardStamp: (Int) -> RewardStamp = @ExcludeFromCoverage { position ->
        RewardStamp(
            id = position.toLong(),
            sheetId = 1,
            completedRewardSheetId = null,
            position = position,
            stampedAt = LocalDateTime.now().minusDays(10 - position.toLong()),
            stampType = StampType.entries.random(),
        )
    }
    RewardSealTheme {
        CompletedSheetDetailContent(
            sheet =
                CompletedRewardSheet(
                    id = 1L,
                    sheetId = 1L,
                    title = "おかたずけ",
                    goalCount = 10,
                    goalStampType = GoalStampType.Bear9,
                    completedAt = LocalDateTime.now(),
                    consumedAt = null,
                ),
            stamps = (0..9).map { rewardStamp(it) },
        )
    }
}
