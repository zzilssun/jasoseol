package com.jasoseol.test.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by Son Aujili on 2022/08/13.
 */


fun View.clicksDelay(): Observable<Unit> {
    return clicks().throttleFirst(1, TimeUnit.SECONDS)
}

/**
 * 뷰에 포커싱을 주고 스크롤이 가능하면 해당 위치로 스크롤을 시킨다.
 */
fun View.requestFocusAndScroll() {
    requestFocus()
    parent.requestChildFocus(this, this)
}

/**
 * 키보드를 표시한다
 */
fun Activity.showKeyboard() {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val v = currentFocus ?: return
    inputManager.showSoftInput(v, InputMethodManager.SHOW_FORCED)
}

/**
 * 키보드를 숨긴다
 */
fun AppCompatEditText.hideKeyboard(context: Context) {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Convert JsonString to Model
 */
fun <T> String.toModel(entityClass: Class<T>): T {
    return try {
        Gson().fromJson(this, entityClass)
    } catch (e: Exception) {
        Gson().fromJson("{}", entityClass)
    }
}

/**
 * Convert JsonString to ArrayList<T>
 */
fun <T> String.toModelList(entityClass: Class<T>): ArrayList<T> {
    return try {
        Gson().fromJson(this, TypeToken.getParameterized(ArrayList::class.java, entityClass).type)
    } catch (e: Exception) {
        arrayListOf()
    }
}