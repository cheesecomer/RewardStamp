package com.cheesecomer.rewardseal.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.StickyNote2
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cheesecomer.rewardseal.navigation.BottomTab
import com.cheesecomer.rewardseal.navigation.Route

@Composable
fun RewardSealBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(modifier = modifier) {
        SheetNavigationBarItem(
            selected = Route.bottomTab(currentRoute) == BottomTab.Sheets,
            onClick = {
                navController.navigateTopLevel(Route.SHEET_LIST)
            },
        )

        RewardNavigationBarItem(
            selected = Route.bottomTab(currentRoute) == BottomTab.Rewards,
            onClick = {
                navController.navigateTopLevel(Route.EXCHANGEABLE_SHEET_LIST)
            },
        )

        HistoryNavigationBarItem(
            selected = Route.bottomTab(currentRoute) == BottomTab.History,
            onClick = {
                navController.navigateTopLevel(Route.COMPLETED_SHEET_LIST)
            },
        )

        SettingsNavigationBarItem(
            selected = Route.bottomTab(currentRoute) == BottomTab.Settings,
            onClick = {
                navController.navigateTopLevel(Route.SETTINGS)
            },
        )
    }
}

@Composable
private fun RowScope.SheetNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.StickyNote2,
                contentDescription = null,
            )
        },
        label = {
            Text("シート")
        },
    )
}

@Composable
private fun RowScope.RewardNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Outlined.CardGiftcard,
                contentDescription = "ごほうび",
            )
        },
        label = { Text("ごほうび") },
    )
}

@Composable
private fun RowScope.HistoryNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = "れきし",
            )
        },
        label = { Text("れきし") },
    )
}

@Composable
private fun RowScope.SettingsNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "せってい",
            )
        },
        label = { Text("せってい") },
    )
}

private fun NavController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(Route.SHEET_LIST) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
