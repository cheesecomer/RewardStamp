package com.cheesecomer.rewardstamp.ui.component.lock

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.ui.component.dialog.DialogButtonsTestTags

/** Types [answer] with the number pad of the unlock dialog and confirms it */
fun ComposeContentTestRule.answerUnlockDialog(answer: Int) {
    answer.toString().forEach { digit ->
        onNodeWithTag(AdultLockDialogTestTags.digit(digit.digitToInt())).performClick()
    }
    onNodeWithTag(DialogButtonsTestTags.CONFIRM_BUTTON).performClick()
}

/** Solves the problem the lock is asking */
fun ComposeContentTestRule.unlock(state: AdultLockState) {
    answerUnlockDialog(checkNotNull(state.problem) { "ロックの問題が出ていない" }.answer)
}
