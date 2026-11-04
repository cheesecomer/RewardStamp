package com.cheesecomer.rewardstamp.feature.sheet.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecomer.rewardstamp.data.repository.RewardSheetRepository
import com.cheesecomer.rewardstamp.data.repository.RewardStampRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SheetListViewModel(
    private val rewardSheetRepository: RewardSheetRepository,
    private val stampRepository: RewardStampRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SheetListUiState())
    val uiState: StateFlow<SheetListUiState> = _uiState.asStateFlow()

    fun reload() {
        viewModelScope.launch {
            val sheets = rewardSheetRepository.findAll()
            val latestStamps = stampRepository.findLatestByEachSheet()
            _uiState.update {
                it.copy(
                    sheets = sheets,
                    latestStamps = latestStamps,
                )
            }
        }
    }
}
