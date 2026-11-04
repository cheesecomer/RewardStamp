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
import com.cheesecomer.rewardstamp.data.repository.SheetCompletionRepository
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.model.RewardStamp
import com.cheesecomer.rewardstamp.model.StampType
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class SheetDetailViewModel(
    private val rewardSheetRepository: RewardSheetRepository,
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
    private val rewardStampRepository: RewardStampRepository,
    private val rewardMilestoneRepository: RewardMilestoneRepository,
    private val sheetCompletionRepository: SheetCompletionRepository,
    private val now: () -> LocalDateTime = { LocalDateTime.now() },
) : ViewModel() {
    @ExcludeFromCoverage
    companion object {
        fun factory(
            rewardSheetRepository: RewardSheetRepository,
            completedRewardSheetRepository: CompletedRewardSheetRepository,
            rewardStampRepository: RewardStampRepository,
            rewardMilestoneRepository: RewardMilestoneRepository,
            sheetCompletionRepository: SheetCompletionRepository,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SheetDetailViewModel(
                        rewardSheetRepository,
                        completedRewardSheetRepository,
                        rewardStampRepository,
                        rewardMilestoneRepository,
                        sheetCompletionRepository,
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
                    goalStampType = null,
                )

            if (sheet != null && sheet.isCompleted) {
                complete(sheet)
            }
        }
    }

    private var incrementJob: Job? = null

    fun increment(
        sheetId: Long,
        stampType: StampType,
    ) {
        if (incrementJob?.isActive == true) return

        incrementJob =
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

                if (sheet != null && sheet.isCompleted) {
                    complete(sheet)
                }
            }
    }

    private suspend fun complete(sheet: RewardSheet) {
        val exchangeableSheetCount =
            completedRewardSheetRepository
                .countExchangeableBySheetId(sheet.id) + 1
        val goalStampType = GoalStampType.entries.random()

        if (!sheetCompletionRepository.complete(sheet.id, goalStampType)) {
            return
        }

        uiState =
            uiState.copy(
                exchangeableRewards =
                    rewardMilestoneRepository
                        .findExchangeableMilestonesBySheetId(
                            sheet.id,
                            exchangeableSheetCount,
                        ),
                lockedRewards =
                    rewardMilestoneRepository
                        .findLockedMilestonesBySheetId(
                            sheet.id,
                            exchangeableSheetCount,
                        ),
                goalStampType = goalStampType,
            )
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
