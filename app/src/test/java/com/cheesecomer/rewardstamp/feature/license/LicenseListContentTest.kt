package com.cheesecomer.rewardstamp.feature.license

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.google.common.truth.Truth.assertThat
import com.mikepenz.aboutlibraries.Libs
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LicenseListContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val libraries =
        Libs
            .Builder()
            .withJson(
                """
                {
                  "libraries": [
                    {
                      "uniqueId": "com.fontna:nikumaru-font",
                      "name": "にくまるフォント",
                      "website": "http://www.fontna.com/blog/nikumaru_font.html",
                      "developers": [],
                      "funding": [{ "platform": "GitHub", "url": "https://github.com/sponsors/example" }],
                      "licenses": ["mplus-fonts"]
                    }
                  ],
                  "licenses": {
                    "mplus-fonts": {
                      "hash": "mplus-fonts",
                      "name": "M+ FONTS LICENSE",
                      "url": "https://mplusfonts.github.io/",
                      "content": "These fonts are free software."
                    }
                  }
                }
                """.trimIndent(),
            ).build()

    private fun setContent(
        libraries: Libs? = this.libraries,
        onBackClick: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            RewardStampTheme {
                LicenseListContent(
                    libraries = libraries,
                    onBackClick = onBackClick,
                )
            }
        }
    }

    @Test
    fun displaysLibrariesAndLicenses() {
        setContent()

        composeTestRule.onNodeWithText("ライセンス").assertIsDisplayed()
        composeTestRule.onNodeWithText("にくまるフォント").assertIsDisplayed()
        composeTestRule.onNodeWithText("M+ FONTS LICENSE").assertIsDisplayed()
    }

    @Test
    fun clickViewLicense_showsLicenseTextInDialog() {
        setContent()

        composeTestRule.onNodeWithText("にくまるフォント").performClick()
        composeTestRule.onNodeWithText("ライセンス全文").performClick()

        composeTestRule.onNodeWithText("These fonts are free software.").assertIsDisplayed()

        composeTestRule.onNodeWithText("閉じる").performClick()

        composeTestRule.onNodeWithText("These fonts are free software.").assertDoesNotExist()
    }

    @Test
    fun expandedLibrary_hidesSponsorLink() {
        setContent()

        composeTestRule.onNodeWithText("にくまるフォント").performClick()

        composeTestRule.onNodeWithText("Webサイト").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sponsor").assertDoesNotExist()
    }

    @Test
    fun displaysTitleWhileLoading() {
        setContent(libraries = null)

        composeTestRule.onNodeWithText("ライセンス").assertIsDisplayed()
        composeTestRule.onNodeWithTag(LicenseListTestTags.LIST).assertExists()
    }

    @Test
    fun clickBack_callsCallback() {
        var clicked = false
        setContent(onBackClick = { clicked = true })

        composeTestRule.onNodeWithContentDescription("戻る").performClick()

        assertThat(clicked).isTrue()
    }
}
