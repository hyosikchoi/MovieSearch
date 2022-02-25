package com.hyosik.android.movie.presentation.home.state

import com.hyosik.android.movie.data.model.MovieDTO

sealed class MovieState {

    object UnInitialized : MovieState()

    object Loading : MovieState()

    data class Success(
        val movieDTO: MovieDTO
    ) : MovieState()

    object Error : MovieState()

}
