package com.jasoseol.test

import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by Son Aujili on 2022/08/10.
 */
@GlideModule
class GlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(UnsafeOkHttpClient.unsafeOkHttpClient))
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)

        val calculator = MemorySizeCalculator.Builder(context)
            .setBitmapPoolScreens(10f)
            .build()
        builder.setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))
    }

    object UnsafeOkHttpClient {
        val unsafeOkHttpClient: OkHttpClient
            get() {
                return try {
                    // Create a trust manager that does not validate certificate chains
                    val trustAllCerts: Array<TrustManager> = arrayOf(
                        @SuppressLint("CustomX509TrustManager")
                        object : X509TrustManager {
                            @SuppressLint("TrustAllX509TrustManager")
                            @Throws(CertificateException::class)
                            override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                            }

                            @SuppressLint("TrustAllX509TrustManager")
                            @Throws(CertificateException::class)
                            override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                            }

                            override fun getAcceptedIssuers(): Array<X509Certificate> {
                                return arrayOf()
                            }
                        }
                    )

                    // Install the all-trusting trust manager
                    val sslContext: SSLContext = SSLContext.getInstance("SSL")
                    sslContext.init(null, trustAllCerts, SecureRandom())

                    // Create an ssl socket factory with our all-trusting manager
                    val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
                    val builder = OkHttpClient.Builder()
                    builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    builder.hostnameVerifier { _, _ -> true }

                    builder.build()
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
    }
}