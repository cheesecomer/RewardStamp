package com.cheesecomer.rewardstamp.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.ui.component.RewardStampScaffold

@Composable
fun SettingsContent(modifier: Modifier = Modifier) {
    RewardStampScaffold(
        title = "せってい",
        modifier = modifier,
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
