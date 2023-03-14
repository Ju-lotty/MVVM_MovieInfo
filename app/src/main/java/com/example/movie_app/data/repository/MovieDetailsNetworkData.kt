package com.example.movie_app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movie_app.data.api.MovieDBInterface
import com.example.movie_app.data.value_object.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/*
    apiService - 네트워크 요청시 메서드를 제공한 인터페이스를 사용
    compositeDisposable - 네트워크 호출 중에 생성된 RxJava disposables를 관리하고 폐기하는 데 사용, 메모리 누수 및 앱 충돌 예방
 */
class MovieDetailsNetworkData(private val apiService: MovieDBInterface, private val compositeDisposable: CompositeDisposable) {

/*
    _networkState :  현재 네트워크 상태 MutableLiveData LOADING, LOADED 및 ERROR 상태를 나타내는 enum 클래스
    networkState : 현재 네트워크 상태 LiveData (변경 불가, 관찰만 가능)
    get() : _networkState 값을 반환하는 접근자

    LiveData 데이터 변경 사항을 관찰
    MutableLiveData 관찰되는 데이터의 값을 변경 Observer를 통해 UI 업데이트
*/
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState


/*
    _downloadedMovieDeatailsResponse : 영화 세부 정보 데이터 보유하는 MutableLiveData (변경 가능, 새 값으로 업데이트)
    downloadMovieDeatailsResponse : 다운로드한 동영상 세부 정보 데이터에 대한 LiveData (변경 불가, 관찰만 가능)
    get() : _downloadedMovieDeatailsResponse 값을 반환하는 접근자
*/
    private val _downloadedMovieDeatailsResponse = MutableLiveData<MovieDetails>()
    val downloadedMovieDeatailsResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDeatailsResponse


    // 이 함수는 네트워크에서 세부 정보 데이터를 가져옴 _downloadedMovieDeatailsResponse 및 _networkState 속성을 업데이트
    // 주어진 영화 ID에 대한 영화 세부 정보를 가져오는 데 사용
    fun fetchMovieDetails(movieId: Int) {

        // postValue() 메서드를 사용하여 _networkState 속성을 LOADING으로 설정
        _networkState.postValue(NetworkState.LOADING)
        // val networkState 속성의 관찰자가 새 상태로 업데이트


/* compositeDisposable.add : 새로운 Disposable 인스턴스 compositeDisposable에 추가

add(apiService.getMovieDetails(movieId) : movieId 매개변수를 사용하여 apiService 객체의 getMovieDetails() 메서드를 호출 (영화 세부정보)
subscribeOn(Schedulers.io()) : UI 스레드 차단을 방지, 백그라운드 스레드에서 수행하도록 Schedulers.io() 스레드 지정

subscribe() : 두 개의 콜백을 지정
성공
_downloadedMovieDeatailsResponse.postValue()를 사용하여 해당 값을 업데이트
_networkState.postValue()를 사용하여 네트워크 상태를 LOADED로 업데이트

오류
_networkState.postValue()를 사용하여 네트워크 상태를 ERROR로 업데이트
*/
        try {
            compositeDisposable.add(apiService.getMovieDetails(movieId).subscribeOn(Schedulers.io())
                    .subscribe( {
                        _downloadedMovieDeatailsResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDetailsNetworkData", "${it.message}")
                    })
            )
        } catch (e: Exception) {
            Log.e("MovieDetailsNetworkData", "${e.message}")
        }
    }
}