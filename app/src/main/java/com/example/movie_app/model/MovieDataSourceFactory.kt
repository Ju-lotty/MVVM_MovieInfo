package com.example.movie_app.model

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movie_app.model.Movie
import com.example.movie_app.network.MovieDBInterface
import com.example.movie_app.network.MovieDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(private val apiService: MovieDBInterface, private val compositeDisposable: CompositeDisposable): DataSource.Factory<Int, Movie>() {
    val movieLiveDataSource = MutableLiveData<MovieDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)
        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}