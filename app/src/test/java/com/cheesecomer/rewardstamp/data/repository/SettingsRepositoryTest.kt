package com.cheesecomer.rewardstamp.data.repository

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.cheesecomer.rewardstamp.data.InMemoryPreferencesDataStore
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SettingsRepositoryTest {
    private val dataStore = InMemoryPreferencesDataStore()
    private val repository = SettingsRepository(dataStore)

    @Test
    fun settings_defaultsToEffectsOnAndAdultLockOff() =
        runTest {
            assertThat(repository.settings.first())
                .isEqualTo(
                    AppSettings(
                        vibrationEnabled = true,
                        goalCelebrationEnabled = true,
                        adultLockEnabled = false,
                        lockTargets = setOf(LockTarget.Stamp),
                    ),
                )
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

    @Test
    fun setAdultLockEnabled_updatesSettings() =
        runTest {
            repository.setAdultLockEnabled(true)

            assertThat(repository.settings.first().adultLockEnabled).isTrue()
        }

    @Test
    fun setLockTarget_addsAndRemovesTargets() =
        runTest {
            repository.setLockTarget(LockTarget.RewardExchange, locked = true)

            assertThat(repository.settings.first().lockTargets)
                .containsExactly(LockTarget.Stamp, LockTarget.RewardExchange)

            repository.setLockTarget(LockTarget.Stamp, locked = false)

            assertThat(repository.settings.first().lockTargets)
                .containsExactly(LockTarget.RewardExchange)
        }

    @Test
    fun setLockTarget_keepsEmptyTargets() =
        runTest {
            repository.setLockTarget(LockTarget.Stamp, locked = false)

            assertThat(repository.settings.first().lockTargets).isEmpty()
        }

    @Test
    fun settings_ignoresUnknownLockTargets() =
        runTest {
            dataStore.edit { it[stringSetPreferencesKey("lock_targets")] = setOf("Removed", "HistoryDelete") }

            assertThat(repository.settings.first().lockTargets)
                .containsExactly(LockTarget.HistoryDelete)
        }
}
