package com.jasoseol.test.retrofits.bases

open class BaseListResponseModel<T> {
    var lastBuildDate = ""
    var total = 0
    var start = 0
    var display = 0
    val items = ArrayList<T>()
}