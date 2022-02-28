package com.hyosik.android.movie.data.remote

import com.hyosik.android.movie.data.model.MovieDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    //https://openapi.naver.com
    @GET("/v1/search/movie.json")
    suspend fun getMovieList(
        @Header("X-Naver-Client-Id") client_id : String,
        @Header("X-Naver-Client-Secret") client_secret : String,
        @Query("query") query : String,
        @Query("display") display : Int
    ) : Response<MovieDTO>

}