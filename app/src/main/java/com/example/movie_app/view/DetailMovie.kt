package com.example.movie_app.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movie_app.R
import com.example.movie_app.network.MovieDBClient
import com.example.movie_app.network.MovieDBInterface

import com.example.movie_app.network.NetworkState
import com.example.movie_app.model.MovieDetails
import com.example.movie_app.model.MovieDetailsRepository
import com.example.movie_app.databinding.ActivityDetailBinding
import com.example.movie_app.network.POSTER_BASE_URL
import com.example.movie_app.viewmodel.DetailsViewModel
import java.text.NumberFormat
import java.util.*

class DetailMovie: AppCompatActivity() {
    // 나중에 초기화 할 binding, viewModel, movieRepository 선언
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailsViewModel
    private lateinit var movieRepository: MovieDetailsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        //getIntExtra를 통해 MainActivity에서 보낸 Int형 값을 받음
        val movieId: Int = intent.getIntExtra("id",1)

        // MovieDBClient 및 MovieDetailsRepository 클래스를 사용하여 apiService 및 movieRepository를 초기화합니다.
        val apiService: MovieDBInterface = MovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)


        //getViewModel 메서드를 호출하여 movieRepository 및 movieId를 사용하여 DetailsViewModel 인스턴스를 얻고 viewModel에 할당
        viewModel = getViewModel(movieId)


        // viewModel의 movieDetails 및 networkState LiveData 개체의 변경 사항을 관찰합니다.
        //수신된 MovieDetails 인스턴스로 UI를 업데이트하기 위해 bindUI 메서드를 호출합니다.
        viewModel.movieDetails.observe(this) {
            bindUI(it)
        }
        viewModel.networkState.observe(this) {
            binding.movieProgressBar.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.movieTxtError.visibility =
                if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        }
    }
    // 수신된 MovieDetails 인스턴스의 세부 정보로 레이아웃의 UI 구성 요소를 업데이트하는 bindUI 메서드를 정의합니다.
    @SuppressLint("SetTextI18n")
    private fun bindUI(it: MovieDetails) = with(binding) {
        movieName.text = it.title
        movieSubtitle.text = it.tagline
        movieOpenDay.text = it.releaseDate
        movieRating.text = it.rating.toString()
        movieRuntime.text = it.runtime.toString() + " minutes"
        movieOverview.text =it.overview

        val formatNum = NumberFormat.getCurrencyInstance(Locale.US)
        movieBudget.text = formatNum.format(it.budget)
        movieRevenue.text = formatNum.format(it.revenue)

        val posterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this@DetailMovie).load(posterURL).into(movieImgPoster)
    }

    private fun getViewModel(movieId: Int): DetailsViewModel {
        /*
        필요한 객체를 만들기 위한 interface를 선언해서 유연하게 구현해서 사용할 수 있도록 하는 패턴
        ViewModelProvider로 ViewModel객체를 만들때 초기 값을 전달하는 것이 금지, Factory 패턴을 사용
        ViewModelProvider.Factory 인터페이스를 구현 → DetailsViewModel의 인스턴스를 만들 수 있는 팩터리를 생성
         */

        val viewModelFactory = object : ViewModelProvider.Factory {
            // 생성 메서드를 재정의합니다.
            // <T : ViewModel> create 메서드가 ViewModel의 하위 클래스여야 하는 유형 매개변수 T를 사용하도록 지정
            // modelClass 매개변수는 생성하려는 ViewModel의 클래스를 지정
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                //DetailsViewModel 개체를 일반 유형 T로 캐스팅할 때 발생하는 확인되지 않은 캐스팅 경고를 억제
                @Suppress("UNCHECKED_CAST")
                // movieRepository 및 movieId 매개변수를 사용하여 DetailsViewModel의 새 인스턴스를 생성하고 결과를 T 유형으로 캐스팅
                return DetailsViewModel(movieRepository, movieId) as T
            }
        }
        /*
        마지막으로 ViewModelProvider는 방금 생성한 팩토리를 사용하여
        DetailsViewModel의 인스턴스를 검색하는 데 사용되며 Activity 또는 Fragment
        및 DetailsViewModel::class.java 참조를 매개변수로 전달합니다.
        ViewModelProvider는 팩토리를 사용하여 DetailsViewModel이 아직 없는 경우 새 인스턴스를 생성하거나 이미 사용 가능한 경우 기존 인스턴스를 반환합니다.
        */
        return ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
    }
}

