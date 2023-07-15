package com.practice.cryptocurrency_android.di

import com.practice.cryptocurrency_android.common.Constants
import com.practice.cryptocurrency_android.data.remote.CoinPaprikaApi
import com.practice.cryptocurrency_android.data.repository.CoinRepositoryImpl
import com.practice.cryptocurrency_android.domain.repository.CoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // all the dependencies in the module(@Module) live as long as the application
object AppModule {

    // the function provides a dependency
    @Provides
    // only have single instance of whatever the function returns
    // single instance throughout the whole lifetime of the app
    @Singleton
    fun providePaprikaApi(): CoinPaprikaApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinPaprikaApi::class.java)
    }

    // provide injection to repository
    @Provides
    @Singleton
    fun provideCoinRepository(api: CoinPaprikaApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }
}