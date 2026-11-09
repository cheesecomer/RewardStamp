package com.cheesecomer.rewardstamp.model

data class AppSettings(
    val vibrationEnabled: Boolean = true,
    val goalCelebrationEnabled: Boolean = true,
    val adultLockEnabled: Boolean = false,
    // Only stamping by default, as children rarely reach the other actions
    val lockTargets: Set<LockTarget> = setOf(LockTarget.Stamp),
) {
    // The lock settings are always locked, so that children cannot turn the lock off
    fun isLocked(target: LockTarget): Boolean =
        adultLockEnabled &&
            (target == LockTarget.LockSettings || target in lockTargets)
}
