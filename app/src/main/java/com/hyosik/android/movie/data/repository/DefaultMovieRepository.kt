package com.hyosik.android.movie.data.repository

import android.util.Log
import com.hyosik.android.movie.BuildConfig
import com.hyosik.android.movie.data.model.MovieDTO
import com.hyosik.android.movie.data.remote.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class DefaultMovieRepository @Inject constructor(
    private val apiService: ApiService,
) : MovieRepository {

    override suspend fun getMovieDTO(query: String, display: Int) : MovieDTO  {
        val response = apiService.getMovieList(BuildConfig.CLIENT_ID , BuildConfig.CLIENT_SECRET , query = query , display = display)
        if(response.isSuccessful) {
            response.body()?.let { return it }
        } else {
            return MovieDTO()
        }
        return MovieDTO()
    }
}