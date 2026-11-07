package com.cheesecomer.rewardstamp.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AppSettingsTest {
    @Test
    fun adultLockDisabled_locksNothing() {
        val settings = AppSettings(adultLockEnabled = false, stampLockEnabled = true)

        assertThat(settings.isLocked(LockTarget.Stamp)).isFalse()
        assertThat(settings.isLocked(LockTarget.ParentAction)).isFalse()
    }

    @Test
    fun adultLockEnabled_locksParentActionsAndStamps() {
        val settings = AppSettings(adultLockEnabled = true, stampLockEnabled = true)

        assertThat(settings.isLocked(LockTarget.Stamp)).isTrue()
        assertThat(settings.isLocked(LockTarget.ParentAction)).isTrue()
    }

    @Test
    fun stampLockDisabled_leavesStampsToChildren() {
        val settings = AppSettings(adultLockEnabled = true, stampLockEnabled = false)

        assertThat(settings.isLocked(LockTarget.Stamp)).isFalse()
        assertThat(settings.isLocked(LockTarget.ParentAction)).isTrue()
    }
}
