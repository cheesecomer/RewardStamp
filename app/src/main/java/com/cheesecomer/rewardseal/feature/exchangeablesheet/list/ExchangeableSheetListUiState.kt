package com.cheesecomer.rewardseal.feature.exchangeablesheet.list

import com.cheesecomer.rewardseal.model.ExchangeableSheet

data class ExchangeableSheetListUiState(
    val sheets: List<ExchangeableSheet> = emptyList(),
)
