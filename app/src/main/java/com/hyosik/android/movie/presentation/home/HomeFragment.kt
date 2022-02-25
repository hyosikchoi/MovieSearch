package com.hyosik.android.movie.presentation.home

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels

import com.hyosik.android.movie.BaseFragment
import com.hyosik.android.movie.data.model.MovieDTO

import com.hyosik.android.movie.databinding.FragmentHomeBinding
import com.hyosik.android.movie.presentation.home.state.MovieState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
@WithFragmentBindings
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel : HomeViewModel by viewModels()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
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
            viewModel.searchMovie(searchText.text.toString() , 10)
        }

    }

    private fun observeData() {
        viewModel.movieListStateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is MovieState.UnInitialized -> {

                }
                is MovieState.Loading -> {

                }
                is MovieState.Success -> {
                    handleSuccess(it.movieDTO)
                }
            }

        }
    }

    private fun handleSuccess(movieDTO: MovieDTO) {
        Log.d("Main" , movieDTO.toString())
    }

}