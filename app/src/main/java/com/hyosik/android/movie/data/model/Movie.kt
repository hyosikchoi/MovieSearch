package com.hyosik.android.movie.data.model

data class Movie(
    val actor: String,
    val director: String,
    val image: String,
    val link: String,
    val pubDate: String,
    val subtitle: String,
    val title: String,
    val userRating: String
) {
    constructor() : this("" ,"" ,"" ,"" ,"" ,"" ,"" ,"")
}

