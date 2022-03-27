package com.hyosik.android.movie.di

import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hyosik.android.movie.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth
    = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideCallbackManager() : CallbackManager
    = CallbackManager.Factory.create()

    @Provides
    @Singleton
    fun provideFirebaseDatabase() : DatabaseReference
    = Firebase.database.reference.child(BuildConfig.USER_DB_KEY)

}