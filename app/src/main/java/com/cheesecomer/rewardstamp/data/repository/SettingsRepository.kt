package com.cheesecomer.rewardstamp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.cheesecomer.rewardstamp.model.AppSettings
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
            )
        }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        dataStore.edit { it[VIBRATION_ENABLED] = enabled }
    }

    suspend fun setGoalCelebrationEnabled(enabled: Boolean) {
        dataStore.edit { it[GOAL_CELEBRATION_ENABLED] = enabled }
    }

    private companion object {
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val GOAL_CELEBRATION_ENABLED = booleanPreferencesKey("goal_celebration_enabled")
    }
}
