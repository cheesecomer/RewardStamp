package com.cheesecomer.rewardstamp.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AppSettingsTest {
    @Test
    fun adultLockDisabled_locksNothing() {
        val settings = AppSettings(adultLockEnabled = false, lockTargets = LockTarget.entries.toSet())

        LockTarget.entries.forEach { target ->
            assertThat(settings.isLocked(target)).isFalse()
        }
    }

    @Test
    fun adultLockEnabled_locksOnlyStampsByDefault() {
        val settings = AppSettings(adultLockEnabled = true)

        assertThat(settings.isLocked(LockTarget.Stamp)).isTrue()
        assertThat(settings.isLocked(LockTarget.RewardExchange)).isFalse()
        assertThat(settings.isLocked(LockTarget.SheetEdit)).isFalse()
        assertThat(settings.isLocked(LockTarget.HistoryDelete)).isFalse()
    }

    @Test
    fun adultLockEnabled_locksChosenTargets() {
        val settings =
            AppSettings(
                adultLockEnabled = true,
                lockTargets = setOf(LockTarget.RewardExchange, LockTarget.HistoryDelete),
            )

        assertThat(settings.isLocked(LockTarget.Stamp)).isFalse()
        assertThat(settings.isLocked(LockTarget.RewardExchange)).isTrue()
        assertThat(settings.isLocked(LockTarget.SheetEdit)).isFalse()
        assertThat(settings.isLocked(LockTarget.HistoryDelete)).isTrue()
    }

    @Test
    fun adultLockEnabled_alwaysLocksLockSettings() {
        val settings = AppSettings(adultLockEnabled = true, lockTargets = emptySet())

        assertThat(settings.isLocked(LockTarget.LockSettings)).isTrue()
    }

    @Test
    fun selectable_excludesLockSettings() {
        assertThat(LockTarget.selectable).doesNotContain(LockTarget.LockSettings)
        assertThat(LockTarget.selectable).containsExactlyElementsIn(LockTarget.entries - LockTarget.LockSettings)
    }
}
