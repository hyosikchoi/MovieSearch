package com.hyosik.android.movie.domain

import com.hyosik.android.movie.data.model.Movie
import com.hyosik.android.movie.data.repository.UserRepository
import javax.inject.Inject

class InsertSeeMovieUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase {

    suspend operator fun invoke(movie: Movie) {
        userRepository.insertSeeMovie(movie = movie)
    }
}