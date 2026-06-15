package com.cheesecomer.rewardseal.ui.screen.sheetlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardseal.data.repository.RewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.model.RewardSheet
import kotlinx.coroutines.launch

class SheetListViewModel(
    private val rewardSheetRepository: RewardSheetRepository,
    private val completedRewardSheetRepository: CompletedRewardSheetRepository
) : ViewModel() {
    companion object {
        fun factory(
            rewardSheetRepository: RewardSheetRepository,
            completedRewardSheetRepository: CompletedRewardSheetRepository
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SheetListViewModel(
                        rewardSheetRepository,
                        completedRewardSheetRepository
                    )
                }
            }
    }

    var sheets by mutableStateOf<List<RewardSheet>>(
        emptyList()
    )
        private set

    var unreceivedRewardCount by mutableStateOf(0)
        private set

    var completedRewardCount by mutableStateOf(0)
        private set

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            sheets = rewardSheetRepository.findAll()
            unreceivedRewardCount = completedRewardSheetRepository.countUnreceived()
            completedRewardCount = completedRewardSheetRepository.countAll()
        }
    }
}