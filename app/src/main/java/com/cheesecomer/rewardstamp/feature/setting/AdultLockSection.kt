package com.cheesecomer.rewardstamp.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget

private val CheckRowMinHeight = 48.dp
private const val SUB_SETTINGS_ALPHA = 0.5f

private val LockTarget.labelRes: Int
    get() =
        when (this) {
            LockTarget.Stamp -> R.string.settings_lock_target_stamp
            LockTarget.RewardExchange -> R.string.settings_lock_target_reward_exchange
            LockTarget.SheetEdit -> R.string.settings_lock_target_sheet_edit
            LockTarget.HistoryDelete -> R.string.settings_lock_target_history_delete
            LockTarget.LockSettings -> error("The lock settings are always locked")
        }

// What a target covers besides its label
private val LockTarget.noteRes: Int?
    get() = if (this == LockTarget.SheetEdit) R.string.settings_lock_target_sheet_edit_note else null

@Composable
private fun LockTargetRow(
    target: LockTarget,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = CheckRowMinHeight)
                .toggleable(
                    value = checked,
                    role = Role.Checkbox,
                    onValueChange = onCheckedChange,
                ).testTag(SettingsTestTags.lockTarget(target)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(
                text = stringResource(target.labelRes),
                style = MaterialTheme.typography.bodyLarge,
            )
            target.noteRes?.let { note ->
                Text(
                    text = stringResource(note),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
internal fun AdultLockSection(
    settings: AppSettings,
    onAdultLockChange: (Boolean) -> Unit,
    onLockTargetChange: (LockTarget, Boolean) -> Unit,
) {
    SettingsSection(title = stringResource(R.string.settings_adult_lock_title)) {
        SettingsSwitchRow(
            label = stringResource(R.string.settings_adult_lock_enabled),
            supportingText = stringResource(R.string.settings_adult_lock_description),
            checked = settings.adultLockEnabled,
            onCheckedChange = onAdultLockChange,
            modifier = Modifier.testTag(SettingsTestTags.ADULT_LOCK_SWITCH),
        )
        if (settings.adultLockEnabled) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = SUB_SETTINGS_ALPHA),
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(
                        text = stringResource(R.string.settings_lock_targets_title),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    LockTarget.selectable.forEach { target ->
                        LockTargetRow(
                            target = target,
                            checked = target in settings.lockTargets,
                            onCheckedChange = { locked -> onLockTargetChange(target, locked) },
                        )
                    }
                }
            }
        }
    }
}
