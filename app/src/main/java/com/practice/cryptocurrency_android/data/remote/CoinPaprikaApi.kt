package com.practice.cryptocurrency_android.data.remote

import com.practice.cryptocurrency_android.data.dto.CoinDetailDto
import com.practice.cryptocurrency_android.data.dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinPaprikaApi {

    // to get all coins
    // return a list of coins -> object for return needed
    // the direct data object from network call -> in dto package(readme.md)
    @GET("/v1/coins")
    suspend fun getCoins(): List<CoinDto>

    // to get single coin detail data
    @GET("v1/coins/{coinId}")
    suspend fun getCoinById(
        @Path("coinId") coinId: String
    ): CoinDetailDto
}