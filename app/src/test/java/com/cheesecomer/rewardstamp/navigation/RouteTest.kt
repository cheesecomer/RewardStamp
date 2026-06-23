package com.cheesecomer.rewardstamp.navigation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RouteTest {
    @Test
    fun sheetDetail_returnsRoute() {
        assertThat(Route.sheetDetail(123L))
            .isEqualTo("sheets/123")
    }

    @Test
    fun sheetEdit_returnsRoute() {
        assertThat(Route.sheetEdit(123L))
            .isEqualTo("sheets/123/edit")
    }

    @Test
    fun completedRewardDetail_returnsRoute() {
        assertThat(Route.completedSheetDetail(456L))
            .isEqualTo("sheets/completed/456")
    }

    @Test
    fun routeConstants_areCorrect() {
        assertThat(Route.SHEET_LIST).isEqualTo("sheets")
        assertThat(Route.SHEET_NEW).isEqualTo("sheets/new")
        assertThat(Route.SHEET_EDIT).isEqualTo("sheets/{sheetId}/edit")
        assertThat(Route.SHEET_DETAIL).isEqualTo("sheets/{sheetId}")
        assertThat(Route.EXCHANGEABLE_SHEET_LIST).isEqualTo("sheets/exchangeable")
        assertThat(Route.COMPLETED_SHEET_LIST).isEqualTo("sheets/completed")
        assertThat(Route.COMPLETED_SHEET_DETAIL)
            .isEqualTo("sheets/completed/{completedSheetId}")
    }

    @Test
    fun bottomTabReturnsSheetsForSheetRoutes() {
        listOf(
            Route.SHEET_LIST,
            Route.SHEET_NEW,
            Route.SHEET_EDIT,
            Route.SHEET_DETAIL,
        ).forEach { route ->
            assertThat(Route.bottomTab(route))
                .isEqualTo(BottomTab.Sheets)
        }
    }

    @Test
    fun bottomTabReturnsRewardsForExchangeableSheetList() {
        assertThat(Route.bottomTab(Route.EXCHANGEABLE_SHEET_LIST))
            .isEqualTo(BottomTab.Rewards)
    }

    @Test
    fun bottomTabReturnsHistoryForCompletedSheetRoutes() {
        listOf(
            Route.COMPLETED_SHEET_DETAIL,
            Route.COMPLETED_SHEET_LIST,
        ).forEach { route ->
            assertThat(Route.bottomTab(route))
                .isEqualTo(BottomTab.History)
        }
    }

    @Test
    fun bottomTabReturnsSettingsForSettingsRoute() {
        assertThat(Route.bottomTab(Route.SETTINGS))
            .isEqualTo(BottomTab.Settings)
    }

    @Test
    fun bottomTabReturnsNullForUnknownRoute() {
        assertThat(Route.bottomTab("unknown"))
            .isNull()
        assertThat(Route.bottomTab(null))
            .isNull()
    }
}
