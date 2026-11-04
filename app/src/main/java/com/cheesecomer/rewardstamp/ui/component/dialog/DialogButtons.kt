package com.cheesecomer.rewardstamp.ui.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.R

@Composable
fun DialogButtons(
    confirmText: String,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    dismissText: String = stringResource(R.string.common_cancel),
    onConfirmClick: () -> Unit = {},
    onDismissClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TextButton(
            modifier = Modifier.weight(1f).testTag("DialogButtons.DismissButton"),
            onClick = onDismissClick,
        ) {
            Text(text = dismissText)
        }

        Button(
            modifier = Modifier.weight(1f).testTag("DialogButtons.ConfirmButton"),
            enabled = enable,
            onClick = onConfirmClick,
        ) {
            Text(confirmText)
        }
    }
}
