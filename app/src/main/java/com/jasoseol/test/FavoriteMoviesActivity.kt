package com.jasoseol.test

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jasoseol.test.adapters.NaverMovieListAdapter
import com.jasoseol.test.databinding.ActivityFavoriteMoviesBinding
import com.jasoseol.test.utils.DataCacheManager

/**
 * Created by Son Aujili on 2022/08/13.
 */
class FavoriteMoviesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteMoviesBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            val activity = this@FavoriteMoviesActivity

            recyclerView.adapter = NaverMovieListAdapter(
                activity = activity,
                onClick = { movieListModel ->
                    WebViewActivity.startActivity(activity, movieListModel)
                },
                onClickFavorite = { movieListModel ->
                    if (movieListModel.isFavorite(activity)) {
                        DataCacheManager.instance.removeFavoriteMovie(activity, movieListModel) {
                            (recyclerView.adapter as NaverMovieListAdapter).notifyDataSetChanged()
                        }
                    } else {
                        DataCacheManager.instance.addFavoriteMovie(activity, movieListModel) {
                            (recyclerView.adapter as NaverMovieListAdapter).notifyDataSetChanged()
                        }
                    }
                }
            )
            (recyclerView.adapter as NaverMovieListAdapter).putItems(DataCacheManager.instance.getFavoriteMovies(activity))
        }
    }

    companion object {
        fun startActivity(activity: Activity) {
            activity.startActivity(Intent(activity, FavoriteMoviesActivity::class.java))
        }
    }
}