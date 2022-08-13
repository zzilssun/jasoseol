package com.jasoseol.test.utils

import androidx.recyclerview.widget.RecyclerView

open class RecyclerViewLoadMoreListener(val onRequest: () -> Unit) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager
        if (layoutManager != null) {
            val childView = layoutManager.getChildAt(0)
            if (childView != null) {
                if (layoutManager.itemCount == layoutManager.getPosition(childView) + recyclerView.childCount) {
                    onRequest()
                }
            }
        }
    }
}