package com.hyosik.android.movie.domain

import com.google.firebase.auth.FirebaseUser
import com.hyosik.android.movie.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSignUpCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase {

    operator suspend fun invoke(id : String , pw : String) : Flow<FirebaseUser> {
        return userRepository.signUpUser(id = id , pw = pw)
    }

}