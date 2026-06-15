package com.cheesecomer.rewardseal.ui.screen.sheetdetail

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
import com.cheesecomer.rewardseal.data.repository.RewardStampRepository
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.model.StampType
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class SheetDetailViewModel(
    private val rewardSheetRepository: RewardSheetRepository,
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
    private val rewardStampRepository: RewardStampRepository
) : ViewModel() {
    companion object {
        fun factory(
            rewardSheetRepository: RewardSheetRepository,
            completedRewardSheetRepository: CompletedRewardSheetRepository,
            rewardStampRepository: RewardStampRepository
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SheetDetailViewModel(
                        rewardSheetRepository,
                        completedRewardSheetRepository,
                        rewardStampRepository
                    )
                }
            }
    }

    var uiState by mutableStateOf(
        SheetDetailUiState()
    )
        private set

    fun load(sheetId: Long) {
        viewModelScope.launch {
            uiState = uiState.copy(
                sheet = rewardSheetRepository.findById(sheetId),
                stamps = rewardStampRepository.findBySheetId(sheetId),
            )
        }
    }

    fun increment(sheetId: Long, stampType: StampType) {
        viewModelScope.launch {
            val sheetBefore = uiState.sheet ?: return@launch
            if (!rewardSheetRepository.increment(sheetId)) {
                return@launch
            }

            rewardStampRepository.save(
                RewardStamp(
                    id = 0,
                    sheetId = sheetId,
                    completedRewardSheetId = null,
                    position = sheetBefore.currentCount,
                    stampType = stampType,
                    stampedAt = LocalDateTime.now(),
                )
            )

            val sheet = rewardSheetRepository.findById(sheetId)
            val stamps = rewardStampRepository.findBySheetId(sheetId)
            uiState = uiState.copy(
                sheet = sheet,
                stamps = stamps)
            sheet
                ?.takeIf { it.currentCount == it.goalCount }
                ?.let(::createCompletedRewardSheet)
        }
    }

    fun createCompletedRewardSheet(sheet: RewardSheet) {
        viewModelScope.launch {
            val completedRewardSheetId = completedRewardSheetRepository.save(
                CompletedRewardSheet(
                    id = 0,
                    sheetId = sheet.id,
                    title = sheet.title,
                    reward = sheet.reward,
                    goalCount = sheet.goalCount,
                    completedAt = LocalDateTime.now(),
                    rewardReceived = false
                )
            )
            rewardStampRepository.attachToCompletedRewardSheet(
                sheetId = sheet.id,
                completedRewardSheetId = completedRewardSheetId,
            )
        }
    }

    fun delete(sheetId: Long) {
        viewModelScope.launch {
            rewardSheetRepository.delete(sheetId)
        }
    }

    fun restart(sheetId: Long) {
        viewModelScope.launch {
            rewardSheetRepository.restart(sheetId)
        }
        load(sheetId)
    }
}