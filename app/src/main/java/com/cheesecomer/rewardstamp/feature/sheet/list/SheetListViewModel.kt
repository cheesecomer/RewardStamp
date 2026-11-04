package com.cheesecomer.rewardstamp.feature.sheet.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.data.repository.RewardSheetRepository
import com.cheesecomer.rewardstamp.data.repository.RewardStampRepository
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.model.RewardStamp
import kotlinx.coroutines.launch

class SheetListViewModel(
    private val rewardSheetRepository: RewardSheetRepository,
    private val stampRepository: RewardStampRepository,
) : ViewModel() {
    @ExcludeFromCoverage
    companion object {
        fun factory(
            rewardSheetRepository: RewardSheetRepository,
            stampRepository: RewardStampRepository,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SheetListViewModel(
                        rewardSheetRepository,
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
        private set

    fun reload() {
        viewModelScope.launch {
            sheets = rewardSheetRepository.findAll()
            latestStamps = stampRepository.findLatestByEachSheet()
        }
    }
}
