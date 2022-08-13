package com.jasoseol.test.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class RecyclerViewHolder<T : ViewBinding> : RecyclerView.ViewHolder {
    private var _viewBinding: T
    val viewBinding: T
        get() {
            return _viewBinding
        }

    constructor(viewBinding: T) : super(viewBinding.root) {
        _viewBinding = viewBinding
    }

    constructor(viewBinding: T, view: View) : super(view) {
        _viewBinding = viewBinding
    }
}