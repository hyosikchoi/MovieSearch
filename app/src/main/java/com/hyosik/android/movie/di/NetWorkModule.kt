package com.hyosik.android.movie.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.hyosik.android.movie.BuildConfig
import com.hyosik.android.movie.data.remote.ApiService
import com.hyosik.android.movie.data.repository.DefaultMovieRepository
import com.hyosik.android.movie.data.repository.MovieRepository
import com.hyosik.android.movie.domain.GetMovieDtoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetWorkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
       okHttpClient: OkHttpClient,
       gsonConverterFactory: GsonConverterFactory
    ) : Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory() : GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun buildOkHttpClient() : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG) interceptor.level = HttpLoggingInterceptor.Level.BODY
        else interceptor.level = HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor = interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Named("IO")
    fun provideIODispatcher() : CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named("Main")
    fun provideMainDispatcher() : CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Named("Default")
    fun provideDefaultDispatcher() : CoroutineDispatcher = Dispatchers.Default

}