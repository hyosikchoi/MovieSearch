package com.hyosik.android.movie.data.repository


import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun signUpUser(id : String , pw : String) : Flow<FirebaseUser>

    suspend fun signInUser(id : String , pw : String) : Flow<FirebaseUser>

    suspend fun signInFaceBook(authCredential: AuthCredential) : Flow<FirebaseUser>
}