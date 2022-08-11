package com.jasoseol.test

import android.webkit.WebView
import androidx.multidex.MultiDexApplication
import com.jasoseol.test.retrofits.NaverAPIController

class JasoseolTestApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        // API 컨트롤러에 사용할 Context 초기화
        NaverAPIController.instance.init(applicationContext)

        // 디버깅일때 웹뷰 디버깅을 할 수 있도록
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }
}