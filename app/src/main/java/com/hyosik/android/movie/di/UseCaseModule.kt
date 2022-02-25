package com.hyosik.android.movie.di

import com.hyosik.android.movie.data.repository.MovieRepository
import com.hyosik.android.movie.domain.GetMovieDtoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideGetMovieDtoUseCase(
        movieRepository: MovieRepository
    ) : GetMovieDtoUseCase {
        return GetMovieDtoUseCase(movieRepository = movieRepository)
    }

}