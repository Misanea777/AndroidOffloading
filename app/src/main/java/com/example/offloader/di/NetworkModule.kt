package com.example.offloader.di

import com.example.offloader.BuildConfig
import com.example.offloader.di.dispatchers.IoDispatcher
import com.example.offloader.offloader.data.remote.OffloadApi
import com.example.offloader.offloader.data.reoisitory.DefaultOffloadRepository
import com.example.offloader.offloader.data.reoisitory.OffloadRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit
        .Builder()
        .baseUrl("https://immense-brushlands-80363.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .apply { if (BuildConfig.DEBUG) addInterceptor(httpLoggingInterceptor) }
        .build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): OffloadApi =
        retrofit.create(OffloadApi::class.java)

    @Singleton
    @Provides
    fun provideDefaultWeatherRepository(
        api: OffloadApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): OffloadRepository = DefaultOffloadRepository(api, ioDispatcher)
}