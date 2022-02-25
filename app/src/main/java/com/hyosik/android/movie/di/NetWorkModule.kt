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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
        return GsonConverterFactory.create(
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        )
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


}