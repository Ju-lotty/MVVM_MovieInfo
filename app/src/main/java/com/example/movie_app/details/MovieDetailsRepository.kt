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
    // Network Data를 가져오고 관리
    lateinit var movieDetailsNetworkData: MovieDetailsNetworkData

    // CompositeDisposable 유형의 compositeDisposable과 Int 유형의 movieId라는 두 가지 매개변수를 사용
    // movieDetailsNetworkData 변수를 초기화하고 movieId 매개변수로 fetchMovieDetails() 메서드를 호출
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

/*
    ViewModel과 API 서비스 간의 중간 계층 역할
    apiService를 사용하여 영화 세부 정보를 가져오고
    결과 LiveData 개체를 반환하는 데 사용되는 movieDetailsNetworkData 개체를 초기화합니다.
 */