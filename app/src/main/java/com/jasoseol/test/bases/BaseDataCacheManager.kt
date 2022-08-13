package com.jasoseol.test.bases

import android.app.Activity
import com.google.gson.Gson
import com.jasoseol.test.utils.FileUtil
import com.jasoseol.test.utils.toModel
import com.jasoseol.test.utils.toModelList

open class BaseDataCacheManager {

    /**
     * 데이터 캐싱하기
     */
    fun <T> saveCacheData(activity: Activity, fileName: String, contents: T, onComplete: ((filePath: String?) -> Unit)? = null) {
        FileUtil.writeFile(FileUtil.getInnerDir(activity, DIR_NAME).path, fileName, Gson().toJson(contents), onComplete)
    }

    /**
     * 리스트 데이터 캐시 데이터 가져오기
     */
    fun <T> getCacheData(activity: Activity, fileName: String, entityClass: Class<T>): T? {
        var result: T? = null
        FileUtil.readFile(FileUtil.getInnerDir(activity, DIR_NAME).path, fileName)?.let { content ->
            result = content.toModel(entityClass)
        }

        return result
    }

    /**
     * 리스트 데이터 캐싱하기
     */
    fun <T> saveCacheListData(activity: Activity, fileName: String, contents: ArrayList<T>, onComplete: ((filePath: String?) -> Unit)? = null) {
        FileUtil.writeFile(FileUtil.getInnerDir(activity, DIR_NAME).path, fileName, Gson().toJson(contents), onComplete)
    }

    /**
     * 리스트 데이터 캐시 데이터 가져오기
     */
    fun <T> getCacheListData(activity: Activity, fileName: String, entityClass: Class<T>): ArrayList<T> {
        val result = ArrayList<T>()

        FileUtil.readFile(FileUtil.getInnerDir(activity, DIR_NAME).path, fileName)?.let { content ->
            if (content.isNotEmpty()) {
                result.addAll(content.toModelList(entityClass))
            }
        }

        return result
    }

    companion object {
        private const val DIR_NAME = "data_cache"
    }
}