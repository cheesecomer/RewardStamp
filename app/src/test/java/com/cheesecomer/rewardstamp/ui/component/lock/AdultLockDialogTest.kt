package com.cheesecomer.rewardstamp.ui.component.lock

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget
import com.cheesecomer.rewardstamp.model.UnlockProblem
import com.cheesecomer.rewardstamp.ui.component.dialog.DialogButtonsTestTags
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
// The dialog is taller than the default screen of Robolectric, where keys outside it cannot be clicked
@Config(qualifiers = "w411dp-h891dp")
class AdultLockDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val answers = mutableListOf<Int>()
    private var dismissed = false

    private fun setDialog(correctAnswer: Int = 85) {
        composeTestRule.setContent {
            RewardStampTheme {
                UnlockDialog(
                    problem = UnlockProblem(left = 38, right = 47),
                    onAnswer = { answer ->
                        answers += answer
                        answer == correctAnswer
                    },
                    onDismissRequest = { dismissed = true },
                )
            }
        }
    }

    private fun press(vararg digits: Int) {
        digits.forEach { composeTestRule.onNodeWithTag(AdultLockDialogTestTags.digit(it)).performClick() }
    }

    @Test
    fun displaysProblem() {
        setDialog()

        composeTestRule.onNodeWithText("38 ＋ 47 ＝ ？").assertIsDisplayed()
        composeTestRule.onNodeWithText("答えを入力").assertIsDisplayed()
        composeTestRule.onNodeWithTag(DialogButtonsTestTags.CONFIRM_BUTTON).assertIsNotEnabled()
    }

    @Test
    fun numberPad_editsAnswer() {
        setDialog()

        press(1, 2, 3, 4)
        composeTestRule.onNodeWithText("123").assertIsDisplayed()

        composeTestRule.onNodeWithTag(AdultLockDialogTestTags.BACKSPACE).performClick()
        composeTestRule.onNodeWithText("12").assertIsDisplayed()

        composeTestRule.onNodeWithTag(AdultLockDialogTestTags.CLEAR).performClick()
        composeTestRule.onNodeWithText("答えを入力").assertIsDisplayed()
    }

    @Test
    fun allDigits_canBeTyped() {
        setDialog(correctAnswer = -1)

        (0..9).forEach { digit ->
            press(digit)
            composeTestRule.onNodeWithTag(DialogButtonsTestTags.CONFIRM_BUTTON).performClick()
        }

        assertThat(answers).containsExactlyElementsIn(0..9).inOrder()
    }

    @Test
    fun wrongAnswer_clearsAndTellsSo() {
        setDialog()

        press(8, 4)
        composeTestRule.onNodeWithTag(DialogButtonsTestTags.CONFIRM_BUTTON).performClick()

        assertThat(answers).containsExactly(84)
        composeTestRule.onNodeWithTag(AdultLockDialogTestTags.WRONG_ANSWER).assertIsDisplayed()
        composeTestRule.onNodeWithText("答えを入力").assertIsDisplayed()
    }

    @Test
    fun rightAnswer_isPassed() {
        setDialog()

        press(8, 5)
        composeTestRule.onNodeWithTag(DialogButtonsTestTags.CONFIRM_BUTTON).performClick()

        assertThat(answers).containsExactly(85)
    }

    @Test
    fun cancel_dismisses() {
        setDialog()

        composeTestRule.onNodeWithTag(DialogButtonsTestTags.DISMISS_BUTTON).performClick()

        assertThat(dismissed).isTrue()
    }

    @Test
    fun adultLockDialog_showsWhileAskingAndRunsAfterUnlock() {
        var unlocked = false
        lateinit var state: AdultLockState
        composeTestRule.setContent {
            RewardStampTheme {
                state = rememberAdultLockState(AppSettings(adultLockEnabled = true), Random(0))
                AdultLockDialog(state)
            }
        }
        composeTestRule.onNodeWithTag(AdultLockDialogTestTags.ROOT).assertDoesNotExist()

        composeTestRule.runOnIdle { state.runUnlocked(LockTarget.Stamp) { unlocked = true } }
        composeTestRule.onNodeWithTag(AdultLockDialogTestTags.ROOT).assertExists()

        composeTestRule.unlock(state)

        assertThat(unlocked).isTrue()
        composeTestRule.onNodeWithTag(AdultLockDialogTestTags.ROOT).assertDoesNotExist()
    }
}
