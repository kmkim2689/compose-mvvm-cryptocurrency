package com.practice.cryptocurrency_android.data.repository

import com.practice.cryptocurrency_android.data.dto.CoinDetailDto
import com.practice.cryptocurrency_android.data.dto.CoinDto
import com.practice.cryptocurrency_android.data.remote.CoinPaprikaApi
import com.practice.cryptocurrency_android.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinPaprikaApi
): CoinRepository {
    override suspend fun getCoins(): List<CoinDto> {
        return api.getCoins()
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }
}