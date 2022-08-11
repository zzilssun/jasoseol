package com.jasoseol.test.retrofits

import com.jasoseol.test.retrofits.bases.BaseAPIController
import com.jasoseol.test.models.response.MovieListResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Son Aujili on 2022/08/10.
 */
class NaverAPIController : BaseAPIController() {

    /**
     * 네이버 영화 검색 목록 가져오기
     */
    fun requestSearchNaverMovies(query: String, start: Int): Flowable<MovieListResponse> {
        return getService().requestSearchNaverMovies(query, start).applySchedulers()
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * APIService 가져오기
     */
    private fun getService(): NaverAPIService {
        return getRetrofitWithGsonConverter(getOkHttpClient().build()).create(NaverAPIService::class.java)
    }

    /**
     * Flowable 에 스케듈러 세팅
     */
    private fun <T : Any> Flowable<T>.applySchedulers(): Flowable<T> {
        return subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    private object Holder {
        val INSTANCE = NaverAPIController()
    }

    companion object {
        val instance: NaverAPIController by lazy { Holder.INSTANCE }
    }
}