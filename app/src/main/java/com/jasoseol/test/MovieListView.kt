package com.jasoseol.test

import android.app.Activity
import androidx.core.text.parseAsHtml
import com.jasoseol.test.databinding.ListItemMovieBinding
import com.jasoseol.test.models.data.MovieListModel
import com.jasoseol.test.utils.clicksDelay
import com.jasoseol.test.utils.setImage

/**
 * Created by Son Aujili on 2022/08/13.
 */

fun ListItemMovieBinding.setView(
    activity: Activity,
    movieListModel: MovieListModel,
    onClickFavorite: (movieListModel: MovieListModel) -> Unit
) {
    movieListModel.run {
        imgMovie.setImage(root.context, image)
        txtName.text = title.parseAsHtml()
        txtDirector.text = "감독: $director"
        txtActor.text = "출연: $actor"
        txtRate.text = "평점: $userRating"
        btnFavorite.setImageResource(if (isFavorite(activity)) R.drawable.ic_favorite else R.drawable.ic_favorite_disable)
        btnFavorite.clicksDelay().subscribe {
            onClickFavorite(this)
        }
    }
}

fun ListItemMovieBinding.updateView(
    activity: Activity,
    movieListModel: MovieListModel,
) {
    movieListModel.run {
        imgMovie.setImage(root.context, image)
        txtName.text = title.parseAsHtml()
        txtDirector.text = "감독: $director"
        txtActor.text = "출연: $actor"
        txtRate.text = "평점: $userRating"
        btnFavorite.setImageResource(if (isFavorite(activity)) R.drawable.ic_favorite else R.drawable.ic_favorite_disable)
    }
}