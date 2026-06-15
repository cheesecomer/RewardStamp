package com.cheesecomer.rewardseal.ui.screen.completedrewarddetail
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.ui.component.RewardBoardState
import com.cheesecomer.rewardseal.ui.component.RewardBoardView

@Composable
fun CompletedRewardDetailScreen(
    modifier: Modifier = Modifier,
    completedRewardId: Long,
) {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    val viewModel: CompletedRewardDetailViewModel = viewModel(
        factory = CompletedRewardDetailViewModel.factory(
            application.completedRewardSheetRepository,
            application.rewardStampRepository
        )
    )

    LaunchedEffect(completedRewardId) {
        viewModel.load(completedRewardId)
    }
    val uiState = viewModel.uiState
    val reward = uiState.reward
    if (reward == null) {
        Text("見つかりません")
        return
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "がんばった記録",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            text = reward.title,
            style = MaterialTheme.typography.titleLarge,
        )

        Text("${reward.goalCount}回 がんばりました")
        Text("ごほうび：${reward.reward}")

        Text(
            text = if (reward.rewardReceived) {
                "ごほうび受け取り済み"
            } else {
                "ごほうび未受領"
            }
        )

        RewardBoardView(
            board = RewardBoardState(
                title = reward.title,
                currentCount = reward.goalCount,
                goalCount = reward.goalCount,
            ),
            stamps = uiState.stamps,
            modifier = Modifier.weight(1f)
        )
    }
}