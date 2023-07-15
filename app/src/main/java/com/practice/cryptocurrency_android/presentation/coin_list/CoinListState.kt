package com.practice.cryptocurrency_android.presentation.coin_list

import com.practice.cryptocurrency_android.domain.model.Coin

data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val error: String = ""
)
