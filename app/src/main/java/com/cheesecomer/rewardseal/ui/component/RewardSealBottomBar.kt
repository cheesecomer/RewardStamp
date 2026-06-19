package com.cheesecomer.rewardseal.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.StickyNote2
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.navigation.BottomTab
import com.cheesecomer.rewardseal.ui.theme.Nikumaru
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme

@Composable
fun RowScope.RewardSealBottomBarItem(
    selected: Boolean,
    icon: @Composable () -> Unit,
    label: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    var itemHeight by remember { mutableIntStateOf(0) }
    var itemWidth by remember { mutableIntStateOf(0) }
    Box(
        modifier =
            modifier
                .selectable(
                    selected = selected,
                    onClick = onClick,
                    enabled = enabled,
                    role = Role.Tab,
                    interactionSource = interactionSource,
                    indication = null,
                ).weight(1f)
                .onSizeChanged {
                    itemHeight = it.height
                    itemWidth = it.width
                },
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            with(LocalDensity.current) {
                Box(
                    modifier =
                        Modifier
                            .size(min(itemHeight.toDp(), itemWidth.toDp()) - 12.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            icon()

            Spacer(Modifier.height(4.dp))

            label()
        }
    }
}

@Composable
fun RewardSealBottomBar(
    selectedTab: BottomTab,
    modifier: Modifier = Modifier,
    onTabClick: (BottomTab) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        NavigationBar(modifier = Modifier) {
            SheetNavigationBarItem(
                selected = selectedTab == BottomTab.Sheets,
                onClick = { onTabClick(BottomTab.Sheets) },
            )

            RewardNavigationBarItem(
                selected = selectedTab == BottomTab.Rewards,
                onClick = { onTabClick(BottomTab.Rewards) },
            )

            HistoryNavigationBarItem(
                selected = selectedTab == BottomTab.History,
                onClick = { onTabClick(BottomTab.History) },
            )

            SettingsNavigationBarItem(
                selected = selectedTab == BottomTab.Settings,
                onClick = { onTabClick(BottomTab.Settings) },
            )
        }
    }
}

@Composable
private fun RowScope.SheetNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
) {
    RewardSealBottomBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.StickyNote2,
                contentDescription = null,
            )
        },
        label = {
            Text(
                text = "シート",
                fontFamily = Nikumaru,
            )
        },
    )
}

@Composable
private fun RowScope.RewardNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
) {
    RewardSealBottomBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Outlined.CardGiftcard,
                contentDescription = "ごほうび",
            )
        },
        label = {
            Text(
                text = "ごほうび",
                fontFamily = Nikumaru,
            )
        },
    )
}

@Composable
private fun RowScope.HistoryNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
) {
    RewardSealBottomBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = "れきし",
            )
        },
        label = {
            Text(
                text = "れきし",
                fontFamily = Nikumaru,
            )
        },
    )
}

@Composable
private fun RowScope.SettingsNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
) {
    RewardSealBottomBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "せってい",
            )
        },
        label = {
            Text(
                text = "せってい",
                fontFamily = Nikumaru,
            )
        },
    )
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Composable
private fun RewardSealBottomBarPreview() {
    RewardSealTheme {
        RewardSealBottomBar(
            selectedTab = BottomTab.Sheets,
        )
    }
}
