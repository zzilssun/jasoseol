package com.jasoseol.test.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jasoseol.test.databinding.ListItemMovieBinding
import com.jasoseol.test.models.data.MovieListModel
import com.jasoseol.test.setView
import com.jasoseol.test.utils.clicksDelay

/**
 * Created by Son Aujili on 2022/08/13.
 */
@SuppressLint("NotifyDataSetChanged")
class NaverMovieListAdapter(
    private val activity: Activity,
    private val onClick: (movieListModel: MovieListModel) -> Unit,
    private val onClickFavorite: (movieListModel: MovieListModel) -> Unit
) : RecyclerView.Adapter<RecyclerViewHolder<ListItemMovieBinding>>() {
    private val items = ArrayList<MovieListModel>()

    fun putItems(items: ArrayList<MovieListModel>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<ListItemMovieBinding> {
        return RecyclerViewHolder(ListItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder<ListItemMovieBinding>, position: Int) {
        val movieListModel = items[holder.bindingAdapterPosition]
        holder.viewBinding.run {
            setView(activity, movieListModel, onClickFavorite)
            root.clicksDelay().subscribe {
                onClick(movieListModel)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}