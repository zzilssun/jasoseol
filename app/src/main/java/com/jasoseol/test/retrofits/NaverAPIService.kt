package com.jasoseol.test.retrofits

import com.jasoseol.test.models.response.MovieListResponse
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Son Aujili on 2022/08/10.
 */
interface NaverAPIService {

    /**
     * 네이버 영화 검색 목록 가져오기
     */
    @GET(NaverAPIConsts.V1_SEARCH)
    fun requestSearchNaverMovies(
        @Query("query") query: String,
        @Query("start") start: Int = 1,
        @Query("display") display: Int = 30
    ): Flowable<MovieListResponse>
}