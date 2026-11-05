package com.cheesecomer.rewardstamp.feature.completedsheet.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecomer.rewardstamp.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardstamp.data.repository.RewardStampRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompletedSheetDetailViewModel(
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
    private val rewardStampRepository: RewardStampRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CompletedSheetDetailUiState())
    val uiState: StateFlow<CompletedSheetDetailUiState> = _uiState.asStateFlow()

    fun load(completedRewardSheetId: Long) {
        viewModelScope.launch {
            val sheet = completedRewardSheetRepository.findById(completedRewardSheetId)
            val stamps = rewardStampRepository.findByCompletedRewardSheetId(completedRewardSheetId)
            _uiState.update {
                it.copy(
                    sheet = sheet,
                    stamps = stamps,
                )
            }
        }
    }

    fun delete(
        completedRewardSheetId: Long,
        onCompleted: () -> Unit,
    ) {
        viewModelScope.launch {
            completedRewardSheetRepository.delete(completedRewardSheetId)
            onCompleted()
        }
    }
}
