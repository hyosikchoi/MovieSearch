package com.hyosik.android.movie.di

import com.hyosik.android.movie.data.repository.MovieRepository
import com.hyosik.android.movie.data.repository.UserRepository
import com.hyosik.android.movie.domain.*
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

    @Provides
    fun provideInsertSeeMovieUseCase(
        userRepository: UserRepository
    ) : InsertSeeMovieUseCase {
        return InsertSeeMovieUseCase(userRepository = userRepository)
    }

    @Provides
    fun provideDeleteSeeMovieUseCase(
        userRepository: UserRepository
    ) : DeleteSeeMovieUseCase {
        return DeleteSeeMovieUseCase(userRepository = userRepository)
    }

}