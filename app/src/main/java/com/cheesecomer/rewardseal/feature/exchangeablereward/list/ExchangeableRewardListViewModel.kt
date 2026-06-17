package com.cheesecomer.rewardseal.feature.exchangeablereward.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.data.repository.ExchangeableRewardRepository
import com.cheesecomer.rewardseal.model.RewardMilestone
import kotlinx.coroutines.launch

class ExchangeableRewardListViewModel(
    private val exchangeableRewardRepository: ExchangeableRewardRepository,
) : ViewModel() {
    @ExcludeFromCoverage
    companion object {
        fun factory(exchangeableRewardRepository: ExchangeableRewardRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ExchangeableRewardListViewModel(exchangeableRewardRepository)
                }
            }
    }

    var uiState by mutableStateOf(
        ExchangeableRewardListUiState(),
    )
        private set

    init {
        reload()
    }

    private suspend fun reloadUiState() {
        uiState =
            uiState.copy(
                sheets = exchangeableRewardRepository.findAll(),
            )
    }

    fun reload() {
        viewModelScope.launch {
            reloadUiState()
        }
    }

    fun receiveReward(
        id: Long,
        milestone: RewardMilestone,
        onCompleted: () -> Unit = {},
    ) {
        viewModelScope.launch {
            exchangeableRewardRepository.exchangeReward(
                id,
                milestone.requiredCompletions,
            )
            reloadUiState()
            onCompleted()
        }
    }
}
