package com.cheesecomer.rewardseal.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsHeader(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text("せってい")
        },
        modifier = modifier,
    )
}

@Composable
fun SettingsContent(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { SettingsHeader() },
    ) {
        Column(modifier = Modifier.padding(it).padding(16.dp)) {
            Text("なにもないよ")
        }
    }
}

@ExcludeFromCoverage
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    SettingsContent(modifier = modifier)
}
