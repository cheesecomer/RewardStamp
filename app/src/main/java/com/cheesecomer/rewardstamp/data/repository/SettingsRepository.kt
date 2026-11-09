package com.cheesecomer.rewardstamp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>,
) {
    private val defaults = AppSettings()

    val settings: Flow<AppSettings> =
        dataStore.data.map { preferences ->
            AppSettings(
                vibrationEnabled = preferences[VIBRATION_ENABLED] ?: defaults.vibrationEnabled,
                goalCelebrationEnabled = preferences[GOAL_CELEBRATION_ENABLED] ?: defaults.goalCelebrationEnabled,
                adultLockEnabled = preferences[ADULT_LOCK_ENABLED] ?: defaults.adultLockEnabled,
                lockTargets = preferences.lockTargets(),
            )
        }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        dataStore.edit { it[VIBRATION_ENABLED] = enabled }
    }

    suspend fun setGoalCelebrationEnabled(enabled: Boolean) {
        dataStore.edit { it[GOAL_CELEBRATION_ENABLED] = enabled }
    }

    suspend fun setAdultLockEnabled(enabled: Boolean) {
        dataStore.edit { it[ADULT_LOCK_ENABLED] = enabled }
    }

    suspend fun setLockTarget(
        target: LockTarget,
        locked: Boolean,
    ) {
        dataStore.edit { preferences ->
            val targets = preferences.lockTargets()
            preferences[LOCK_TARGETS] = (if (locked) targets + target else targets - target).map { it.name }.toSet()
        }
    }

    // Names no longer in LockTarget are ignored
    private fun Preferences.lockTargets(): Set<LockTarget> =
        this[LOCK_TARGETS]
            ?.mapNotNull { name -> LockTarget.entries.find { it.name == name } }
            ?.toSet()
            ?: defaults.lockTargets

    private companion object {
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val GOAL_CELEBRATION_ENABLED = booleanPreferencesKey("goal_celebration_enabled")
        val ADULT_LOCK_ENABLED = booleanPreferencesKey("adult_lock_enabled")
        val LOCK_TARGETS = stringSetPreferencesKey("lock_targets")
    }
}
