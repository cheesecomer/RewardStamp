package com.cheesecomer.rewardstamp.feature.exchangeablesheet.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecomer.rewardstamp.data.repository.ExchangeableSheetRepository
import com.cheesecomer.rewardstamp.model.RewardMilestone
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExchangeableSheetListViewModel(
    private val exchangeableRewardRepository: ExchangeableSheetRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExchangeableSheetListUiState())
    val uiState: StateFlow<ExchangeableSheetListUiState> = _uiState.asStateFlow()

    private var receiveRewardJob: Job? = null

    init {
        reload()
    }

    private suspend fun reloadUiState() {
        val sheets = exchangeableRewardRepository.findAll()
        _uiState.update { it.copy(sheets = sheets) }
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
        if (receiveRewardJob?.isActive == true) return

        receiveRewardJob =
            viewModelScope.launch {
                exchangeableRewardRepository.exchangeReward(
                    id,
                    milestone.requiredSheetCount,
                )
                reloadUiState()
                onCompleted()
            }
    }
}
