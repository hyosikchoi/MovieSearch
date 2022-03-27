package com.hyosik.android.movie.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.hyosik.android.movie.domain.GetCurrentUserUseCase
import com.hyosik.android.movie.domain.GetFacebookUserUseCase
import com.hyosik.android.movie.domain.GetSignUpCurrentUserUseCase
import com.hyosik.android.movie.presentation.mypage.state.MypageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getSignUpCurrentUserUseCase: GetSignUpCurrentUserUseCase,
    private val getFacebookUserUseCase: GetFacebookUserUseCase
) : ViewModel() {
    private var _currentUserStateFlow = MutableStateFlow<MypageState>(MypageState.UnInitialized)

    val currentUserStateFlow = _currentUserStateFlow

    fun signUpEmail(id : String , pw : String) = viewModelScope.launch {
        getSignUpCurrentUserUseCase(id = id , pw = pw)
            .onStart { _currentUserStateFlow.value = MypageState.Loading }
            .catch { cause: Throwable ->
                _currentUserStateFlow.value = MypageState.Error(cause.message.toString()) }
            .collect { _currentUserStateFlow.value = MypageState.Success(it) }
    }

    fun signInEmail(id : String , pw : String) = viewModelScope.launch {
        getCurrentUserUseCase(id = id , pw = pw)
            .onStart { _currentUserStateFlow.value = MypageState.Loading }
            .catch {cause: Throwable ->
                _currentUserStateFlow.value = MypageState.Error(cause.message.toString())}
            .collect { _currentUserStateFlow.value = MypageState.Success(it)}
    }

    fun signInFacebook(authCredential: AuthCredential) = viewModelScope.launch {
        getFacebookUserUseCase(authCredential = authCredential)
            .onStart { _currentUserStateFlow.value = MypageState.Loading }
            .catch { cause: Throwable ->
                _currentUserStateFlow.value = MypageState.Error(cause.message.toString())}
            .collect { _currentUserStateFlow.value = MypageState.Success(it)}
    }

}