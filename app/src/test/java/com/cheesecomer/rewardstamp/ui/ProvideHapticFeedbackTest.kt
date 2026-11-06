package com.cheesecomer.rewardstamp.ui

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProvideHapticFeedbackTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val performed = mutableListOf<HapticFeedbackType>()

    private val hapticFeedback =
        object : HapticFeedback {
            override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) {
                performed += hapticFeedbackType
            }
        }

    private fun performInside(enabled: Boolean) {
        composeTestRule.setContent {
            CompositionLocalProvider(LocalHapticFeedback provides hapticFeedback) {
                ProvideHapticFeedback(enabled = enabled) {
                    LocalHapticFeedback.current.performHapticFeedback(HapticFeedbackType.Confirm)
                }
            }
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun enabled_performsHapticFeedback() {
        performInside(enabled = true)

        assertThat(performed).containsExactly(HapticFeedbackType.Confirm)
    }

    @Test
    fun disabled_ignoresHapticFeedback() {
        performInside(enabled = false)

        assertThat(performed).isEmpty()
    }
}
