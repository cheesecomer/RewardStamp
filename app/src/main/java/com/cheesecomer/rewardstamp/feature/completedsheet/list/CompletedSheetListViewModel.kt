package com.cheesecomer.rewardstamp.feature.completedsheet.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardstamp.model.CompletedRewardSheet
import kotlinx.coroutines.launch

class CompletedSheetListViewModel(
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
) : ViewModel() {
    @ExcludeFromCoverage
    companion object {
        fun factory(completedRewardSheetRepository: CompletedRewardSheetRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    CompletedSheetListViewModel(
                        completedRewardSheetRepository,
                    )
                }
            }
    }

    var sheets by mutableStateOf<List<CompletedRewardSheet>>(emptyList())
        private set

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            sheets = completedRewardSheetRepository.findAll()
        }
    }
}
