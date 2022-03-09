package com.hyosik.android.movie.presentation.home
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isGone
import androidx.fragment.app.viewModels

import com.hyosik.android.movie.BaseFragment
import com.hyosik.android.movie.data.model.MovieDTO

import com.hyosik.android.movie.databinding.FragmentHomeBinding
import com.hyosik.android.movie.extensions.toastShort
import com.hyosik.android.movie.presentation.MainActivity
import com.hyosik.android.movie.presentation.detail.MovieDetailFragment
import com.hyosik.android.movie.presentation.home.adapter.MovieAdapter
import com.hyosik.android.movie.presentation.home.state.MovieState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext
@AndroidEntryPoint
@WithFragmentBindings
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel : HomeViewModel by viewModels()

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
            if (actionId == EditorInfo.IME_ACTION_DONE) viewModel.searchMovie(searchText.text.toString() , 10)
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
                    handleUnInitialized()
                }
                is MovieState.Loading -> {
                    handleLoading()
                }
                is MovieState.Success -> {
                    handleSuccess(it.movieDTO)
                }

                is MovieState.Error -> {
                    handleError()
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
        Log.d("Main" , movieDTO.toString())
        if(movieDTO.items.isNotEmpty()) {
            movieAdapter.submitList(movieDTO.items)
            /** Movie 클릭 시 MovieDetailFragment 로 Movie data 전송 */
            movieAdapter.setMovieOnClickListener { movie ->
                (activity as MainActivity).supportFragmentManager.fragments.find { it is MovieDetailFragment}?.let {
                    (it as MovieDetailFragment).setMovie(
                        movie.image,
                        movie.actor,
                        movie.pubDate,
                        movie.userRating,
                        movie.title.replace("<b>" , "").replace("</b>" , ""))
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



