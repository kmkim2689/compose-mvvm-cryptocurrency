package com.practice.cryptocurrency_android.presentation.coin_detail

import com.practice.cryptocurrency_android.domain.model.Coin
import com.practice.cryptocurrency_android.domain.model.CoinDetail

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val error: String = ""
)
