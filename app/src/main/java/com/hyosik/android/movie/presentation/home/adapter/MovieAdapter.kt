package com.hyosik.android.movie.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyosik.android.movie.R
import com.hyosik.android.movie.data.model.Movie
import com.hyosik.android.movie.databinding.ItemMovieBinding

class MovieAdapter : ListAdapter<Movie, MovieAdapter.MovieItemViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        return MovieItemViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent , false))
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {

        holder.bind(currentList[position])

    }


    inner class MovieItemViewHolder(private val binding : ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie : Movie) = with(binding) {
            titleTextView.text = movie.title.replace("<b>" , "").replace("</b>" , "")
            publishTextView.text = if(movie.pubDate.isNullOrEmpty()) publishTextView.context.getString(R.string.no_information) else movie.pubDate

            Glide.with(thumbNailImageView.context).load(movie.image).error(R.drawable.ic_image_not_supported).into(thumbNailImageView)

        }
    }


    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<Movie>() {

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.link == newItem.link
            }
        }

    }

}