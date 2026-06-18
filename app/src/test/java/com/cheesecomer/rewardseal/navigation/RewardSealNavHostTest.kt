package com.cheesecomer.rewardseal.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RewardSealNavHostTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var navigator: Navigator
    private lateinit var screenFactory: FakeScreenFactory

    @Before
    fun setUp() {
        composeTestRule.setContent {
            navController =
                TestNavHostController(LocalContext.current).apply {
                    navigatorProvider.addNavigator(ComposeNavigator())
                }

            navigator = mock<NavControllerNavigator>()
            screenFactory = FakeScreenFactory()

            RewardSealNavHost(
                navController = navController,
                navigator = navigator,
                screenFactory = screenFactory,
            )
        }
    }

    @Test
    fun startsWithSheetList() {
        assertThat(navController.currentDestination?.route)
            .isEqualTo(Route.SHEET_LIST)
    }

    @Test
    fun navigatesToSheetNewFromSheetList() {
        composeTestRule.onNodeWithText("create sheet").performClick()

        verify(navigator).toSheetNew()
    }

    @Test
    fun navigatesToSheetNew() {
        navController.navigate(Route.SHEET_NEW)

        composeTestRule.onNodeWithText("sheet edit").assertExists()
        assertThat(screenFactory.sheetEditArgs[0]).isEqualTo(null)
    }

    @Test
    fun navigatesToSheetDetailFromSheetList() {
        composeTestRule.onNodeWithText("open sheet").performClick()

        verify(navigator).toSheetDetail(1L)
    }

    @Test
    fun navigatesToSheetDetail() {
        navController.navigate(Route.sheetDetail(1L))
        composeTestRule.onNodeWithText("edit sheet").assertExists()
        assertThat(screenFactory.sheetDetailArgs[0]).isEqualTo(1L)
    }

    @Test
    fun navigatesToSheetEditFromSheetDetail() {
        navController.navigate(Route.sheetDetail(1L))

        composeTestRule.onNodeWithText("edit sheet").performClick()

        verify(navigator).toSheetEdit(1L)
    }

    @Test
    fun navigatesToExchangeableSheetList() {
        navController.navigate(Route.EXCHANGEABLE_SHEET_LIST)
        composeTestRule.onNodeWithText("exchangeable rewards").assertExists()
    }

    @Test
    fun navigatesToSheetEdit() {
        navController.navigate(Route.sheetEdit(1L))

        composeTestRule.onNodeWithText("sheet edit").assertExists()
        assertThat(screenFactory.sheetEditArgs[0]).isEqualTo(1L)
    }

    @Test
    fun navigatesToCompletedSheetList() {
        navController.navigate(Route.COMPLETED_SHEET_LIST)
        composeTestRule.onNodeWithText("open completed sheet").assertExists()
    }

    @Test
    fun navigatesToCompletedSheetDetailFromCompletedSheetList() {
        navController.navigate(Route.COMPLETED_SHEET_LIST)

        composeTestRule.onNodeWithText("open completed sheet").performClick()

        verify(navigator).toCompletedSheetDetail(1L)
    }

    @Test
    fun navigatesToCompletedSheetDetail() {
        navController.navigate(Route.completedSheetDetail(1L))
        composeTestRule.onNodeWithText("completed sheet detail").assertExists()
        assertThat(screenFactory.completedSheetDetailArgs[0]).isEqualTo(1L)
    }

    @Test
    fun navigatesToSetting() {
        navController.navigate(Route.SETTINGS)
        composeTestRule.onNodeWithText("settings").assertExists()
    }

    private class FakeScreenFactory : ScreenFactory {
        @Composable
        override fun SheetList(
            onSheetClick: (Long) -> Unit,
            onCreateSheetClick: () -> Unit,
        ) {
            Scaffold {
                Column(modifier = Modifier.padding(it)) {
                    Button(onClick = onCreateSheetClick) {
                        Text("create sheet")
                    }

                    Button(onClick = { onSheetClick(1L) }) {
                        Text("open sheet")
                    }
                }
            }
        }

        var sheetEditArgs: Array<Any?> = emptyArray()

        @Composable
        override fun SheetEdit(
            sheetId: Long?,
            onSaveClick: () -> Unit,
            onBackClick: () -> Unit,
        ) {
            sheetEditArgs =
                arrayOf(
                    sheetId,
                    onSaveClick,
                    onBackClick,
                )
            Scaffold {
                Column(modifier = Modifier.padding(it)) {
                    Text("sheet edit")
                }
            }
        }

        var sheetDetailArgs: Array<Any?> = emptyArray()

        @Composable
        override fun SheetDetail(
            sheetId: Long,
            onBackClick: () -> Unit,
            onEditClick: () -> Unit,
            onDeleteClick: () -> Unit,
            onRestartWithEditClick: () -> Unit,
        ) {
            sheetDetailArgs =
                arrayOf(
                    sheetId,
                    onBackClick,
                    onEditClick,
                    onDeleteClick,
                    onRestartWithEditClick,
                )
            Scaffold {
                Column(modifier = Modifier.padding(it)) {
                    Button(onClick = onEditClick) {
                        Text("edit sheet")
                    }
                }
            }
        }

        @Composable
        override fun ExchangeableRewardList() {
            Scaffold {
                Column(modifier = Modifier.padding(it)) {
                    Text("exchangeable rewards")
                }
            }
        }

        @Composable
        override fun CompletedSheetList(onRewardClick: (Long) -> Unit) {
            Scaffold {
                Column(modifier = Modifier.padding(it)) {
                    Button(onClick = { onRewardClick(1L) }) {
                        Text("open completed sheet")
                    }
                }
            }
        }

        var completedSheetDetailArgs: Array<Any?> = emptyArray()

        @Composable
        override fun CompletedSheetDetail(completedSheetId: Long) {
            completedSheetDetailArgs = arrayOf(completedSheetId)
            Scaffold {
                Column(modifier = Modifier.padding(it)) {
                    Text("completed sheet detail")
                }
            }
        }

        @Composable
        override fun Settings() {
            Scaffold {
                Column(modifier = Modifier.padding(it)) {
                    Text("settings")
                }
            }
        }
    }
}
