package com.practice.cryptocurrency_android.domain.repository

import com.practice.cryptocurrency_android.data.dto.CoinDetailDto
import com.practice.cryptocurrency_android.data.dto.CoinDto

interface CoinRepository {

    suspend fun getCoins(): List<CoinDto>

    suspend fun getCoinById(coinId: String): CoinDetailDto
}