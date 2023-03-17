package com.example.movie_app.model
import com.google.gson.annotations.SerializedName

// 유저가 선택한 영화에 상세 정보를 나타낼 때 필요한 데이터
data class MovieDetails(
        val budget: Int,
        val id: Int,
        val overview: String,
        val popularity: Double,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("release_date")
        val releaseDate: String,
        val revenue: Long,
        val runtime: Int,
        val status: String,
        val tagline: String,
        val title: String,
        val video: Boolean,
        @SerializedName("vote_average")
        val rating: Double
)