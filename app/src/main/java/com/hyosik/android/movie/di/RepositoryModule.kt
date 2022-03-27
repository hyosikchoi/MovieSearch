package com.hyosik.android.movie.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.hyosik.android.movie.data.remote.ApiService
import com.hyosik.android.movie.data.repository.DefaultMovieRepository
import com.hyosik.android.movie.data.repository.DefaultUserRepository
import com.hyosik.android.movie.data.repository.MovieRepository
import com.hyosik.android.movie.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import javax.inject.Named
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

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseAuth: FirebaseAuth,
        databaseReference: DatabaseReference
    ) : UserRepository {
        return DefaultUserRepository(firebaseAuth,databaseReference)
    }

}