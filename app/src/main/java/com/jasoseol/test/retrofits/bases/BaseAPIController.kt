package com.jasoseol.test.retrofits.bases

import android.content.Context
import com.google.gson.GsonBuilder
import com.jasoseol.test.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class BaseAPIController {
    protected lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    /**
     * OkHttpClient 를 가져온다.
     */
    protected fun getOkHttpClient(): OkHttpClient.Builder {
        val okHttpBuilder = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor {
                it.proceed(it.request().newBuilder().apply {
                    header(Header_X_NAVER_CLIENT_ID, "v7RecvOhQ1Acz9QRhmTQ")
                    header(Header_X_NAVER_CLIENT_SECRET, "by09oSWr2J")
                }.build())
            }

        // 디버깅 모드에서만 Http 로그를 찍는다.
        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        return okHttpBuilder
    }

    /**
     * Gson Converter 를 사용하는 Retrofit 2 를 가져온다.
     */
    protected fun getRetrofitWithGsonConverter(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()

        return Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    companion object {
        const val Header_X_NAVER_CLIENT_ID = "X-Naver-Client-Id"
        const val Header_X_NAVER_CLIENT_SECRET = "X-Naver-Client-Secret"
    }
}