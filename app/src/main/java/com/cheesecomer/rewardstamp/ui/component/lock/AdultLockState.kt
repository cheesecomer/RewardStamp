package com.cheesecomer.rewardstamp.ui.component.lock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget
import com.cheesecomer.rewardstamp.model.UnlockProblem
import kotlin.random.Random

/**
 * Asks an adult to solve [UnlockProblem] before a locked action runs (docs/usecases.md「おとなロック」).
 * An unlock kept with `keepsUnlocked` lasts until [lock] is called or the screen is left.
 */
@Stable
class AdultLockState internal constructor(
    private val settings: () -> AppSettings,
    private val random: Random,
) {
    private class Pending(
        val target: LockTarget,
        val keepsUnlocked: Boolean,
        val action: () -> Unit,
    )

    private var pending: Pending? = null
    private var unlockedTargets by mutableStateOf(emptySet<LockTarget>())

    var problem by mutableStateOf<UnlockProblem?>(null)
        private set

    fun isLocked(target: LockTarget): Boolean = settings().isLocked(target) && target !in unlockedTargets

    fun runUnlocked(
        target: LockTarget,
        keepsUnlocked: Boolean = false,
        action: () -> Unit,
    ) {
        if (!isLocked(target)) {
            action()
            return
        }
        pending = Pending(target, keepsUnlocked, action)
        problem = UnlockProblem.random(random)
    }

    // Returns false when the answer is wrong, so that the dialog can ask again
    fun answer(value: Int): Boolean {
        val unlocked = pending
        if (unlocked == null || value != problem?.answer) return false

        pending = null
        problem = null
        if (unlocked.keepsUnlocked) unlockedTargets = unlockedTargets + unlocked.target
        unlocked.action()
        return true
    }

    fun cancel() {
        pending = null
        problem = null
    }

    fun lock(target: LockTarget) {
        unlockedTargets = unlockedTargets - target
    }
}

@Composable
fun rememberAdultLockState(
    settings: AppSettings = AppSettings(),
    random: Random = Random.Default,
): AdultLockState {
    val currentSettings by rememberUpdatedState(settings)
    return remember {
        AdultLockState(settings = { currentSettings }, random = random)
    }
}
