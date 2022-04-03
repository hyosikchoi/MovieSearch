package com.hyosik.android.movie.presentation.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyosik.android.movie.R
import com.hyosik.android.movie.data.model.Movie
import com.hyosik.android.movie.databinding.ItemMovieBinding
import com.hyosik.android.movie.extensions.replaceMultipleBlank


class MovieAdapter : ListAdapter<Movie, MovieAdapter.MovieItemViewHolder>(diffUtil) {

    private lateinit var movieItemClickListener : (Movie) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        return MovieItemViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent , false),movieItemClickListener)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {

        holder.bind(currentList[position])
        holder.bindOnClick(currentList[position])
    }

    fun setMovieOnClickListener(movieItemClickListener : (Movie) -> Unit) {
        this.movieItemClickListener = movieItemClickListener
    }

    inner class MovieItemViewHolder(
        private val binding : ItemMovieBinding,
        private val movieItemClickListener : (Movie) -> Unit
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie : Movie) = with(binding) {
            titleTextView.text = movie.title.replaceMultipleBlank("<b>","</b>")
            publishTextView.text = if(movie.pubDate.isNullOrEmpty()) publishTextView.context.getString(R.string.no_information) else movie.pubDate

            Glide.with(thumbNailImageView.context).load(movie.image).error(R.drawable.ic_image_not_supported).into(thumbNailImageView)

        }

        fun bindOnClick(movie: Movie) = with(binding) {
            root.setOnClickListener {
                movieItemClickListener(movie)
            }
        }

    }


    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<Movie>() {

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.image == newItem.image
            }
        }

    }

}