package com.hyosik.android.movie.presentation.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.hyosik.android.movie.BaseFragment
import com.hyosik.android.movie.data.model.Movie
import com.hyosik.android.movie.databinding.FragmentMypageBinding
import com.hyosik.android.movie.extensions.replaceMultipleBlank
import com.hyosik.android.movie.extensions.toastShort
import com.hyosik.android.movie.presentation.MainActivity
import com.hyosik.android.movie.presentation.detail.MovieDetailFragment
import com.hyosik.android.movie.presentation.home.HomeViewModel
import com.hyosik.android.movie.presentation.mypage.adapter.MySearchMovieListAdapter
import com.hyosik.android.movie.presentation.mypage.state.MypageState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@WithFragmentBindings
class MyPageFragment : BaseFragment<FragmentMypageBinding>() {

    companion object {
        const val TAG = "MyPageFragment"
    }
    @Inject
    lateinit var auth : FirebaseAuth

    @Inject
    lateinit var userDB : DatabaseReference

    @Inject
    lateinit var callbackManager : CallbackManager

    private val movieList = mutableListOf<Movie>()

    private val viewModel: MyPageViewModel by viewModels()

    private val isVisibilityGroup = true

    private lateinit var mySearchMovieListAdapter: MySearchMovieListAdapter

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val movie = snapshot.getValue(Movie::class.java)
            movie ?: return
            movieList.add(movie)
            mySearchMovieListAdapter.submitList(mutableListOf<Movie>().apply {
                addAll(movieList)
            })
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val movie = snapshot.getValue(Movie::class.java)
            movieList.remove(movie)
            mySearchMovieListAdapter.submitList(mutableListOf<Movie>().apply {
                addAll(movieList)
            })
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    override fun getViewBinding(): FragmentMypageBinding = FragmentMypageBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSignUp()
        initLogin()
        initLogout()
        observeData()
    }

    private fun initViews() = with(binding) {
        setVisibleViewGroup()

        idEditTextView.addTextChangedListener {
            if(auth.currentUser == null) {
                var enabled = idEditTextView.text.isNullOrEmpty() or pwEditTextView.text.isNullOrEmpty()
                signUpButton.isEnabled = !enabled
                loginButton.isEnabled = !enabled
            }
        }

        pwEditTextView.addTextChangedListener {
            if(auth.currentUser == null) {
                var enabled = idEditTextView.text.isNullOrEmpty() or pwEditTextView.text.isNullOrEmpty()
                signUpButton.isEnabled = !enabled
                loginButton.isEnabled = !enabled
            }
        }
        mySearchMovieListAdapter = MySearchMovieListAdapter()
        mySearchMovieListAdapter.setCloseClickListener { movie ->
            viewModel.deleteSeeMovie(movie = movie)
        }
        mySearchMovieListAdapter.setItemClickListener { movie ->
            (activity as MainActivity).supportFragmentManager.fragments.find { it is MovieDetailFragment }
                ?.let {
                    (it as MovieDetailFragment).setMovie(
                        movie.image,
                        movie.actor,
                        movie.pubDate,
                        movie.userRating,
                        movie.title.replaceMultipleBlank("<b>","</b>")
                    )
                }
        }
        mySearchMovieListRecyclerView.adapter = mySearchMovieListAdapter
        if(auth.currentUser != null) userDB.child(auth.currentUser!!.uid).child("see").addChildEventListener(childEventListener)
    }

    private fun observeData() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentUserStateFlow.collect { myPageState ->
                when(myPageState) {
                    is MypageState.UnInitialized -> handleUnInitialized()
                    is MypageState.Loading -> handleLoading()
                    is MypageState.Success -> handleSuccess(myPageState.user)
                    is MypageState.Error -> handleError(myPageState.errorMessage)
                }

            }
        }
    }

    private fun initSignUp() = with(binding) {
        signUpButton.setOnClickListener {
            val id = idEditTextView.text.toString()
            val pw = pwEditTextView.text.toString()

            /** email 형식에 맞다면(TextInputLayout은 inputType 으로 형식 체크 불가능) */
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches() && pw.length >= 6) {
               viewModel.signUpEmail(id,pw)
            } else {
                context?.let {it.toastShort("아이디가 이메일 형식에 맞지 않거나 비밀번호가 6자리 이상이어야 합니다.")}
            }

        }
    }

    private fun handleUnInitialized() = with(binding) {
        setVisibleViewGroup()
    }

    private fun handleLoading() = with(binding) {
        progressBar.isGone = false
    }

    private fun handleSuccess(user : FirebaseUser) = with(binding) {
        progressBar.isGone = true
        context?.let {it.toastShort("로그인에 성공했습니다.")}
        successLogin(user)
    }

    private fun handleError(errorMessage : String) = with(binding) {
        progressBar.isGone = true
        context?.let { it.toastShort("로그인에 실패했습니다.\n error : ${errorMessage}")}
    }

    private fun initLogin() = with(binding) {
        /** 이메일 로그인 */
        loginButton.setOnClickListener {
            if(auth.currentUser == null) {

                val id = idEditTextView.text.toString()
                val pw = pwEditTextView.text.toString()
                viewModel.signInEmail(id,pw)
            }
        }
        /** Facebook 유저의 가져올 정보 permission */
        facebookLoginButton.setPermissions("email","public_profile")

        /** Facebook 로그인 */
        facebookLoginButton.registerCallback(callbackManager , object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                    Log.d("facebook" , result.toString())
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    viewModel.signInFacebook(credential)
            }

            override fun onCancel() {}
            override fun onError(error: FacebookException) {
                requireContext().toastShort("페이스북 로그인에 실패했습니다.")
            }
        })
    }

    private fun successLogin(user: FirebaseUser) = with(binding) {
        if(auth.currentUser == null) {
            context?.toastShort("로그인에 실패했습니다.")
            return@with
        }
        setVisibleViewGroup()
        setUserDBListener()
        signUpButton.isEnabled = false
        loginButton.isEnabled = false
        idEditTextView.text?.clear()
        pwEditTextView.text?.clear()
        idEditTextView.isEnabled = false
        pwEditTextView.isEnabled = false
    }

    private fun setUserDBListener() {
        movieList.clear()
        userDB.child(auth.currentUser!!.uid).child("see").removeEventListener(childEventListener)
        userDB.child(auth.currentUser!!.uid).child("see").addChildEventListener(childEventListener)
    }

    private fun setVisibleViewGroup() = with(binding) {
        if(auth.currentUser == null) {
            loginViewGroup.isVisible = isVisibilityGroup
            logoutViewGroup.isVisible = isVisibilityGroup.not()

            emailTextView.text = ""

        } else {
            loginViewGroup.isVisible = isVisibilityGroup.not()
            logoutViewGroup.isVisible = isVisibilityGroup

            emailTextView.text = auth.currentUser?.email
        }
    }

    private fun initLogout() = with(binding) {

        logOutButton.setOnClickListener {
            auth.currentUser?.providerData?.forEach { userInfo ->
                when(userInfo.providerId) {
                    "facebook.com" -> {
                        LoginManager.getInstance().logOut()
                        setVisibleViewGroup()
                    }
                    else -> {
                        auth.signOut()
                        setVisibleViewGroup()
                    }
                }
            }
            context?.let{it.toastShort("로그아웃에 성공했습니다.")}
            signUpButton.isEnabled = false
            loginButton.isEnabled = false
            idEditTextView.isEnabled = true
            pwEditTextView.isEnabled = true
            movieList.clear()
            mySearchMovieListAdapter.submitList(mutableListOf<Movie>())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(auth.currentUser != null) userDB.child(auth.currentUser!!.uid).child("see").removeEventListener(childEventListener)
    }

}