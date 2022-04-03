package com.hyosik.android.movie.presentation.home

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth

import com.hyosik.android.movie.BaseFragment
import com.hyosik.android.movie.data.model.MovieDTO

import com.hyosik.android.movie.databinding.FragmentHomeBinding
import com.hyosik.android.movie.extensions.replaceMultipleBlank
import com.hyosik.android.movie.extensions.toastShort
import com.hyosik.android.movie.presentation.MainActivity
import com.hyosik.android.movie.presentation.detail.MovieDetailFragment
import com.hyosik.android.movie.presentation.home.adapter.MovieAdapter
import com.hyosik.android.movie.presentation.home.state.MovieState

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
@WithFragmentBindings
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        const val TAG = "HomeFragment"
    }

    @Inject
    lateinit var auth : FirebaseAuth

    private val viewModel: HomeViewModel by viewModels()

    private val movieAdapter = MovieAdapter()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun initViews() = with(binding) {

        recyclerView.adapter = movieAdapter

        searchText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) viewModel.searchMovie(
                searchText.text.toString(),
                10
            )
            return@setOnEditorActionListener false
        }

        searchButton.setOnClickListener {
            val imm =
                (requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE)) as InputMethodManager
            val isHide = imm.hideSoftInputFromWindow(searchText.windowToken, 0)
            Log.d("Main", searchText.text.toString())
            if (isHide) viewModel.searchMovie(searchText.text.toString(), 10)
        }

    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movieListStateFlow.collect { movieState ->
                when (movieState) {
                    is MovieState.UnInitialized -> {
                        handleUnInitialized()
                    }
                    is MovieState.Loading -> {
                        handleLoading()
                    }
                    is MovieState.Success -> {
                        handleSuccess(movieState.movieDTO)
                    }
                    is MovieState.Error -> {
                        handleError()
                    }
                }
            }
        }
    }

    private fun handleUnInitialized() = with(binding) {
        resultTextView.isGone = false
        recyclerView.isGone = true
        progressBar.isGone = true
    }

    private fun handleLoading() = with(binding) {
        resultTextView.isGone = true
        recyclerView.isGone = true
        progressBar.isGone = false
    }

    private fun handleSuccess(movieDTO: MovieDTO) = with(binding) {
        Log.d("Main", movieDTO.toString())
        if (movieDTO.items.isNotEmpty()) {
            movieAdapter.submitList(movieDTO.items)
            /** Movie 클릭 시 MovieDetailFragment 로 Movie data 전송 */
            movieAdapter.setMovieOnClickListener { movie ->
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
                if(auth.currentUser != null) {
                    viewModel.saveSeeMovie(movie)
                }
            }
            resultTextView.isGone = true
            recyclerView.isGone = false
            progressBar.isGone = true
        } else {
            resultTextView.isGone = false
            recyclerView.isGone = true
            progressBar.isGone = true
        }
    }

    private fun handleError() {
        context?.let {
            it.toastShort("에러가 발생했습니다. 잠시 후 다시 시도해주세요.")
        }
    }

}



