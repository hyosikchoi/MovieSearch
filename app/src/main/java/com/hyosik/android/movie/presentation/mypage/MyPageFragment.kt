package com.hyosik.android.movie.presentation.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
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

    override fun getViewBinding(): FragmentMypageBinding = FragmentMypageBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSignUp()
        initLogin()

    }

    private fun initViews() = with(binding) {

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

            auth.createUserWithEmailAndPassword(id,pw)
                .addOnCompleteListener(requireActivity()) { task ->
                    if(task.isSuccessful) {
                        context?.let {it.toastShort("회원가입에 성공했습니다.")}
                        successLogin()
                    }
                    else context?.let { it.toastShort("이미 가입한 이메일이거나 , 회원가입에 실패했습니다.")}
                }

        }
    }

    private fun initLogin() = with(binding) {
        loginButton.setOnClickListener {

            if(auth.currentUser == null) {

                val id = idEditTextView.text.toString()
                val pw = pwEditTextView.text.toString()

                auth.signInWithEmailAndPassword(id,pw)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if(task.isSuccessful) successLogin()
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
    }

    private fun successLogin() = with(binding) {
        loginButton.text = "로그아웃"
        signUpButton.isEnabled = false
        loginButton.isEnabled = true
        idEditTextView.text?.clear()
        pwEditTextView.text?.clear()
        idEditTextView.isEnabled = false
        pwEditTextView.isEnabled = false
    }

}