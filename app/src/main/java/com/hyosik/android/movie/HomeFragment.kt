package com.hyosik.android.movie

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager

import com.hyosik.android.movie.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        const val TAG = "HomeFragment"
    }

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

    }

    private fun initViews() = with(binding) {

        searchText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) Log.d("Main", v.text.toString())
            return@setOnEditorActionListener false
        }

        searchButton.setOnClickListener {

            context?.let {
                val imm = (it.getSystemService(Activity.INPUT_METHOD_SERVICE)) as InputMethodManager
                imm.hideSoftInputFromWindow(searchText.windowToken,0)
            }
            Log.d("Main", searchText.text.toString())
        }

    }

}