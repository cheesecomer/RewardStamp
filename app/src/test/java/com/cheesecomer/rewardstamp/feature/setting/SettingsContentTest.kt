package com.cheesecomer.rewardstamp.feature.setting

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val openedUris = mutableListOf<String>()

    private val uriHandler =
        object : UriHandler {
            override fun openUri(uri: String) {
                openedUris += uri
            }
        }

    private fun setContent(
        settings: AppSettings = AppSettings(),
        uriHandler: UriHandler = this.uriHandler,
        onVibrationChange: (Boolean) -> Unit = {},
        onGoalCelebrationChange: (Boolean) -> Unit = {},
        onLicensesClick: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            RewardStampTheme {
                CompositionLocalProvider(LocalUriHandler provides uriHandler) {
                    SettingsContent(
                        settings = settings,
                        versionName = "1.2.3",
                        onVibrationChange = onVibrationChange,
                        onGoalCelebrationChange = onGoalCelebrationChange,
                        onLicensesClick = onLicensesClick,
                    )
                }
            }
        }
    }

    @Test
    fun displaysSettings() {
        setContent(
            settings =
                AppSettings(
                    vibrationEnabled = true,
                    goalCelebrationEnabled = false,
                ),
        )

        composeTestRule
            .onNodeWithTag(SettingsTestTags.VIBRATION_SWITCH)
            .assertIsOn()
        composeTestRule
            .onNodeWithTag(SettingsTestTags.GOAL_CELEBRATION_SWITCH)
            .assertIsOff()
        composeTestRule
            .onNodeWithTag(SettingsTestTags.VERSION)
            .performScrollTo()
        composeTestRule
            .onNodeWithText("1.2.3")
            .assertIsDisplayed()
    }

    @Test
    fun clickVibration_callsCallbackWithToggledValue() {
        var vibrationEnabled: Boolean? = null
        setContent(
            settings = AppSettings(vibrationEnabled = true),
            onVibrationChange = { vibrationEnabled = it },
        )

        composeTestRule
            .onNodeWithTag(SettingsTestTags.VIBRATION_SWITCH)
            .performClick()

        assertThat(vibrationEnabled).isFalse()
    }

    @Test
    fun clickGoalCelebration_callsCallbackWithToggledValue() {
        var goalCelebrationEnabled: Boolean? = null
        setContent(
            settings = AppSettings(goalCelebrationEnabled = false),
            onGoalCelebrationChange = { goalCelebrationEnabled = it },
        )

        composeTestRule
            .onNodeWithTag(SettingsTestTags.GOAL_CELEBRATION_SWITCH)
            .performClick()

        assertThat(goalCelebrationEnabled).isTrue()
    }

    @Test
    fun clickLinks_opensUris() {
        setContent()

        listOf(
            SettingsTestTags.PRIVACY_POLICY,
            SettingsTestTags.CONTACT,
            SettingsTestTags.RATE_APP,
        ).forEach { tag ->
            composeTestRule
                .onNodeWithTag(tag)
                .performScrollTo()
                .performClick()
        }

        assertThat(openedUris)
            .containsExactly(
                SettingsLinks.PRIVACY_POLICY,
                SettingsLinks.CONTACT,
                SettingsLinks.RATE_APP,
            ).inOrder()
    }

    @Test
    fun clickLicenses_callsCallback() {
        var clicked = false
        setContent(onLicensesClick = { clicked = true })

        composeTestRule
            .onNodeWithTag(SettingsTestTags.LICENSES)
            .performScrollTo()
            .performClick()

        assertThat(clicked).isTrue()
        assertThat(openedUris).isEmpty()
    }

    @Test
    fun clickLink_ignoresMissingApp() {
        setContent(
            uriHandler =
                object : UriHandler {
                    override fun openUri(uri: String): Unit = throw IllegalArgumentException(uri)
                },
        )

        composeTestRule
            .onNodeWithTag(SettingsTestTags.CONTACT)
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithText("せってい")
            .assertIsDisplayed()
    }
}
