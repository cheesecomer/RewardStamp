package com.cheesecomer.rewardstamp.feature.setting

import com.cheesecomer.rewardstamp.model.AppSettings

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
)
