package com.hyosik.android.movie.presentation.home

import android.util.Log
import androidx.lifecycle.*
import com.hyosik.android.movie.domain.GetMovieDtoUseCase
import com.hyosik.android.movie.presentation.home.state.MovieState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovieDtoUseCase: GetMovieDtoUseCase
) : ViewModel() {

    private var _movieListStateLiveData = MutableLiveData<MovieState>(MovieState.UnInitialized)

    val movieListStateLiveData : LiveData<MovieState> = _movieListStateLiveData


    fun searchMovie(query : String , display : Int)  = viewModelScope.launch {
        try {
            _movieListStateLiveData.postValue(MovieState.Loading)
            getMovieDtoUseCase(query = query , display = display).also {
                _movieListStateLiveData.postValue(MovieState.Success(it))
            }
        } catch (e: Exception) {
            _movieListStateLiveData.postValue(MovieState.Error)
        }
    }

    private fun setState(state : MovieState) {
        _movieListStateLiveData.postValue(state)
    }

}