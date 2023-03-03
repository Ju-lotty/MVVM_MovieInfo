package com.example.movie_app.data.api
import com.example.movie_app.data.value_object.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

//필수 속성, 베이스 url을 제외한 나머지 url을 @GET형식으로 불러옴 (request 파라미터 추가하는 방식)
interface MovieDBInterface {

    //https://api.themoviedb.org/3/movie/popular?api_key=????????????????????????????????
    //https://api.themoviedb.org/3/movie/1058949?api_key=????????????????????????????????
    //https://api.themoviedb.org/3/

    @GET("movie/{movie_id}")
    // Retrofit에 getMovieDetails 메서드의 id 매개변수가 URL의 {movie_id} 자리 표시자로 대체
    //반환 유형은 Single<MovieDetails>, Single 클래스는 RxJava 라이브러리의 일부이며 단일 데이터 항목 또는 오류를 내보냅니다.
    fun getMovieDetails(@Path("movie_id")id: Int): Single<MovieDetails>

}

