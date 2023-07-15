package com.practice.cryptocurrency_android.common

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class Loading<T>(data: T? = null): Resource<T>(data)

    // 프로그레스 바를 보여주며, 성공, 실패, 로딩 상태 중 하나일 때
}
