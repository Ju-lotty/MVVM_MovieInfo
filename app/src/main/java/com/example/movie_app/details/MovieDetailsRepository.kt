package com.example.movie_app.details

import androidx.lifecycle.LiveData
import com.example.movie_app.data.api.MovieDBInterface
import com.example.movie_app.data.repository.MovieDetailsNetworkData
import com.example.movie_app.data.repository.NetworkState
import com.example.movie_app.data.value_object.MovieDetails
import io.reactivex.disposables.CompositeDisposable

// MovieDBInterface 유형의 apiService라는 하나의 매개변수를 사용
class MovieDetailsRepository(private val apiService: MovieDBInterface) {

    // 나중에 fetchSingleMovieDetails() 함수로 초기화됩니다.
    // Movie Data를 가져오고 관리
    lateinit var movieDetailsNetworkData: MovieDetailsNetworkData

    // CompositeDisposable, movieId 매개 변수로 사용
    //  MovieDetailsNetworkData의 인스턴스를 만들고 해당 클래스의 함수 fetchMovieDetails() 사용하여 영화 세부 정보를 가져옴
    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<MovieDetails> {
        movieDetailsNetworkData = MovieDetailsNetworkData(apiService, compositeDisposable)
        movieDetailsNetworkData.fetchMovieDetails(movieId)

        // movieDetailsNetworkData 개체의 downloadMovieDeatailsResponse 속성을 반환
        return movieDetailsNetworkData.downloadedMovieDeatailsResponse
    }
    // movieDetailsNetworkData 객체의 networkState 속성을 반환
    fun getMovieDetailsNetWorkState(): LiveData<NetworkState> {
        return movieDetailsNetworkData.networkState
    }
}