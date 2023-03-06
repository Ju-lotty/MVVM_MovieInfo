package com.example.movie_app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movie_app.data.api.MovieDBInterface
import com.example.movie_app.data.value_object.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/* 영화 세부 정보에 대한 Network Data를 가져오고 관리하는데 필요한 2개의 매개변수
    apiService - 네트워크 요청시 메서드를 제공한 인터페이스를 상속
    compositeDisposable - 네트워크 호출 중에 생성된 RxJava disposables를 관리하고 폐기하는 데 사용, 메모리 누수 및 앱 충돌 예방
 */
class MovieDetailsNetworkData(private val apiService: MovieDBInterface, private val compositeDisposable: CompositeDisposable) {

/*
    _networkState :  현재 네트워크 상태 MutableLiveData LOADING, LOADED 및 ERROR 상태를 나타내는 enum 클래스
    networkState : 현재 네트워크 상태 LiveData (변경 불가, 관찰만 가능)
    get() : _networkState 값을 반환하는 접근자
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
    fun fetchMovieDetails(movieId: Int) {

        // postValue() 메서드를 사용하여 _networkState 속성을 LOADING으로 설정
        _networkState.postValue(NetworkState.LOADING)
        // val networkState 속성의 관찰자가 새 상태로 업데이트


/* compositeDisposable.add : 메서드를 사용하여 새로운 Disposable 인스턴스 compositeDisposable에 추가

add(apiService.getMovieDetails(movieId) : movieId 매개변수를 사용하여 apiService 객체의 getMovieDetails() 메서드를 호출
subscribeOn(Schedulers.io()) : UI 스레드 차단을 방지, 백그라운드 스레드에서 수행하도록 Schedulers.io() 스레드 지정

subscribe() : 두 개의 콜백을 지정
데이터의 성공적인 방출, 오류 사례

_downloadedMovieDeatailsResponse.postValue(it) : postValue() 메서드를 사용하여 세부 정보 업데이트
그 후 val downloadMovieDeatailsResponse 속성의 관찰자가 새 데이터로 업데이트
_networkState.postValue(NetworkState.LOADED) : postValue() 메서드를 사용하여 _networkState 속성을 LOADED로 설정

오류의 경우 postValue() 메서드를 사용하여 _networkState 속성을 ERROR로 설정하고 오류 메시지는 Log 클래스를 사용하여 기록합니다.
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