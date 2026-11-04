package com.cheesecomer.rewardstamp.feature.sheet.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    private val _uiState = MutableStateFlow(SheetDetailUiState())
    val uiState: StateFlow<SheetDetailUiState> = _uiState.asStateFlow()

    fun load(sheetId: Long) {
        val sheet = _uiState.value.sheet
        if (sheet != null && sheet.id == sheetId && sheet.isCompleted) return

        reload(sheetId)
    }

    private fun reload(sheetId: Long) {
        viewModelScope.launch {
            val sheet = rewardSheetRepository.findById(sheetId)
            val stamps = rewardStampRepository.findBySheetId(sheetId)
            _uiState.update {
                it.copy(
                    sheet = sheet,
                    stamps = stamps,
                    goalStampType = null,
                )
            }

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
                val sheetBefore = _uiState.value.sheet ?: return@launch
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
                _uiState.update {
                    it.copy(
                        sheet = sheet,
                        stamps = stamps,
                    )
                }

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

        val exchangeableRewards =
            rewardMilestoneRepository.findExchangeableMilestonesBySheetId(
                sheet.id,
                exchangeableSheetCount,
            )
        val lockedRewards =
            rewardMilestoneRepository.findLockedMilestonesBySheetId(
                sheet.id,
                exchangeableSheetCount,
            )
        _uiState.update {
            it.copy(
                exchangeableRewards = exchangeableRewards,
                lockedRewards = lockedRewards,
                goalStampType = goalStampType,
            )
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
        reload(sheetId)
    }
}
