package com.example.movie_app.model


import com.google.gson.annotations.SerializedName

data class MoviePopulars(
    val page: Int,
    @SerializedName("results")
    val movieList: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)