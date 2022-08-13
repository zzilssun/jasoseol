package com.jasoseol.test.models.data

import android.app.Activity
import android.net.Uri
import com.jasoseol.test.utils.DataCacheManager

/**
 * Created by Son Aujili on 2022/08/10.
 */
class MovieListModel {
    var title = ""
    var link = ""
    var image = ""
    var subtitle = ""
    var pubDate = ""
    var director = ""
    var actor = ""
    var userRating = ""

    val code: Int
        get() {
            return try {
                Uri.parse(link).getQueryParameter("code")?.toInt() ?: 0
            } catch (e: Exception) {
                0
            }
        }

    fun isFavorite(activity: Activity): Boolean {
        return DataCacheManager.instance.getFavoriteMovies(activity).any { it.code == this.code }
    }
}