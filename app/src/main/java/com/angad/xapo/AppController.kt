package com.angad.xapo

import android.app.Application
import com.angad.xapo.helpers.AppUtils
import com.angad.xapo.networks.AndroidTrendingRepoService
import com.facebook.drawee.backends.pipeline.Fresco
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author Angad Tiwari
 * @Date 11 Oct 2018
 * @comment Application class
 */
class AppController: Application() {

    override fun onCreate() {
        super.onCreate()

        Fresco.initialize(this) //initializing the fresco
        initRetrofit2() //initializing the retrofit2
    }

    companion object {
        var service: AndroidTrendingRepoService? = null

        /**
         * get retrofit service object
         */
        fun getRetrofitService(): AndroidTrendingRepoService? {
            if(service == null) { // if service object wasn't initialize then, initialize first
                initRetrofit2()
            }
            return service
        }

        /**
         * initialize the retrofit2 networking, including http logging & gson converter factory
         */
        private fun initRetrofit2() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(AppUtils.GITHUB_API_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            service = retrofit.create<AndroidTrendingRepoService>(AndroidTrendingRepoService::class.java)
        }
    }
}