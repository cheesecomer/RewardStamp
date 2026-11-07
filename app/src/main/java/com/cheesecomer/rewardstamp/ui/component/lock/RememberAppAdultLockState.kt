package com.cheesecomer.rewardstamp.ui.component.lock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheesecomer.rewardstamp.RewardStampApplication
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.AppSettings

// The lock of a screen, following the saved settings. Problems use the random numbers of the app (fixed in screenshots)
@ExcludeFromCoverage
@Composable
fun rememberAppAdultLockState(): AdultLockState {
    val application = LocalContext.current.applicationContext as RewardStampApplication
    val settings by application.settingsRepository.settings.collectAsStateWithLifecycle(AppSettings())
    return rememberAdultLockState(settings = settings, random = application.random)
}
