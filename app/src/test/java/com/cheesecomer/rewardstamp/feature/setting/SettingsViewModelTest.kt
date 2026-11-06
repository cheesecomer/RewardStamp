package com.cheesecomer.rewardstamp.feature.setting

import com.cheesecomer.rewardstamp.data.InMemoryPreferencesDataStore
import com.cheesecomer.rewardstamp.data.repository.SettingsRepository
import com.cheesecomer.rewardstamp.model.AppSettings
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsViewModelTest {
    private val settingsRepository = SettingsRepository(InMemoryPreferencesDataStore())

    @Test
    fun init_loadsSettings() =
        runTest {
            settingsRepository.setVibrationEnabled(false)

            val viewModel = SettingsViewModel(settingsRepository)

            assertThat(viewModel.uiState.value.settings)
                .isEqualTo(AppSettings(vibrationEnabled = false, goalCelebrationEnabled = true))
        }

    @Test
    fun setVibrationEnabled_savesAndUpdatesState() =
        runTest {
            val viewModel = SettingsViewModel(settingsRepository)

            viewModel.setVibrationEnabled(false)

            assertThat(settingsRepository.settings.first().vibrationEnabled).isFalse()
            assertThat(viewModel.uiState.value.settings.vibrationEnabled).isFalse()
        }

    @Test
    fun setGoalCelebrationEnabled_savesAndUpdatesState() =
        runTest {
            val viewModel = SettingsViewModel(settingsRepository)

            viewModel.setGoalCelebrationEnabled(false)

            assertThat(settingsRepository.settings.first().goalCelebrationEnabled).isFalse()
            assertThat(viewModel.uiState.value.settings.goalCelebrationEnabled).isFalse()
        }
}
