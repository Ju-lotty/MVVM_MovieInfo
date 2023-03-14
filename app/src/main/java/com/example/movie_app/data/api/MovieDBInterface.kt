package com.example.movie_app.data.api
import com.example.movie_app.data.value_object.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface MovieDBInterface {

    //https://api.themoviedb.org/3/movie/popular?api_key=????????????????????????????????
    //https://api.themoviedb.org/3/movie/1058949?api_key=????????????????????????????????
    //https://api.themoviedb.org/3/

    // 필수 속성, 베이스 url을 제외한 나머지 url을 @GET형식으로 불러옴 (Request 파라미터 추가하는 방식)
    @GET("movie/{movie_id}")
    //경로 매개변수 movie_id를 id로 변경. Single 단일 값을 내보낸 다음 완료되는 RxJava 비동기 요청 및 응답을 처리하는 데 사용
    fun getMovieDetails(@Path("movie_id")id: Int): Single<MovieDetails>
}
