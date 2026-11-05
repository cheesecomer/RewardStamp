package com.cheesecomer.rewardstamp.data.repository

import com.cheesecomer.rewardstamp.data.InMemoryPreferencesDataStore
import com.cheesecomer.rewardstamp.model.AppSettings
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SettingsRepositoryTest {
    private val repository = SettingsRepository(InMemoryPreferencesDataStore())

    @Test
    fun settings_defaultsToEnabled() =
        runTest {
            assertThat(repository.settings.first())
                .isEqualTo(AppSettings(vibrationEnabled = true, goalCelebrationEnabled = true))
        }

    @Test
    fun setVibrationEnabled_updatesSettings() =
        runTest {
            repository.setVibrationEnabled(false)

            assertThat(repository.settings.first())
                .isEqualTo(AppSettings(vibrationEnabled = false, goalCelebrationEnabled = true))
        }

    @Test
    fun setGoalCelebrationEnabled_updatesSettings() =
        runTest {
            repository.setGoalCelebrationEnabled(false)

            assertThat(repository.settings.first())
                .isEqualTo(AppSettings(vibrationEnabled = true, goalCelebrationEnabled = false))
        }
}
