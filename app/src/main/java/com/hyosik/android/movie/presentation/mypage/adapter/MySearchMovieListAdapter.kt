package com.hyosik.android.movie.presentation.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyosik.android.movie.R
import com.hyosik.android.movie.data.model.Movie
import com.hyosik.android.movie.databinding.ItemMyMovieBinding
import com.hyosik.android.movie.extensions.replaceMultipleBlank

class MySearchMovieListAdapter :
    ListAdapter<Movie, MySearchMovieListAdapter.MySearchMovieListViewHolder>(diffUtil) {

    private lateinit var itemClickListener: (Movie) -> Unit

    private lateinit var closeClickListener: (Movie) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySearchMovieListViewHolder =
        MySearchMovieListViewHolder(
            ItemMyMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), closeClickListener = closeClickListener, itemClickListener = itemClickListener
        )

    override fun onBindViewHolder(holder: MySearchMovieListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun setCloseClickListener(closeClickListener: (Movie) -> Unit) {
        this.closeClickListener = closeClickListener
    }

    fun setItemClickListener(itemClickListener: (Movie) -> Unit) {
        this.itemClickListener = itemClickListener
    }

    inner class MySearchMovieListViewHolder(
        private val binding: ItemMyMovieBinding,
        private val closeClickListener: (Movie) -> Unit,
        private val itemClickListener: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) = with(binding) {

            Glide.with(myMovieImageView.context).load(movie.image)
                .error(R.drawable.ic_image_not_supported).into(myMovieImageView)
            myMovieTitleTextView.text = movie.title.replaceMultipleBlank("<b>", "</b>")

            deleteImageView.setOnClickListener {
                closeClickListener(movie)
            }
            
            root.setOnClickListener {
                itemClickListener(movie)
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