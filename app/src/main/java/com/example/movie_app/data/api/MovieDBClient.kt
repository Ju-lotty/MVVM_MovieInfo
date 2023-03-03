package com.example.movie_app.data.api

import com.example.movie_app.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/*  나중에 사용될 두 개의 상수 선언
     BASE_URL 상수는 The Movie DB API의 기본 URL을 지정하고
     POSTER_BASE_URL 상수는 영화 포스터 이미지의 기본 URL 지정
 */
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

// 싱글턴 패턴이 적용 된 MovieDBClient는 클래스 정의와 인스턴스 생성을 동시에 수행
// MovieDBClient 객체는 Retrofit 라이브러리를 사용하여 API와 통신하는데 필요한 Retrofit 인스턴스를 생성하는 함수를 포함
object MovieDBClient {

    // getClient 함수의 반환형은 MovieDBInterface @GET
    // 매번 인스턴스를 생성하는 대신에 MovieDBClient 객체를 사용하여 동일한 Retrofit 인스턴스를 공유하도록 할 수 있습니다.
    fun getClient(): MovieDBInterface {

        // Interceptor 생성 (HTTP 요청 및 응답을 가로채고 수정하는 데 사용)
        // 변수 requestInterceptor는 API 모든 요청에 API_KEY를 추가하는 데 사용
        // 인터셉터는 람다 함수인 하나의 인수만 취하므로 파라미터들을 생략

        val requestInterceptor = Interceptor { chain -> // Chain - 요청을 실행하는 데 사용
            val url = chain.request().url().newBuilder().addQueryParameter("api_key", BuildConfig.serverKey).build()
            // chain.request()로 부터 request를 받아 newBuilder()를 통해 builder를 생성한다.
            // 람다식은 addQueryParameter() 메서드를 사용하여 원래 요청의 URL에 매개변수 (api key)를 추가하여 새 URL을 생성
            // 1. api_key 매개변수를 추가하여 요청 URL을 수정한다.

            val request = chain.request() // request() 메서드를 사용하여 수정된 URL로 새요청
                .newBuilder()
                .url(url)
                .build()
            // 2. 수정된 URL로 새 요청을 생성

            return@Interceptor chain.proceed(request) // chain.proceed()에 파라미터로 builder를 담아 요청보낸다.
            // 3. 수정된 요청의 응답을 반환
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
            // 변수 requestInterceptor와 60초의 연결 제한 시간을 사용하여 OkHttpClient 인스턴스를 생성합니다.

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieDBInterface::class.java)

        /*
            변수 okHttpClient 및 기타 여러 매개변수를 사용하여 Retrofit 인스턴스를 생성
            addCallAdapterFactory 및 addConverterFactory 메소드는 Retrofit 인스턴스가 API 응답을 처리하기 위해 RxJava 및 Gson을 사용해야 함을 지정
            마지막으로 creat() 메소드는 MovieDB API에 요청하는 데 사용할 수 있는 MovieDBInterface 인터페이스의 인스턴스를 생성합니다.

            OkHttp 클라이언트, 기본 URL 및 Gson 변환기 팩토리가 있는 새로운 Retrofit 인스턴스. 또한 API 요청 시 Observable 객체를 반환하기 위해
            RxJava 호출 어댑터 팩토리를 추가합니다. 그런 다음 create() 메서드는 MovieDBInterface 인터페이스의 인스턴스를 반환합니다.
         */
    }
}

// Retrofit, OkHttp 및 RxJava를 사용하여 요청을 만들고 응답을 처리하는 MovieDB API용 API 클라이언트를 설정합니다.