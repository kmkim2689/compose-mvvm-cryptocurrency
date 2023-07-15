package com.practice.cryptocurrency_android.domain.use_case.get_coins

import com.practice.cryptocurrency_android.common.Resource
import com.practice.cryptocurrency_android.data.dto.toCoin
import com.practice.cryptocurrency_android.domain.model.Coin
import com.practice.cryptocurrency_android.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    // only to get info for all coins, not coin details
    // operator function : call the use case class as if it was a function
    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            // emit loading status(for progress bar)
            emit(Resource.Loading())
            val coins = repository.getCoins().map { it.toCoin() }
            emit(Resource.Success(coins))
        } catch (e: HttpException) {
            // network call  not successful
            emit(Resource.Error(e.localizedMessage ?: "unexpected error"))
        } catch (e: IOException) {
            // repository / api cannot require the actual remote api
            emit(Resource.Error("couldn't reach server, check internet connection"))
        }
    }
    // use flow, to ***emit ***multiple*** values over a period of time
    // Resource : 성공, 실패, 로딩 => 경우에 따라 Flow가 emit할 데이터를 다르게 할 수 있음
    // Flow는 Resource가 Successful일 때 데이터(list of coins - Resource 클래스 참고)를 emit하도록 하고,
    // Error일 때 데이터를 에러를 emit
    // Loading일 때 진전도를 emit...


}