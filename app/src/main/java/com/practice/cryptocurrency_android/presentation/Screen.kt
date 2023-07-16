package com.practice.cryptocurrency_android.presentation

sealed class Screen(
    val route: String
) {
    object CoinListScreen: Screen("coin_list_screen")
    object CoinDetailScreen: Screen("coin_detail_screen")
}

// to deal with the route