package com.hyosik.android.movie.presentation.mypage.state

import com.google.firebase.auth.FirebaseUser
import com.hyosik.android.movie.data.model.MovieDTO
import com.hyosik.android.movie.presentation.home.state.MovieState

sealed class MypageState {

    object UnInitialized : MypageState()

    object Loading : MypageState()

    data class Success(
        val user : FirebaseUser
    ) : MypageState()

    data class Error(
        val errorMessage : String
    ) : MypageState()

}
