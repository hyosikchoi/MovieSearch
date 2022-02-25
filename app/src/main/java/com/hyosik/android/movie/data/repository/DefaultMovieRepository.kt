package com.hyosik.android.movie.data.repository

import com.hyosik.android.movie.BuildConfig
import com.hyosik.android.movie.data.model.MovieDTO
import com.hyosik.android.movie.data.remote.ApiService
import javax.inject.Inject

class DefaultMovieRepository @Inject constructor(
    private val apiService: ApiService
) : MovieRepository {

    override suspend fun getMovieDTO(query: String, display: Int): MovieDTO {
        val response = apiService.getMovieList(BuildConfig.CLIENT_ID , BuildConfig.CLIENT_SECRET , query = query , display = display)
        if(response.isSuccessful) {
            response.body()?.let { return it }
        }
        return MovieDTO()
    }
}