package com.cheesecomer.rewardstamp.navigation

import androidx.compose.runtime.Composable
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.feature.completedsheet.detail.CompletedSheetDetailScreen
import com.cheesecomer.rewardstamp.feature.completedsheet.list.CompletedSheetListScreen
import com.cheesecomer.rewardstamp.feature.exchangeablesheet.list.ExchangeableSheetListScreen
import com.cheesecomer.rewardstamp.feature.setting.SettingsScreen
import com.cheesecomer.rewardstamp.feature.sheet.detail.SheetDetailScreen
import com.cheesecomer.rewardstamp.feature.sheet.edit.SheetEditScreen
import com.cheesecomer.rewardstamp.feature.sheet.list.SheetListScreen

interface ScreenFactory {
    @Composable
    fun SheetList(
        onSheetClick: (Long) -> Unit,
        onCreateSheetClick: () -> Unit,
    )

    @Composable
    fun SheetEdit(
        sheetId: Long?,
        onSaveClick: () -> Unit,
        onBackClick: () -> Unit,
    )

    @Composable
    fun SheetDetail(
        sheetId: Long,
        onBackClick: () -> Unit,
        onEditClick: () -> Unit,
        onDeleteClick: () -> Unit,
        onRestartWithEditClick: () -> Unit,
    )

    @Composable
    fun ExchangeableRewardList()

    @Composable
    fun CompletedSheetList(onRewardClick: (Long) -> Unit)

    @Composable
    fun CompletedSheetDetail(completedSheetId: Long)

    @Composable
    fun Settings()
}

@ExcludeFromCoverage
object RewardStampScreenFactory : ScreenFactory {
    @Composable
    override fun SheetList(
        onSheetClick: (Long) -> Unit,
        onCreateSheetClick: () -> Unit,
    ) {
        SheetListScreen(
            onSheetClick = onSheetClick,
            onCreateSheetClick = onCreateSheetClick,
        )
    }

    @Composable
    override fun SheetEdit(
        sheetId: Long?,
        onSaveClick: () -> Unit,
        onBackClick: () -> Unit,
    ) {
        SheetEditScreen(
            sheetId = sheetId,
            onSaveClick = onSaveClick,
            onBackClick = onBackClick,
        )
    }

    @Composable
    override fun SheetDetail(
        sheetId: Long,
        onBackClick: () -> Unit,
        onEditClick: () -> Unit,
        onDeleteClick: () -> Unit,
        onRestartWithEditClick: () -> Unit,
    ) {
        SheetDetailScreen(
            sheetId = sheetId,
            onBackClick = onBackClick,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            onRestartWithEditClick = onRestartWithEditClick,
        )
    }

    @Composable
    override fun ExchangeableRewardList() {
        ExchangeableSheetListScreen()
    }

    @Composable
    override fun CompletedSheetList(onRewardClick: (Long) -> Unit) {
        CompletedSheetListScreen(
            onRewardClick = onRewardClick,
        )
    }

    @Composable
    override fun CompletedSheetDetail(completedSheetId: Long) {
        CompletedSheetDetailScreen(
            completedSheetId = completedSheetId,
        )
    }

    @Composable
    override fun Settings() {
        SettingsScreen()
    }
}
