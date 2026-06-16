package com.cheesecomer.rewardseal.feature.completedsheet.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.RewardStampRepository
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import kotlinx.coroutines.launch

class CompletedSheetDetailViewModel(
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
    private val rewardStampRepository: RewardStampRepository,
) : ViewModel() {
    companion object {
        fun factory(
            completedRewardSheetRepository: CompletedRewardSheetRepository,
            rewardStampRepository: RewardStampRepository,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    CompletedSheetDetailViewModel(
                        completedRewardSheetRepository,
                        rewardStampRepository,
                    )
                }
            }
    }

    var uiState by mutableStateOf(
        CompletedSheetDetailUiState(),
    )
        private set

    var reward by mutableStateOf<CompletedRewardSheet?>(null)
        private set

    fun load(completedRewardSheetId: Long) {
        viewModelScope.launch {
            uiState =
                uiState.copy(
                    reward = completedRewardSheetRepository.findById(completedRewardSheetId),
                    stamps = rewardStampRepository.findByCompletedRewardSheetId(completedRewardSheetId),
                )
        }
    }
}
