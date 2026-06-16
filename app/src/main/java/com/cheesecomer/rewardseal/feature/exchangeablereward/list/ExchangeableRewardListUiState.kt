package com.cheesecomer.rewardseal.feature.exchangeablereward.list

import com.cheesecomer.rewardseal.model.ExchangeableSheet

data class ExchangeableRewardListUiState(
    val sheets: List<ExchangeableSheet> = emptyList(),
)
