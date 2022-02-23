package com.hyosik.android.movie.data.model

data class MovieDTO(
    val display: Int,
    val items: List<Movie>,
    val lastBuildDate: String,
    val start: Int,
    val total: Int
)