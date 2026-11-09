package com.cheesecomer.rewardstamp.feature.setting

import com.cheesecomer.rewardstamp.model.LockTarget

internal object SettingsTestTags {
    const val ADULT_LOCK_SWITCH = "SettingsScreen.AdultLockSwitch"
    const val VIBRATION_SWITCH = "SettingsScreen.VibrationSwitch"
    const val GOAL_CELEBRATION_SWITCH = "SettingsScreen.GoalCelebrationSwitch"
    const val VERSION = "SettingsScreen.Version"
    const val PRIVACY_POLICY = "SettingsScreen.PrivacyPolicy"
    const val CONTACT = "SettingsScreen.Contact"
    const val RATE_APP = "SettingsScreen.RateApp"
    const val LICENSES = "SettingsScreen.Licenses"

    fun lockTarget(target: LockTarget) = "SettingsScreen.LockTarget.${target.name}"
}
