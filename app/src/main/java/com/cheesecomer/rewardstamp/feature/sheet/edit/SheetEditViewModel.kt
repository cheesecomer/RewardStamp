package com.cheesecomer.rewardstamp.feature.sheet.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardstamp.data.repository.RewardSheetRepository
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.ui.component.MAX_GOAL_COUNT
import com.cheesecomer.rewardstamp.ui.component.MIN_GOAL_COUNT
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class SheetEditViewModel(
    private val rewardSheetRepository: RewardSheetRepository,
    private val milestoneRepository: RewardMilestoneRepository,
) : ViewModel() {
    @ExcludeFromCoverage
    companion object {
        fun factory(
            rewardSheetRepository: RewardSheetRepository,
            milestoneRepository: RewardMilestoneRepository,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SheetEditViewModel(
                        rewardSheetRepository,
                        milestoneRepository,
                    )
                }
            }
    }

    var uiState by mutableStateOf(
        SheetEditUiState(),
    )
        private set

    private var loadedSheetId: Long? = null

    fun load(sheetId: Long) {
        if (loadedSheetId == sheetId) return
        loadedSheetId = sheetId

        viewModelScope.launch {
            val sheet = rewardSheetRepository.findById(sheetId)
            if (sheet == null) return@launch

            uiState =
                uiState.copy(
                    sheetId = sheetId,
                    title = sheet.title,
                    goalCount = sheet.goalCount,
                    milestones = milestoneRepository.findBySheetId(sheetId),
                )
        }
    }

    fun updateTitle(title: String) {
        uiState =
            uiState.copy(
                title = title,
            )
    }

    fun incrementGoalCount() {
        if (uiState.goalCount >= MAX_GOAL_COUNT) return

        uiState =
            uiState.copy(
                goalCount = uiState.goalCount + 1,
            )
    }

    fun decrementGoalCount() {
        if (uiState.goalCount <= MIN_GOAL_COUNT) return

        uiState =
            uiState.copy(
                goalCount = uiState.goalCount - 1,
            )
    }

    fun createMilestone(milestone: RewardMilestone) {
        uiState =
            uiState.copy(
                milestones = uiState.milestones + milestone,
            )
    }

    fun removeMilestone(index: Int) {
        uiState =
            uiState.copy(
                milestones =
                    uiState.milestones.filterIndexed { i, _ ->
                        i != index
                    },
            )
    }

    fun updateMilestone(
        index: Int,
        value: RewardMilestone,
    ) {
        uiState =
            uiState.copy(
                milestones =
                    uiState.milestones.mapIndexed { i, milestone ->
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

    suspend fun createRewardSheet(): RewardSheet =
        RewardSheet(
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

    fun canSave(): Boolean = uiState.canSave()

    private var saveJob: Job? = null

    fun save(onSaved: () -> Unit) {
        if (!canSave() || saveJob?.isActive == true) {
            return
        }

        saveJob =
            viewModelScope.launch {
                val sheetId =
                    rewardSheetRepository.save(
                        createRewardSheet(),
                    )
                val milestones =
                    uiState.milestones.map {
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
