package com.jasoseol.test.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jasoseol.test.GlideApp

/**
 * Created by Son Aujili on 2022/03/03.
 */

fun ImageView.setImage(context: Context, url: String) {
    try {
        GlideApp.with(context).load(url)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}