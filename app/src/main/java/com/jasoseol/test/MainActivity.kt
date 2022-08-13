package com.jasoseol.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jasoseol.test.adapters.NaverMovieListAdapter
import com.jasoseol.test.databinding.ActivityMainBinding
import com.jasoseol.test.utils.*
import com.jasoseol.test.viewModels.MainViewModel

/**
 * Created by Son Aujili on 2022/08/10.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private var isLoading = false
    private var isLoadMore = true

    private val onScrollListener = RecyclerViewLoadMoreListener {
        requestSearchNaverMovies(false)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.btnClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            val activity = this@MainActivity

            viewModel.run {
                btnFavorite.clicksDelay().subscribe {
                    FavoriteMoviesActivity.startActivity(activity)
                }
                editSearch.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        requestSearchNaverMovies(true)

                        editSearch.clearFocus()
                        editSearch.hideKeyboard(activity)
                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener false
                }
                btnClear.setOnClickListener {
                    editSearch.setText("")
                    editSearch.requestFocusAndScroll()
                    activity.showKeyboard()
                }
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

                naverMovies.observe(activity) { response ->
                    (recyclerView.adapter as NaverMovieListAdapter).run {
                        putItems(response.items)
                        if (itemCount >= response.total) {
                            isLoadMore = false
                        }
                    }

                    isLoading = false
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.run {
                    if (editSearch.text.toString().isNotEmpty()) {
                        editSearch.setText("")
                    } else {
                        finish()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        binding.run {
            editSearch.addTextChangedListener(textWatcher)
            recyclerView.addOnScrollListener(onScrollListener)
        }
    }

    override fun onPause() {
        super.onPause()

        binding.run {
            editSearch.removeTextChangedListener(textWatcher)
            recyclerView.removeOnScrollListener(onScrollListener)
        }
    }

    private fun requestSearchNaverMovies(isClear: Boolean) {
        binding.run {
            viewModel.run {
                if (isClear) {
                    isLoadMore = true
                    (recyclerView.adapter as NaverMovieListAdapter).clear()
                }

                val query = editSearch.text.toString()
                if (query.isEmpty() || isLoading || !isLoadMore) return
                val start = (recyclerView.adapter as NaverMovieListAdapter).itemCount + 1

                isLoading = true
                requestSearchNaverMovies(query, start)
            }
        }
    }
}