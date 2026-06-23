package com.cheesecomer.rewardstamp.feature.exchangeablesheet.list

import com.cheesecomer.rewardstamp.model.ExchangeableSheet

data class ExchangeableSheetListUiState(
    val sheets: List<ExchangeableSheet> = emptyList(),
)
