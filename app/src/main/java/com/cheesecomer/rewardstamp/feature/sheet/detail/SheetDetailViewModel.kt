package com.cheesecomer.rewardstamp.feature.sheet.detail

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
import com.cheesecomer.rewardstamp.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardstamp.data.repository.RewardSheetRepository
import com.cheesecomer.rewardstamp.data.repository.RewardStampRepository
import com.cheesecomer.rewardstamp.model.CompletedRewardSheet
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.model.RewardStamp
import com.cheesecomer.rewardstamp.model.StampType
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
            val sheet = rewardSheetRepository.findById(sheetId)
            uiState =
                uiState.copy(
                    sheet = sheet,
                    stamps = rewardStampRepository.findBySheetId(sheetId),
                )

            if (sheet != null && sheet.currentCount >= sheet.goalCount) {
                val exchangeableSheetCount =
                    completedRewardSheetRepository
                        .countExchangeableBySheetId(sheetId) + 1
                val goalStampType = GoalStampType.entries.random()
                uiState =
                    uiState.copy(
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
                        goalStampType = goalStampType,
                    )
                createCompletedRewardSheet(sheet, goalStampType)
            }
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

            if (sheet != null && sheet.currentCount >= sheet.goalCount) {
                val exchangeableSheetCount =
                    completedRewardSheetRepository
                        .countExchangeableBySheetId(sheetId) + 1
                val goalStampType = GoalStampType.entries.random()
                uiState =
                    uiState.copy(
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
                        goalStampType = goalStampType,
                    )
                createCompletedRewardSheet(sheet, goalStampType)
            }
        }
    }

    suspend fun createCompletedRewardSheet(
        sheet: RewardSheet,
        goalStampType: GoalStampType,
    ) {
        val completedRewardSheetId =
            completedRewardSheetRepository.save(
                CompletedRewardSheet(
                    id = 0,
                    sheetId = sheet.id,
                    title = sheet.title,
                    goalCount = sheet.goalCount,
                    goalStampType = goalStampType,
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
