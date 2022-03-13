package com.hyosik.android.movie.presentation.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.hyosik.android.movie.BaseFragment
import com.hyosik.android.movie.databinding.FragmentMypageBinding
import com.hyosik.android.movie.extensions.toastShort
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
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
    lateinit var callbackManager : CallbackManager

    private val isVisibilityGroup = true

    override fun getViewBinding(): FragmentMypageBinding = FragmentMypageBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSignUp()
        initLogin()
        initLogout()
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

    }

    private fun initSignUp() = with(binding) {
        signUpButton.setOnClickListener {
            val id = idEditTextView.text.toString()
            val pw = pwEditTextView.text.toString()

            /** email 형식에 맞다면(TextInputLayout은 inputType 으로 형식 체크 불가능) */
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(id).matches() && pw.length >= 6) {
                auth.createUserWithEmailAndPassword(id,pw)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if(task.isSuccessful) {
                            context?.let {it.toastShort("회원가입에 성공했습니다.")}
                            successLogin()
                        }
                        else context?.let { it.toastShort("이미 가입한 이메일이거나 , 회원가입에 실패했습니다.")}
                    }
            } else {
                context?.let {it.toastShort("아이디가 이메일 형식에 맞지 않거나 비밀번호가 6자리 이상이어야 합니다.")}
            }

        }
    }

    private fun initLogin() = with(binding) {
        /** 이메일 로그인 */
        loginButton.setOnClickListener {

            if(auth.currentUser == null) {

                val id = idEditTextView.text.toString()
                val pw = pwEditTextView.text.toString()

                auth.signInWithEmailAndPassword(id,pw)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if(task.isSuccessful){
                            context?.let{it.toastShort("이메일 로그인에 성공했습니다.")}
                            successLogin()
                        }
                        else context?.let{it.toastShort("로그인에 실패했습니다. 아이디 또는 비밀번호를 확인해주세요.")}
                    }

            } else {
                auth.signOut()
                context?.let{it.toastShort("로그아웃에 성공했습니다.")}
                loginButton.text = "로그인"
                signUpButton.isEnabled = true
                loginButton.isEnabled = true
                idEditTextView.isEnabled = true
                pwEditTextView.isEnabled = true
            }
        }
        /** Facebook 유저의 가져올 정보 permission */
        facebookLoginButton.setPermissions("email","public_profile")

        /** Facebook 로그인 */
        facebookLoginButton.registerCallback(callbackManager , object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                    Log.d("facebook" , result.toString())
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)

                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if(task.isSuccessful) {
                                requireContext().toastShort("페이스북 로그인에 성공했습니다.")
                                successLogin()
                            }
                            else requireContext().toastShort("페이스북 로그인에 실패했습니다.")
                        }
            }

            override fun onCancel() {}
            override fun onError(error: FacebookException) {
                requireContext().toastShort("페이스북 로그인에 실패했습니다.")
            }
        })
    }

    private fun successLogin() = with(binding) {
        setVisibleViewGroup()
        signUpButton.isEnabled = false
        loginButton.isEnabled = false
        idEditTextView.text?.clear()
        pwEditTextView.text?.clear()
        idEditTextView.isEnabled = false
        pwEditTextView.isEnabled = false

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
        }
    }

}