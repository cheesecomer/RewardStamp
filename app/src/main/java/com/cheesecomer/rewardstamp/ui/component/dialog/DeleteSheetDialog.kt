package com.cheesecomer.rewardstamp.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.cheesecomer.rewardstamp.R

@Composable
fun DeleteSheetDialog(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.delete_sheet_title),
    message: String = stringResource(R.string.delete_sheet_message),
    onDeleteRequest: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier.testTag(DeleteSheetDialogTestTags.ROOT),
        onDismissRequest = onDismissRequest,
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            TextButton(
                onClick = onDeleteRequest,
                modifier = Modifier.testTag(DeleteSheetDialogTestTags.DELETE_BUTTON),
            ) {
                Text(stringResource(R.string.common_delete))
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
