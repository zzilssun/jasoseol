package com.jasoseol.test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.parseAsHtml
import com.google.gson.Gson
import com.jasoseol.test.databinding.ActivityWebviewBinding
import com.jasoseol.test.models.data.MovieListModel
import com.jasoseol.test.utils.DataCacheManager

/**
 * Created by Son Aujili on 2022/08/13.
 */
class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebviewBinding
    private lateinit var movieListModel: MovieListModel

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!intent.hasExtra("movieListModel")) {
            finish()
            return
        }

        movieListModel = Gson().fromJson(intent.getStringExtra("movieListModel") ?: "{}", MovieListModel::class.java)

        binding.run {
            val activity = this@WebViewActivity

            txtTitle.text = movieListModel.title.parseAsHtml()
            viewMovie.setView(activity, movieListModel) { movieListModel ->
                if (movieListModel.isFavorite(activity)) {
                    DataCacheManager.instance.removeFavoriteMovie(activity, movieListModel) {
                        viewMovie.updateView(activity, movieListModel)
                    }
                } else {
                    DataCacheManager.instance.addFavoriteMovie(activity, movieListModel) {
                        viewMovie.updateView(activity, movieListModel)
                    }
                }
            }
            webView.apply {
                settings.run {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    domStorageEnabled = true    // HTML5 Local Storage API 설정
                    databaseEnabled = true      // database storage API 사용 설정
                    setSupportZoom(true)

                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }

                webChromeClient = WebChromeClient()
                webViewClient = JasoseolWebViewClient()
            }.loadUrl(movieListModel.link)
        }
    }

    class JasoseolWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    companion object {
        fun startActivity(activity: AppCompatActivity, movieListModel: MovieListModel) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).apply {
                putExtra("movieListModel", Gson().toJson(movieListModel))
            })
        }
    }
}