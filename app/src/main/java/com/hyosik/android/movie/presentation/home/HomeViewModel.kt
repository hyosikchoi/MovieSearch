package com.hyosik.android.movie.presentation.home

import android.util.Log
import androidx.lifecycle.*
import com.hyosik.android.movie.data.model.Movie
import com.hyosik.android.movie.domain.GetMovieDtoUseCase
import com.hyosik.android.movie.domain.InsertSeeMovieUseCase
import com.hyosik.android.movie.presentation.home.state.MovieState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovieDtoUseCase: GetMovieDtoUseCase,
    private val insertSeeMovieUseCase: InsertSeeMovieUseCase
) : ViewModel() {

    private var _movieListStateFlow = MutableStateFlow<MovieState>(MovieState.UnInitialized)

    val movieListStateFlow : StateFlow<MovieState> = _movieListStateFlow


    fun searchMovie(query : String , display : Int)  = viewModelScope.launch {

        getMovieDtoUseCase(query = query , display = display)
            .onStart {
                _movieListStateFlow.value = MovieState.Loading
            }
            .catch { exception ->
                _movieListStateFlow.value = MovieState.Error
            }
            .collect { movieDTO ->
                _movieListStateFlow.value = MovieState.Success(movieDTO)
            }
    }

    fun saveSeeMovie(movie: Movie) = viewModelScope.launch {
        insertSeeMovieUseCase(movie = movie)
    }

}