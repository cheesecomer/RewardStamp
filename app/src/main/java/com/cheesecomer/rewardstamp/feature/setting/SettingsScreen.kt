package com.cheesecomer.rewardstamp.feature.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheesecomer.rewardstamp.BuildConfig
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget
import com.cheesecomer.rewardstamp.ui.component.RewardStampScaffold
import com.cheesecomer.rewardstamp.ui.component.SectionTitle
import com.cheesecomer.rewardstamp.ui.component.lock.AdultLockDialog
import com.cheesecomer.rewardstamp.ui.component.lock.AdultLockState
import com.cheesecomer.rewardstamp.ui.component.lock.rememberAdultLockState
import com.cheesecomer.rewardstamp.ui.component.lock.rememberAppAdultLockState
import com.cheesecomer.rewardstamp.ui.rewardStampViewModel
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.cheesecomer.rewardstamp.ui.theme.SheetBorder

private val RowMinHeight = 56.dp
private const val SUB_SETTINGS_ALPHA = 0.5f

@Composable
private fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, SheetBorder),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionTitle(text = title)
            content()
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = RowMinHeight)
                .toggleable(
                    value = checked,
                    role = Role.Switch,
                    onValueChange = onCheckedChange,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
            )
            if (supportingText != null) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = null,
        )
    }
}

@Composable
private fun SettingsLinkRow(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Filled.OpenInNew,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = RowMinHeight)
                .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun SettingsValueRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = RowMinHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun AdultLockSection(
    settings: AppSettings,
    onAdultLockChange: (Boolean) -> Unit,
    onStampLockChange: (Boolean) -> Unit,
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
                SettingsSwitchRow(
                    label = stringResource(R.string.settings_stamp_lock),
                    checked = settings.stampLockEnabled,
                    onCheckedChange = onStampLockChange,
                    modifier =
                        Modifier
                            .padding(horizontal = 12.dp)
                            .testTag(SettingsTestTags.STAMP_LOCK_SWITCH),
                )
            }
        }
    }
}

@Composable
private fun EffectsSection(
    settings: AppSettings,
    onVibrationChange: (Boolean) -> Unit,
    onGoalCelebrationChange: (Boolean) -> Unit,
) {
    SettingsSection(title = stringResource(R.string.settings_effects_title)) {
        SettingsSwitchRow(
            label = stringResource(R.string.settings_vibration),
            checked = settings.vibrationEnabled,
            onCheckedChange = onVibrationChange,
            modifier = Modifier.testTag(SettingsTestTags.VIBRATION_SWITCH),
        )
        HorizontalDivider()
        SettingsSwitchRow(
            label = stringResource(R.string.settings_goal_celebration),
            checked = settings.goalCelebrationEnabled,
            onCheckedChange = onGoalCelebrationChange,
            modifier = Modifier.testTag(SettingsTestTags.GOAL_CELEBRATION_SWITCH),
        )
    }
}

@Composable
private fun AboutSection(
    versionName: String,
    onLicensesClick: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    // Nothing to do when no app can open the link
    val openUri = { uri: String -> runCatching { uriHandler.openUri(uri) } }

    SettingsSection(title = stringResource(R.string.settings_about_title)) {
        SettingsValueRow(
            label = stringResource(R.string.settings_version),
            value = versionName,
            modifier = Modifier.testTag(SettingsTestTags.VERSION),
        )
        HorizontalDivider()
        SettingsLinkRow(
            label = stringResource(R.string.settings_privacy_policy),
            onClick = { openUri(SettingsLinks.PRIVACY_POLICY) },
            modifier = Modifier.testTag(SettingsTestTags.PRIVACY_POLICY),
        )
        HorizontalDivider()
        SettingsLinkRow(
            label = stringResource(R.string.settings_contact),
            onClick = { openUri(SettingsLinks.CONTACT) },
            modifier = Modifier.testTag(SettingsTestTags.CONTACT),
        )
        HorizontalDivider()
        SettingsLinkRow(
            label = stringResource(R.string.settings_rate_app),
            onClick = { openUri(SettingsLinks.RATE_APP) },
            modifier = Modifier.testTag(SettingsTestTags.RATE_APP),
        )
        HorizontalDivider()
        SettingsLinkRow(
            label = stringResource(R.string.settings_licenses),
            onClick = onLicensesClick,
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            modifier = Modifier.testTag(SettingsTestTags.LICENSES),
        )
    }
}

@Composable
internal fun SettingsContent(
    settings: AppSettings,
    versionName: String,
    modifier: Modifier = Modifier,
    onVibrationChange: (Boolean) -> Unit = {},
    onGoalCelebrationChange: (Boolean) -> Unit = {},
    onAdultLockChange: (Boolean) -> Unit = {},
    onStampLockChange: (Boolean) -> Unit = {},
    onLicensesClick: () -> Unit = {},
    adultLock: AdultLockState = rememberAdultLockState(settings),
) {
    AdultLockDialog(adultLock)
    // While the lock is on, the lock settings change only after unlocking, which lasts until the screen is left
    val changeLockSetting = { change: () -> Unit ->
        adultLock.runUnlocked(LockTarget.ParentAction, keepsUnlocked = true, action = change)
    }
    RewardStampScaffold(
        title = stringResource(R.string.settings_title),
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AdultLockSection(
                settings = settings,
                onAdultLockChange = { enabled -> changeLockSetting { onAdultLockChange(enabled) } },
                onStampLockChange = { enabled -> changeLockSetting { onStampLockChange(enabled) } },
            )
            EffectsSection(
                settings = settings,
                onVibrationChange = onVibrationChange,
                onGoalCelebrationChange = onGoalCelebrationChange,
            )
            AboutSection(
                versionName = versionName,
                onLicensesClick = onLicensesClick,
            )
        }
    }
}

@ExcludeFromCoverage
@Composable
fun SettingsScreen(
    onLicensesClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel =
        rewardStampViewModel {
            SettingsViewModel(settingsRepository)
        },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsContent(
        settings = uiState.settings,
        versionName = BuildConfig.VERSION_NAME,
        modifier = modifier,
        onVibrationChange = viewModel::setVibrationEnabled,
        onGoalCelebrationChange = viewModel::setGoalCelebrationEnabled,
        onAdultLockChange = viewModel::setAdultLockEnabled,
        onStampLockChange = viewModel::setStampLockEnabled,
        onLicensesClick = onLicensesClick,
        adultLock = rememberAppAdultLockState(),
    )
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Composable
private fun SettingsContentPreview() {
    RewardStampTheme {
        SettingsContent(
            settings = AppSettings(),
            versionName = "1.0.0",
        )
    }
}
