package com.hyosik.android.movie.data.repository


import android.util.Log
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.hyosik.android.movie.extensions.toastShort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.suspendCoroutine

class DefaultUserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val userDb: DatabaseReference
) : UserRepository {

    override suspend fun signUpUser(id: String, pw: String): Flow<FirebaseUser> =
        flow<FirebaseUser> {
            val response = auth.createUserWithEmailAndPassword(id, pw).await()
            //TODO userDB 에 업데이트
            emit(response.user!!)
        }

    override suspend fun signInUser(id: String, pw: String): Flow<FirebaseUser> =
        flow<FirebaseUser> {
            val response = auth.signInWithEmailAndPassword(id, pw).await()
            emit(response.user!!)
        }

    override suspend fun signInFaceBook(authCredential: AuthCredential): Flow<FirebaseUser> =
        flow<FirebaseUser> {
           val response =  auth.signInWithCredential(authCredential).await()
            emit(response.user!!)
        }


}