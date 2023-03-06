package com.example.movie_app.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movie_app.data.repository.NetworkState
import com.example.movie_app.data.value_object.MovieDetails
import io.reactivex.disposables.CompositeDisposable

// MovieDetailsRepository 유형의 movieRepository와 Int 유형의 movideId  두 매개변수를 사용

class DetailsViewModel(private val movieRepository: MovieDetailsRepository, movideId: Int): ViewModel() {

    // CompositeDisposable 클래스의 새 인스턴스를 할당, Disposable 객체를 보유하는 데 사용
    private val compositeDisposable = CompositeDisposable()

    // LiveData<MovieDetails> 유형의 movieDetails라는 공용 변수를 선언합니다.
    // movieRepository 개체의 fetchSingleMovieDetails() 메서드를 호출하는 람다 식으로 지연 초기화됩니다.
    val movieDetails: LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movideId)
    }

    // LiveData<NetworkState> 유형의 networkState라는 공용 변수를 선언합니다.
    // movieRepository 개체의 getMovieDetailsNetWorkState() 메서드를 호출하는 람다 식으로 지연 초기화됩니다.
    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetWorkState()
    }

    // ViewModel이 더 이상 사용되지 않으면 compositeDisposable 객체를 폐기합니다.
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}