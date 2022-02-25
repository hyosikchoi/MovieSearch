package com.hyosik.android.movie.data.repository

import com.hyosik.android.movie.data.model.MovieDTO

interface MovieRepository {

    suspend fun getMovieDTO(query : String , display : Int) : MovieDTO

}