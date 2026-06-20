package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardseal.data.repository.RewardSheetRepository
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
    private val rewardStampRepository: RewardStampRepository,
    private val rewardMilestoneRepository: RewardMilestoneRepository,
    private val now: () -> LocalDateTime = { LocalDateTime.now() },
) : ViewModel() {
    @ExcludeFromCoverage
    companion object {
        fun factory(
            rewardSheetRepository: RewardSheetRepository,
            completedRewardSheetRepository: CompletedRewardSheetRepository,
            rewardStampRepository: RewardStampRepository,
            rewardMilestoneRepository: RewardMilestoneRepository,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SheetDetailViewModel(
                        rewardSheetRepository,
                        completedRewardSheetRepository,
                        rewardStampRepository,
                        rewardMilestoneRepository,
                    )
                }
            }
    }

    var uiState by mutableStateOf(
        SheetDetailUiState(),
    )
        private set

    fun load(sheetId: Long) {
        viewModelScope.launch {
            val exchangeableSheetCount =
                completedRewardSheetRepository
                    .countExchangeableBySheetId(sheetId)
            uiState =
                uiState.copy(
                    sheet = rewardSheetRepository.findById(sheetId),
                    stamps = rewardStampRepository.findBySheetId(sheetId),
                    exchangeableRewards =
                        rewardMilestoneRepository
                            .findExchangeableMilestonesBySheetId(
                                sheetId,
                                exchangeableSheetCount,
                            ),
                    lockedRewards =
                        rewardMilestoneRepository
                            .findLockedMilestonesBySheetId(
                                sheetId,
                                exchangeableSheetCount,
                            ),
                )
        }
    }

    fun increment(
        sheetId: Long,
        stampType: StampType,
    ) {
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
                    stampedAt = now(),
                ),
            )

            val sheet = rewardSheetRepository.findById(sheetId)
            val stamps = rewardStampRepository.findBySheetId(sheetId)
            uiState =
                uiState.copy(
                    sheet = sheet,
                    stamps = stamps,
                )
            sheet
                ?.takeIf { it.currentCount == it.goalCount }
                ?.let(::createCompletedRewardSheet)
        }
    }

    fun createCompletedRewardSheet(sheet: RewardSheet) {
        viewModelScope.launch {
            val completedRewardSheetId =
                completedRewardSheetRepository.save(
                    CompletedRewardSheet(
                        id = 0,
                        sheetId = sheet.id,
                        title = sheet.title,
                        goalCount = sheet.goalCount,
                        completedAt = now(),
                        consumedAt = null,
                    ),
                )
            rewardStampRepository.attachToCompletedRewardSheet(
                sheetId = sheet.id,
                completedRewardSheetId = completedRewardSheetId,
            )
            rewardSheetRepository.restart(sheet.id)
        }
    }

    fun delete(
        sheetId: Long,
        onCompleted: () -> Unit,
    ) {
        viewModelScope.launch {
            rewardSheetRepository.delete(sheetId)
            onCompleted()
        }
    }

    fun restart(sheetId: Long) {
        load(sheetId)
    }
}
