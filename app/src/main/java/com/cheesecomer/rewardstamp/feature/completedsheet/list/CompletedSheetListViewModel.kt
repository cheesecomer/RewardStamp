package com.cheesecomer.rewardstamp.feature.completedsheet.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecomer.rewardstamp.data.repository.CompletedRewardSheetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompletedSheetListViewModel(
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CompletedSheetListUiState())
    val uiState: StateFlow<CompletedSheetListUiState> = _uiState.asStateFlow()

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            val sheets = completedRewardSheetRepository.findAll()
            _uiState.update { it.copy(sheets = sheets) }
        }
    }
}
