package com.cheesecomer.rewardstamp.feature.sheet.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.cheesecomer.rewardstamp.R

@Composable
fun StampActionDialog(
    modifier: Modifier = Modifier,
    onChangeRequest: () -> Unit = {},
    onCancelStampRequest: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier.testTag(StampActionDialogTestTags.ROOT),
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(R.string.stamp_action_title))
        },
        text = {
            Text(stringResource(R.string.stamp_action_message))
        },
        confirmButton = {
            Row {
                TextButton(
                    onClick = onCancelStampRequest,
                    modifier = Modifier.testTag(StampActionDialogTestTags.CANCEL_STAMP_BUTTON),
                ) {
                    Text(stringResource(R.string.stamp_action_cancel))
                }
                TextButton(
                    onClick = onChangeRequest,
                    modifier = Modifier.testTag(StampActionDialogTestTags.CHANGE_BUTTON),
                ) {
                    Text(stringResource(R.string.stamp_action_change))
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(stringResource(R.string.common_cancel))
            }
        },
    )
}
