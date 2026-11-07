package com.cheesecomer.rewardstamp.model

data class AppSettings(
    val vibrationEnabled: Boolean = true,
    val goalCelebrationEnabled: Boolean = true,
    val adultLockEnabled: Boolean = false,
    val stampLockEnabled: Boolean = true,
) {
    fun isLocked(target: LockTarget): Boolean =
        when (target) {
            LockTarget.Stamp -> adultLockEnabled && stampLockEnabled
            LockTarget.ParentAction -> adultLockEnabled
        }
}
