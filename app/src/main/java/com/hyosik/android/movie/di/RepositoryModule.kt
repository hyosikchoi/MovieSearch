package com.hyosik.android.movie.di

import com.hyosik.android.movie.data.remote.ApiService
import com.hyosik.android.movie.data.repository.DefaultMovieRepository
import com.hyosik.android.movie.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        apiService: ApiService
    ) : MovieRepository {
        return DefaultMovieRepository(apiService = apiService)
    }

}