package com.jasoseol.test.utils

import android.app.Activity
import com.jasoseol.test.bases.BaseDataCacheManager
import com.jasoseol.test.models.data.MovieListModel

class DataCacheManager : BaseDataCacheManager() {

    fun addFavoriteMovie(activity: Activity, content: MovieListModel, onComplete: ((filePath: String?) -> Unit)? = null) {
        val recentAllStaySearchList = getFavoriteMovies(activity).apply {
            var isMatched = false
            forEach {
                if (!isMatched) {
                    isMatched = it.code == content.code
                }
            }
            // 중복되는 아이템이 없는 경우 필터를 추가한다.
            if (!isMatched) add(0, content)
        }

        saveFavoriteMovies(activity, recentAllStaySearchList, onComplete)
    }

    fun removeFavoriteMovie(activity: Activity, content: MovieListModel, onComplete: ((filePath: String?) -> Unit)? = null) {
        val recentAllStaySearchList = getFavoriteMovies(activity).apply {
            val iterator = iterator()
            while (iterator.hasNext()) {
                val model = iterator.next()
                if (model.code == content.code) {
                    iterator.remove()
                }
            }
        }

        saveFavoriteMovies(activity, recentAllStaySearchList, onComplete)
    }

    private fun saveFavoriteMovies(activity: Activity, contents: ArrayList<MovieListModel>, onComplete: ((filePath: String?) -> Unit)? = null) {
        saveCacheListData(activity, FAVORITE_MOVIES, contents, onComplete)
    }

    fun getFavoriteMovies(activity: Activity): ArrayList<MovieListModel> {
        return getCacheListData(activity, FAVORITE_MOVIES, MovieListModel::class.java)
    }

    private object Holder {
        val INSTANCE = DataCacheManager()
    }

    companion object {
        private const val FAVORITE_MOVIES = "FAVORITE_MOVIES.json"

        val instance: DataCacheManager by lazy { Holder.INSTANCE }
    }
}