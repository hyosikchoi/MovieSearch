package com.hyosik.android.movie.presentation.mypage

import android.os.Bundle
import android.view.View
import com.hyosik.android.movie.BaseFragment
import com.hyosik.android.movie.databinding.FragmentMypageBinding


class MyPageFragment : BaseFragment<FragmentMypageBinding>() {

    companion object {
        const val TAG = "MyPageFragment"
    }

    override fun getViewBinding(): FragmentMypageBinding = FragmentMypageBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}