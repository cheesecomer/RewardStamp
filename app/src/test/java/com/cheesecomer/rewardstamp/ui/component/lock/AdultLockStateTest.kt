package com.cheesecomer.rewardstamp.ui.component.lock

import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.random.Random

class AdultLockStateTest {
    private var settings = AppSettings(adultLockEnabled = true)
    private val state = AdultLockState(settings = { settings }, random = Random(0))
    private var runCount = 0

    private fun run(
        target: LockTarget = LockTarget.ParentAction,
        keepsUnlocked: Boolean = false,
    ) {
        state.runUnlocked(target, keepsUnlocked) { runCount++ }
    }

    private fun answerCorrectly(): Boolean = state.answer(checkNotNull(state.problem).answer)

    @Test
    fun notLocked_runsRightAway() {
        settings = AppSettings(adultLockEnabled = false)

        run()

        assertThat(runCount).isEqualTo(1)
        assertThat(state.problem).isNull()
    }

    @Test
    fun locked_asksBeforeRunning() {
        run()

        assertThat(runCount).isEqualTo(0)
        assertThat(state.problem).isNotNull()
    }

    @Test
    fun wrongAnswer_keepsAsking() {
        run()
        val problem = state.problem!!

        assertThat(state.answer(problem.answer + 1)).isFalse()

        assertThat(runCount).isEqualTo(0)
        assertThat(state.problem).isEqualTo(problem)
    }

    @Test
    fun rightAnswer_runsAction() {
        run()

        assertThat(answerCorrectly()).isTrue()

        assertThat(runCount).isEqualTo(1)
        assertThat(state.problem).isNull()
    }

    @Test
    fun answerWithoutProblem_isIgnored() {
        assertThat(state.answer(0)).isFalse()
    }

    @Test
    fun cancel_dropsAction() {
        run()

        state.cancel()

        assertThat(state.problem).isNull()
        assertThat(runCount).isEqualTo(0)
    }

    @Test
    fun withoutKeepsUnlocked_asksEveryTime() {
        run()
        answerCorrectly()

        run()

        assertThat(runCount).isEqualTo(1)
        assertThat(state.problem).isNotNull()
    }

    @Test
    fun keepsUnlocked_lastsUntilLocked() {
        run(LockTarget.Stamp, keepsUnlocked = true)
        answerCorrectly()

        run(LockTarget.Stamp, keepsUnlocked = true)
        assertThat(runCount).isEqualTo(2)
        assertThat(state.isLocked(LockTarget.ParentAction)).isTrue()

        state.lock(LockTarget.Stamp)
        run(LockTarget.Stamp, keepsUnlocked = true)
        assertThat(runCount).isEqualTo(2)
        assertThat(state.problem).isNotNull()
    }

    @Test
    fun followsSettingsChanges() {
        assertThat(state.isLocked(LockTarget.Stamp)).isTrue()

        settings = settings.copy(stampLockEnabled = false)

        assertThat(state.isLocked(LockTarget.Stamp)).isFalse()
    }
}
