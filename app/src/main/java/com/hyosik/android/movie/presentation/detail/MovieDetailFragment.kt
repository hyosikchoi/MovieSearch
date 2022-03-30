package com.hyosik.android.movie.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.bumptech.glide.Glide
import com.hyosik.android.movie.BaseFragment
import com.hyosik.android.movie.R
import com.hyosik.android.movie.databinding.FragmentMovieDetailBinding
import com.hyosik.android.movie.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlin.math.abs

@AndroidEntryPoint
@WithFragmentBindings
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    companion object {
        const val TAG = "MovieDetailFragment"
    }

    override fun getViewBinding(): FragmentMovieDetailBinding  = FragmentMovieDetailBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMotionLayout()
        initView()
    }


    private fun initMotionLayout() = with(binding) {

        movieMotionLayout.setTransitionListener(object: MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {}

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                (activity as MainActivity).also { mainActivity ->
                    mainActivity.binding.mainMotionLayout.progress = abs(progress)
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when {
                    motionLayout.progress > 0.9f -> {
                        (activity as MainActivity).also { mainActivity ->
                            mainActivity.binding.mainMotionLayout.progress = 1.0f
                        }
                    }
                    motionLayout.progress < 0.2f -> {
                        (activity as MainActivity).also { mainActivity ->
                            mainActivity.binding.mainMotionLayout.progress = 0.0f
                        }
                    }
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {}
        })

    }

    private fun initView() = with(binding) {
        bottomCloseImageView.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction().hide(this@MovieDetailFragment).commit()
        }
    }

    fun setMovie(url : String,actor : String,pubDate : String, userRate : String,title : String) = with(binding) {
        (activity as MainActivity).supportFragmentManager.beginTransaction().show(this@MovieDetailFragment).commit()
        movieMotionLayout.transitionToEnd()
        Glide.with(movieImageView.context).load(url).error(R.drawable.ic_image_not_supported).into(movieImageView)
        bottomMovieTitleTextView.text = title
        movieTitleTextView.text = title
        movieActorTextView.text = if(actor == "") getString(R.string.none) else actor
        moviePubDateTextView.text = if(pubDate == "") getString(R.string.none) else pubDate
        movieUserRateTextView.text = userRate
    }

}