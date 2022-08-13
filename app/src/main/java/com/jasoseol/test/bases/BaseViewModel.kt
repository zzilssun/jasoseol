package com.jasoseol.test.bases

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Created by Son Aujili on 2022/08/11.
 */
open class BaseViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun clear() {
        compositeDisposable.clear()
    }
}