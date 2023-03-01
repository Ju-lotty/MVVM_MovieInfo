package com.example.movie_app.data.api
import com.example.movie_app.data.value_object.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieDBInterface {

    //https://api.themoviedb.org/3/movie/popular?api_key=21321414325445621342156342156342
    //https://api.themoviedb.org/3/movie/1058949?api_key=21321414325445621342156342156342
    //https://api.themoviedb.org/3/

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id")id: Int): Single<MovieDetails>

}