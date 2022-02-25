package com.hyosik.android.movie.domain

import com.hyosik.android.movie.data.model.MovieDTO
import com.hyosik.android.movie.data.repository.MovieRepository
import javax.inject.Inject

class GetMovieDtoUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : UseCase {

    suspend operator fun invoke(query : String , display : Int) : MovieDTO {
        return movieRepository.getMovieDTO(query = query , display = display)
    }

}