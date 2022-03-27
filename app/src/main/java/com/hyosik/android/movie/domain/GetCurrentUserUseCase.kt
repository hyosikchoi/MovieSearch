package com.hyosik.android.movie.domain

import com.google.firebase.auth.FirebaseUser
import com.hyosik.android.movie.data.model.MovieDTO
import com.hyosik.android.movie.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase {

    suspend operator fun invoke(id : String , pw : String) : Flow<FirebaseUser> {
        return userRepository.signInUser(id = id , pw =  pw)
    }

}