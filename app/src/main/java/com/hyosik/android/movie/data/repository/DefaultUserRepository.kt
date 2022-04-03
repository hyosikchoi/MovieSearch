package com.hyosik.android.movie.data.repository


import android.util.Log
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.hyosik.android.movie.data.model.Movie
import com.hyosik.android.movie.extensions.replaceBlank
import com.hyosik.android.movie.extensions.replaceMultipleBlank

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
    private val userDB: DatabaseReference
) : UserRepository {

    override suspend fun signUpUser(id: String, pw: String): Flow<FirebaseUser> =
        flow<FirebaseUser> {
            val response = auth.createUserWithEmailAndPassword(id, pw).await()
            response.user?.let { firebaseUser ->
                saveUserDB(firebaseUser)
                emit(firebaseUser)
            }
        }

    override suspend fun signInUser(id: String, pw: String): Flow<FirebaseUser> =
        flow<FirebaseUser> {
            val response = auth.signInWithEmailAndPassword(id, pw).await()
            response.user?.let { firebaseUser ->
                saveUserDB(firebaseUser)
                emit(firebaseUser)
            }
        }

    override suspend fun signInFaceBook(authCredential: AuthCredential): Flow<FirebaseUser> =
        flow<FirebaseUser> {
           val response =  auth.signInWithCredential(authCredential).await()
            response.user?.let { firebaseUser ->
                saveUserDB(firebaseUser)
                emit(firebaseUser)
            }
        }

    override suspend fun insertSeeMovie(movie: Movie) {
        val userId = auth.currentUser?.uid.orEmpty()
        val insertUserDB = userDB.child(userId).child("see").child("${movie.title.replaceBlank(".").replaceMultipleBlank("<b>","</b>")}${movie.pubDate.replaceBlank(".")}${movie.userRating.replaceBlank(".")}")
        insertUserDB.setValue(movie)
    }

    override suspend fun deleteSeeMovie(movie: Movie) {
        val userId = auth.currentUser?.uid.orEmpty()
        val deleteUserDB = userDB.child(userId).child("see").child("${movie.title.replaceBlank(".").replaceMultipleBlank("<b>","</b>")}${movie.pubDate.replaceBlank(".")}${movie.userRating.replaceBlank(".")}")
        deleteUserDB.removeValue()
    }

    private fun saveUserDB(firebaseUser: FirebaseUser) {
        val userId = firebaseUser.uid.orEmpty()
        val userEmail = firebaseUser.email.orEmpty()

        val updateUserDB = userDB.child(userId)
        val user = mutableMapOf<String,  Any>()
        user["userId"] = userId
        user["userEmail"] = userEmail
        auth.currentUser?.providerData?.forEach { userInfo ->
            user["userSnsType"] = userInfo.providerId
        }
        updateUserDB.updateChildren(user)
    }

}