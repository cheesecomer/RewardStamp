package com.cheesecomer.rewardseal.feature.completedsheet.detail
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.ui.component.RewardBoardState
import com.cheesecomer.rewardseal.ui.component.RewardBoardView

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
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "がんばった記録",
                style = MaterialTheme.typography.headlineMedium,
            )

            Text(
                text = "${sheet.title} を ${sheet.goalCount}回 がんばりました！",
                style = MaterialTheme.typography.headlineSmall,
            )

            if (sheet.consumedAt != null) {
                Text(
                    text = "交換済み",
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
                goalStampType = sheet.goalStampType,
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
