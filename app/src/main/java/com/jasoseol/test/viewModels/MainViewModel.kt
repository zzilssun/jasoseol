package com.jasoseol.test.viewModels

import androidx.lifecycle.MutableLiveData
import com.jasoseol.test.bases.BaseViewModel
import com.jasoseol.test.models.response.MovieListResponse
import com.jasoseol.test.retrofits.NaverAPIController

/**
 * Created by Son Aujili on 2022/08/11.
 */
class MainViewModel : BaseViewModel() {
    val naverMovies = MutableLiveData<MovieListResponse>()

    fun requestSearchNaverMovies(query: String, start: Int) {
        addDisposable(NaverAPIController.instance.requestSearchNaverMovies(query, start).subscribe({ response ->
            naverMovies.value = response
        }, {

        }))
    }
}