package com.cheesecomer.rewardstamp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardstamp.RewardStampApplication
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage

@ExcludeFromCoverage
@Composable
inline fun <reified VM : ViewModel> rewardStampViewModel(crossinline create: RewardStampApplication.() -> VM): VM =
    viewModel {
        (checkNotNull(this[APPLICATION_KEY]) as RewardStampApplication).create()
    }
