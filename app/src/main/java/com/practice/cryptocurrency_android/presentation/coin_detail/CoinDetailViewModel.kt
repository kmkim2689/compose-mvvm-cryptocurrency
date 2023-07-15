package com.practice.cryptocurrency_android.presentation.coin_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.cryptocurrency_android.common.Constants
import com.practice.cryptocurrency_android.common.Resource
import com.practice.cryptocurrency_android.domain.use_case.get_coin.GetCoinUseCase
import com.practice.cryptocurrency_android.domain.use_case.get_coins.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    // no repository anymore... all in the use case
    private val getCoinUseCase: GetCoinUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = mutableStateOf(CoinDetailState())
    val state: State<CoinDetailState> = _state

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let { coinId ->
            getCoinDetail(coinId)
        }
    }

    private fun getCoinDetail(coinId: String) {
        // operate invoke function -> use class as a function
        // call the use case class as if it were a function
        // returns the flow that emits Resource over time
        getCoinUseCase(coinId).onEach { result ->
            // 별 일이 없으면, 첫 번째로는 Resource.Loading이 발행되고, 두 번째로는 Resource.success가 발행.
            // 발행 중 문제가 생기면 Resource.Error 발행
            when (result) {
                is Resource.Success -> {
                    // 두 번째로 방출되는 것 처리
                    _state.value = CoinDetailState(
                        coin = result.data
                    )
                }
                is Resource.Error -> {
                    // 오류 발생 시 처라
                    _state.value = CoinDetailState(
                        error = result.message ?: "unexpected error"
                    )
                }
                is Resource.Loading -> {
                    // 첫 번째 방출되는 것 처리
                    _state.value = CoinDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}