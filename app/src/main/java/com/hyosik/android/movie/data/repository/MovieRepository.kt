package com.hyosik.android.movie.data.repository

import com.hyosik.android.movie.data.model.MovieDTO
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovieDTO(query : String , display : Int) : Flow<MovieDTO>
}