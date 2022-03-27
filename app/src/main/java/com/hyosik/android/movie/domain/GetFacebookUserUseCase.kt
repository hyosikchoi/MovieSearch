package com.hyosik.android.movie.domain

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.hyosik.android.movie.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFacebookUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase {

    suspend operator fun invoke(authCredential: AuthCredential) : Flow<FirebaseUser> {
        return userRepository.signInFaceBook(authCredential = authCredential)
    }

}
