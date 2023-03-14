package com.example.movie_app.details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movie_app.R
import com.example.movie_app.data.api.MovieDBClient
import com.example.movie_app.data.api.MovieDBInterface
import com.example.movie_app.data.api.POSTER_BASE_URL
import com.example.movie_app.data.repository.NetworkState
import com.example.movie_app.data.value_object.MovieDetails
import com.example.movie_app.databinding.ActivityDetailBinding
import java.text.NumberFormat
import java.util.*

class DetailMovie : AppCompatActivity() {
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
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                //DetailsViewModel 개체를 일반 유형 T로 캐스팅할 때 발생하는 확인되지 않은 캐스팅 경고를 억제하는 데 사용됩니다.
                @Suppress("UNCHECKED_CAST")
                // 생성자에 전달된 movieRepository 및 movieId 매개변수가 있는 새로운 DetailsViewModel 객체를 T 유형으로 캐스트하여 반환합니다.
                return DetailsViewModel(movieRepository, movieId) as T
            }
        }
        return ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
    }
}