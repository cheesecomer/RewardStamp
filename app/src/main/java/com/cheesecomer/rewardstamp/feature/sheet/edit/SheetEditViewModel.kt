package com.cheesecomer.rewardstamp.feature.sheet.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheesecomer.rewardstamp.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardstamp.data.repository.RewardSheetRepository
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.ui.component.MAX_GOAL_COUNT
import com.cheesecomer.rewardstamp.ui.component.MIN_GOAL_COUNT
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class SheetEditViewModel(
    private val rewardSheetRepository: RewardSheetRepository,
    private val milestoneRepository: RewardMilestoneRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SheetEditUiState())
    val uiState: StateFlow<SheetEditUiState> = _uiState.asStateFlow()

    private var loadedSheetId: Long? = null

    fun load(sheetId: Long) {
        if (loadedSheetId == sheetId) return
        loadedSheetId = sheetId

        viewModelScope.launch {
            val sheet = rewardSheetRepository.findById(sheetId)
            if (sheet == null) return@launch

            val milestones = milestoneRepository.findBySheetId(sheetId)
            _uiState.update {
                it.copy(
                    sheetId = sheetId,
                    title = sheet.title,
                    goalCount = sheet.goalCount,
                    currentCount = sheet.currentCount,
                    milestones = milestones,
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun incrementGoalCount() {
        _uiState.update {
            if (it.goalCount >= MAX_GOAL_COUNT) it else it.copy(goalCount = it.goalCount + 1)
        }
    }

    fun decrementGoalCount() {
        _uiState.update {
            if (it.goalCount <= maxOf(MIN_GOAL_COUNT, it.currentCount)) it else it.copy(goalCount = it.goalCount - 1)
        }
    }

    fun createMilestone(milestone: RewardMilestone) {
        _uiState.update { it.copy(milestones = it.milestones + milestone) }
    }

    fun removeMilestone(index: Int) {
        _uiState.update {
            it.copy(
                milestones =
                    it.milestones.filterIndexed { i, _ ->
                        i != index
                    },
            )
        }
    }

    fun updateMilestone(
        index: Int,
        value: RewardMilestone,
    ) {
        _uiState.update {
            it.copy(
                milestones =
                    it.milestones.mapIndexed { i, milestone ->
                        if (index == i) {
                            milestone.copy(
                                reward = value.reward,
                                requiredSheetCount = value.requiredSheetCount,
                            )
                        } else {
                            milestone
                        }
                    },
            )
        }
    }

    suspend fun createRewardSheet(): RewardSheet {
        val uiState = _uiState.value
        return RewardSheet(
            id = uiState.sheetId,
            title = uiState.title,
            currentCount =
                uiState.sheetId
                    .takeIf { it != 0L }
                    ?.let {
                        rewardSheetRepository.findById(it)?.currentCount
                    } ?: 0,
            goalCount = uiState.goalCount,
        )
    }

    private var saveJob: Job? = null

    fun save(onSaved: () -> Unit) {
        if (!_uiState.value.canSave() || saveJob?.isActive == true) {
            return
        }

        saveJob =
            viewModelScope.launch {
                val sheetId =
                    rewardSheetRepository.save(
                        createRewardSheet(),
                    )
                val milestones =
                    _uiState.value.milestones.map {
                        RewardMilestone(
                            id = it.id,
                            sheetId = sheetId,
                            requiredSheetCount = it.requiredSheetCount,
                            reward = it.reward,
                        )
                    }
                milestoneRepository.saveAll(sheetId, milestones)

                onSaved()
            }
    }
}
