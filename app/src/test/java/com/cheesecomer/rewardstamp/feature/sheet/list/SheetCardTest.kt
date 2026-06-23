package com.cheesecomer.rewardstamp.feature.sheet.list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.cheesecomer.rewardstamp.data.rewardSheet
import com.cheesecomer.rewardstamp.model.StampType
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SheetCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dosNotExistsStamp() {
        composeTestRule.setContent {
            SheetCard(
                sheet = rewardSheet(),
                lastStampType = null,
            )
        }

        composeTestRule
            .onNodeWithTag("SheetCard.LastStampView.Stamp")
            .assertDoesNotExist()
    }

    @Test
    fun existsStamp() {
        composeTestRule.setContent {
            SheetCard(
                sheet = rewardSheet(),
                lastStampType = StampType.Hippopotamus,
            )
        }

        composeTestRule
            .onNodeWithTag("SheetCard.LastStampView.Stamp")
            .assertExists()
    }

    @Test
    fun existsProgressStampRow_WhenGoalIs5() {
        composeTestRule.setContent {
            SheetCard(
                sheet =
                    rewardSheet(
                        goalCount = 5,
                    ),
                lastStampType = StampType.Hippopotamus,
            )
        }

        composeTestRule
            .onNodeWithTag("SheetCard.ProgressStampRow")
            .assertExists()
    }

    @Test
    fun existsProgressStampRow_WhenGoalIs10() {
        composeTestRule.setContent {
            SheetCard(
                sheet =
                    rewardSheet(
                        goalCount = 10,
                    ),
                lastStampType = StampType.Hippopotamus,
            )
        }

        composeTestRule
            .onNodeWithTag("SheetCard.ProgressStampRow")
            .assertExists()
    }

    @Test
    fun existsProgressStampRow_WhenGoalIs15() {
        composeTestRule.setContent {
            SheetCard(
                sheet =
                    rewardSheet(
                        goalCount = 15,
                    ),
                lastStampType = StampType.Hippopotamus,
            )
        }

        composeTestRule
            .onNodeWithTag("SheetCard.ProgressStampRow")
            .assertExists()
    }

    @Test
    fun existsProgressStampRow_WhenGoalIs20() {
        composeTestRule.setContent {
            SheetCard(
                sheet =
                    rewardSheet(
                        goalCount = 20,
                    ),
                lastStampType = StampType.Hippopotamus,
            )
        }

        composeTestRule
            .onNodeWithTag("SheetCard.ProgressStampRow")
            .assertExists()
    }

    @Test
    fun existsProgressStampRow_WhenGoalIs25() {
        composeTestRule.setContent {
            SheetCard(
                sheet =
                    rewardSheet(
                        goalCount = 25,
                    ),
                lastStampType = StampType.Hippopotamus,
            )
        }

        composeTestRule
            .onNodeWithTag("SheetCard.ProgressStampRow")
            .assertExists()
    }
}
