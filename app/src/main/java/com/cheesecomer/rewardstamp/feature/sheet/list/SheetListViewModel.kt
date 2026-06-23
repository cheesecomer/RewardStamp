package com.cheesecomer.rewardstamp.feature.sheet.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardstamp.data.repository.RewardSheetRepository
import com.cheesecomer.rewardstamp.data.repository.RewardStampRepository
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.model.RewardStamp
import kotlinx.coroutines.launch

class SheetListViewModel(
    private val rewardSheetRepository: RewardSheetRepository,
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
    private val stampRepository: RewardStampRepository,
) : ViewModel() {
    @ExcludeFromCoverage
    companion object {
        fun factory(
            rewardSheetRepository: RewardSheetRepository,
            completedRewardSheetRepository: CompletedRewardSheetRepository,
            stampRepository: RewardStampRepository,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SheetListViewModel(
                        rewardSheetRepository,
                        completedRewardSheetRepository,
                        stampRepository,
                    )
                }
            }
    }

    var sheets by mutableStateOf<List<RewardSheet>>(
        emptyList(),
    )
        private set

    var latestStamps by mutableStateOf<Map<Long, RewardStamp>>(emptyMap())

    var exchangeableSheetCount by mutableIntStateOf(0)
        private set

    var completedSheetCount by mutableIntStateOf(0)
        private set

    init {
        reload()
    }

    fun reload() {
        android.util.Log.d("SheetListViewModel", "ON reload")
        viewModelScope.launch {
            sheets = rewardSheetRepository.findAll()
            exchangeableSheetCount = rewardSheetRepository.countExchangeable()
            completedSheetCount = completedRewardSheetRepository.countAll()
            latestStamps = stampRepository.findLatestByEachSheet()
        }
    }
}
