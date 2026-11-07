package com.cheesecomer.rewardstamp.feature.license

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.ui.component.RewardStampScaffold
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimary
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimaryContainer
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.style.m3VariantColors
import com.mikepenz.aboutlibraries.ui.compose.style.LibraryActionBadges
import com.mikepenz.aboutlibraries.ui.compose.style.LicenseHueResolver
import com.mikepenz.aboutlibraries.ui.compose.variant.LibraryActionKind

// [libraries] is null while loading. The overload that takes the dialog state is experimental, as it has a bottom sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LicenseListContent(
    libraries: Libs?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var licenseLibrary by remember { mutableStateOf<Library?>(null) }

    RewardStampScaffold(
        title = stringResource(R.string.licenses_title),
        modifier = modifier,
        onBackClick = onBackClick,
    ) { innerPadding ->
        LibrariesContainer(
            libraries = libraries,
            dialogLibrary = licenseLibrary,
            sheetLibrary = null,
            onDialogLibraryChange = { licenseLibrary = it },
            onSheetLibraryChange = {},
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag(LicenseListTestTags.LIST),
            // No sponsor links, as children use this app
            actionLabels =
                LibraryActionBadges(
                    sponsorEnabled = false,
                    source = stringResource(R.string.licenses_source),
                    website = stringResource(R.string.licenses_website),
                    viewLicense = stringResource(R.string.licenses_view_license),
                ),
            // One color for every license, instead of a color for each kind
            variantColors =
                LibraryDefaults.m3VariantColors(
                    licenseHueResolver = LicenseHueResolver.None,
                    licenseBadgeContainer = SheetPrimaryContainer,
                    licenseBadgeContent = SheetPrimary,
                ),
            // Shows the license text in the app, instead of opening its page in the browser
            onActionClick = { library, kind ->
                val showsText = kind == LibraryActionKind.License && library.licenses.isNotEmpty()
                if (showsText) licenseLibrary = library
                showsText
            },
            licenseDialogConfirmText = stringResource(R.string.licenses_close),
        )
    }
}

// Lists the licenses of the libraries and assets collected at build time (app/config/aboutlibraries for the assets)
@ExcludeFromCoverage
@Composable
fun LicenseListScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val libraries by produceLibraries(R.raw.aboutlibraries)

    LicenseListContent(
        libraries = libraries,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}
