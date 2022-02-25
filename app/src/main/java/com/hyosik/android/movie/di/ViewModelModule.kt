package com.hyosik.android.movie.di

import com.hyosik.android.movie.domain.GetMovieDtoUseCase
import com.hyosik.android.movie.presentation.home.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideHomeViewModel(
        getMovieDtoUseCase: GetMovieDtoUseCase
    ) : HomeViewModel {
        return HomeViewModel(
            getMovieDtoUseCase = getMovieDtoUseCase
        )
    }

}