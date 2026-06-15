package com.cheesecomer.rewardseal.ui.screen.unreceivedrewardlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardseal.data.repository.RewardSheetRepository
import com.cheesecomer.rewardseal.model.RewardMilestone
import kotlinx.coroutines.launch


class UnreceivedRewardListViewModel(
    private val rewardSheetRepository: RewardSheetRepository,
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
    private val milestoneRepository: RewardMilestoneRepository
) : ViewModel() {
    companion object {
        fun factory(
            rewardSheetRepository: RewardSheetRepository,
            completedRewardSheetRepository: CompletedRewardSheetRepository,
            milestoneRepository: RewardMilestoneRepository
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    UnreceivedRewardListViewModel(
                        rewardSheetRepository,
                        completedRewardSheetRepository,
                        milestoneRepository
                    )
                }
            }
    }

    var uiState by mutableStateOf(
        UnreceivedRewardListUiState()
    )
        private set

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            uiState = uiState.copy(
                sheets = rewardSheetRepository.findExchangeable().map {
                    val unconsumedCompletedCount = completedRewardSheetRepository.countExchangeableBySheetId(it.id)
                    ExchangeableRewardListItemState(
                        id = it.id,
                        title = it.title,
                        unconsumedCompletedCount = unconsumedCompletedCount,
                        exchangeableMilestones =
                            milestoneRepository.findExchangeableBySheetId(it.id, unconsumedCompletedCount),
                        nextMilestone = milestoneRepository.findNext(it.id, unconsumedCompletedCount)
                    )
                }
            )
        }
    }

    fun receiveReward(id: Long, milestone: RewardMilestone) {
        viewModelScope.launch {
            completedRewardSheetRepository.markRewardReceived(id, milestone.requiredCompletions)
            reload()
        }
    }
}