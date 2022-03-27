package com.hyosik.android.movie.di

import com.hyosik.android.movie.data.repository.MovieRepository
import com.hyosik.android.movie.data.repository.UserRepository
import com.hyosik.android.movie.domain.GetCurrentUserUseCase
import com.hyosik.android.movie.domain.GetFacebookUserUseCase
import com.hyosik.android.movie.domain.GetMovieDtoUseCase
import com.hyosik.android.movie.domain.GetSignUpCurrentUserUseCase
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

    @Provides
    fun provideGetCurrentUserUseCase(
        userRepository: UserRepository
    ) : GetCurrentUserUseCase {
        return GetCurrentUserUseCase(userRepository = userRepository)
    }

    @Provides
    fun provideGetSignUpCurrentUserUseCase(
        userRepository: UserRepository
    ) : GetSignUpCurrentUserUseCase {
        return GetSignUpCurrentUserUseCase(userRepository = userRepository)
    }

    @Provides
    fun provideGetFacebookUserUseCase(
        userRepository: UserRepository
    ) : GetFacebookUserUseCase {
        return GetFacebookUserUseCase(userRepository = userRepository)
    }

}